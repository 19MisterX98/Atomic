package me.zeroX150.atomic.feature.module.impl.world;

import me.zeroX150.atomic.Atomic;
import me.zeroX150.atomic.feature.module.Module;
import me.zeroX150.atomic.feature.module.ModuleType;
import me.zeroX150.atomic.feature.module.config.SliderValue;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

public class LeverAura extends Module {
    SliderValue amount = this.config.create("Clicks per tick", 1, 1, 50, 0);

    public LeverAura() {
        super("LeverAura", "has a spasm around levers in the area", ModuleType.WORLD);
    }

    @Override
    public void tick() {
        float d = 4;
        for (double x = -d; x < d + 1; x++) {
            for (double y = -d; y < d + 1; y++) {
                for (double z = -d; z < d + 1; z++) {
                    Vec3d v = new Vec3d(x, y, z);
                    if (v.distanceTo(Vec3d.ZERO) >= Atomic.client.interactionManager.getReachDistance()) continue;
                    Vec3d a = Atomic.client.player.getPos().add(v);
                    BlockPos bb = new BlockPos(a);
                    BlockState bs = Atomic.client.world.getBlockState(bb);
                    if (bs.getBlock() == Blocks.LEVER) {
                        for (int i = 0; i < amount.getValue(); i++) {
                            BlockHitResult bhr = new BlockHitResult(a, Direction.DOWN, bb, false);
                            Atomic.client.player.swingHand(Hand.MAIN_HAND);
                            Atomic.client.interactionManager.interactBlock(Atomic.client.player, Atomic.client.world, Hand.MAIN_HAND, bhr);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void enable() {

    }

    @Override
    public void disable() {

    }

    @Override
    public String getContext() {
        return null;
    }

    @Override
    public void onWorldRender(MatrixStack matrices) {

    }

    @Override
    public void onHudRender() {

    }
}

