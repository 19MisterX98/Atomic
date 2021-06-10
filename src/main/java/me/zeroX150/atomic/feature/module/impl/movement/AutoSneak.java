package me.zeroX150.atomic.feature.module.impl.movement;

import me.zeroX150.atomic.Atomic;
import me.zeroX150.atomic.feature.module.Module;
import me.zeroX150.atomic.feature.module.ModuleType;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

public class AutoSneak extends Module {
    public AutoSneak() {
        super("AutoSneak", "Sneaks automatically", ModuleType.MOVEMENT);
    }

    @Override
    public void tick() {
        Box bounding = Atomic.client.player.getBoundingBox();
        bounding = bounding.offset(0, -1, 0);
        bounding = bounding.expand(0.3);
        //bounding = bounding.contract(0.3);
        boolean sneak = false;
        for (int x = -1; x < 2; x++) {
            for (int z = -1; z < 2; z++) {
                double xScale = x / 3d + .5;
                double zScale = z / 3d + .5;
                BlockPos current = Atomic.client.player.getBlockPos().add(x, -1, z);
                BlockState bs = Atomic.client.world.getBlockState(current);
                if (bs.isAir() && bounding.contains(new Vec3d(current.getX() + xScale, current.getY() + 1, current.getZ() + zScale))) {
                    sneak = true;
                    break;
                }
            }
        }
        //STL.notifyUser(sneak+"");
        boolean previousState = InputUtil.isKeyPressed(Atomic.client.getWindow().getHandle(), Atomic.client.options.keySneak.getDefaultKey().getCode());
        if (Atomic.client.player.isOnGround()) Atomic.client.options.keySneak.setPressed(sneak || previousState);
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
