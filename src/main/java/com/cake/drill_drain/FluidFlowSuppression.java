package com.cake.drill_drain;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.BlockEvent;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Temporarily prevents fluids from placing new fluid blocks in, or flowing out of,
 * the small area surrounding a block removed by the Drill Drain.
 */
@EventBusSubscriber(modid = CreateDrillDrain.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public final class FluidFlowSuppression {
    private static final Map<Level, Map<Long, Long>> PROTECTED_POSITIONS = new WeakHashMap<>();

    private FluidFlowSuppression() {}

    public static void protectAround(LevelAccessor levelAccessor, BlockPos center, int radius, int durationTicks) {
        if (!(levelAccessor instanceof Level level) || level.isClientSide) {
            return;
        }

        long expiresAt = level.getGameTime() + durationTicks;
        synchronized (PROTECTED_POSITIONS) {
            Map<Long, Long> positions = PROTECTED_POSITIONS.computeIfAbsent(level, ignored -> new HashMap<>());
            BlockPos.betweenClosedStream(
                center.offset(-radius, -radius, -radius),
                center.offset(radius, radius, radius)
            ).forEach(pos -> positions.merge(pos.asLong(), expiresAt, Math::max));
        }
    }

    /** Returns true while a fluid tick at this position should be suppressed. */
    public static boolean isProtected(Level level, BlockPos pos) {
        if (level == null || level.isClientSide) {
            return false;
        }
        long now = level.getGameTime();
        synchronized (PROTECTED_POSITIONS) {
            Map<Long, Long> positions = PROTECTED_POSITIONS.get(level);
            if (positions == null || positions.isEmpty()) {
                return false;
            }
            Iterator<Map.Entry<Long, Long>> iterator = positions.entrySet().iterator();
            while (iterator.hasNext()) {
                if (iterator.next().getValue() <= now) {
                    iterator.remove();
                }
            }
            boolean protectedPosition = positions.containsKey(pos.asLong());
            if (positions.isEmpty()) {
                PROTECTED_POSITIONS.remove(level);
            }
            return protectedPosition;
        }
    }

    @SubscribeEvent
    public static void onFluidPlace(BlockEvent.FluidPlaceBlockEvent event) {
        if (!(event.getLevel() instanceof Level level) || level.isClientSide) {
            return;
        }

        long now = level.getGameTime();
        boolean suppress = false;
        synchronized (PROTECTED_POSITIONS) {
            Map<Long, Long> positions = PROTECTED_POSITIONS.get(level);
            if (positions == null || positions.isEmpty()) {
                return;
            }

            Iterator<Map.Entry<Long, Long>> iterator = positions.entrySet().iterator();
            while (iterator.hasNext()) {
                if (iterator.next().getValue() <= now) {
                    iterator.remove();
                }
            }

            suppress = positions.containsKey(event.getPos().asLong())
                || positions.containsKey(event.getLiquidPos().asLong());
            if (positions.isEmpty()) {
                PROTECTED_POSITIONS.remove(level);
            }
        }

        if (suppress) {
            event.setCanceled(true);
        }
    }
}
