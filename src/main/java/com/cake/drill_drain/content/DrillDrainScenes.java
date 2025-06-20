package com.cake.drill_drain.content;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.simibubi.create.content.fluids.pump.PumpBlock;
import com.simibubi.create.content.fluids.tank.FluidTankBlockEntity;
import com.simibubi.create.content.kinetics.simpleRelays.ShaftBlock;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock;
import com.simibubi.create.foundation.ponder.CreateSceneBuilder;
import net.createmod.catnip.data.Iterate;
import net.createmod.catnip.math.Pointing;
import net.createmod.ponder.api.PonderPalette;
import net.createmod.ponder.api.element.ElementLink;
import net.createmod.ponder.api.element.WorldSectionElement;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.createmod.ponder.api.scene.Selection;
import net.minecraft.client.particle.Particle;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class DrillDrainScenes {

    public static void drillDrainScene(SceneBuilder builder, SceneBuildingUtil util) {

        CreateSceneBuilder scene = new CreateSceneBuilder(builder);
        scene.title("drill_drain", "Using the Drill Drain");
        scene.configureBasePlate(0, 0, 7);
        scene.showBasePlate();

        Selection innerKineticsRegion = util.select().fromTo(6, 1, 0, 6, 1, 6);
        Selection drillKineticsRegion = util.select().fromTo(2, 1, 5, 4, 1, 5);

        scene.world().showSection(util.select().fromTo(7, 0, 0, 7, 1, 6), Direction.WEST);
        scene.idle(5);
        scene.world().showSection(util.select().fromTo(5, 1, 0, 6, 1, 6), Direction.DOWN);
        scene.idle(5);
        scene.world().showSection(drillKineticsRegion, Direction.SOUTH);
        scene.idle(5);
        scene.world().showSection(util.select().fromTo(4, 1, 6, 4, 4, 6), Direction.NORTH);
        scene.idle(5);
        scene.world().showSection(util.select().position(3, 1, 6), Direction.EAST);

        scene.idle(20);
        scene.world().showSection(util.select().fromTo(2, 1, 2, 4, 1, 4), Direction.UP);

        scene.idle(40);

        scene.overlay().showOutlineWithText(
            util.select().position(3, 1, 6),
            60
        )
            .attachKeyFrame()
            .text("The Drill Drain can be placed behind a drill, it will connect to a range of blocks in front of it")
            .colored(PonderPalette.INPUT);
        scene.overlay().showOutline(
            PonderPalette.OUTPUT, "connected_to",
            drillKineticsRegion,
            60
        );
        scene.idle(80);

        scene.world().setKineticSpeed(innerKineticsRegion, -32);
        scene.world().setKineticSpeed(util.select().fromTo(7, 0, 0, 7, 0, 6), -64);
        scene.world().setKineticSpeed(util.select().fromTo(7, 1, 0, 7, 1, 6), 32);
        scene.world().setKineticSpeed(drillKineticsRegion, 32);

        ElementLink<WorldSectionElement> movingSection =
            scene.world().makeSectionIndependent(util.select().fromTo(2, 1, 5, 5, 4, 6));

        scene.world().moveSection(movingSection, new Vec3(0, 0, -4), 60);

        for (int i = 0; i < 3; i++) {
            int finalI = i;
            scene.world().modifyBlockEntity(new BlockPos(4, 1, 6), FluidTankBlockEntity.class, be -> {
                be.getTankInventory().fill(new FluidStack(Fluids.WATER, 3000), IFluidHandler.FluidAction.EXECUTE);
            });
            scene.world().setBlocks(util.select().fromTo(2, 1, 4 - i, 4, 1, 4 - i), Blocks.AIR.defaultBlockState(), false);
            scene.idle(15);
        }
        scene.idle(60 - 15 * 3);
        scene.world().setKineticSpeed(drillKineticsRegion, 0);

        scene.idle(40);

        scene.overlay().showOutlineWithText(
                util.select().fromTo(4, 1, 2, 4, 3, 2),
                80
            )
            .attachKeyFrame()
            .text("Attached fluid tanks will fill with the fluid captured by the drill,")
            .colored(PonderPalette.WHITE);
        scene.idle(90);

        scene.overlay().showOutlineWithText(
                util.select().fromTo(4, 1, 2, 4, 3, 2),
                60
            )
            .attachKeyFrame()
            .text("There must be space in the contraption for the fluid to be stored otherwise it will not pick up any fluid.")
            .colored(PonderPalette.WHITE);
        scene.idle(80);

        scene.markAsFinished();
    }

}
