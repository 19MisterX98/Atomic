package me.zeroX150.atomic.feature.module.impl.movement;

import me.zeroX150.atomic.Atomic;
import me.zeroX150.atomic.feature.module.Module;
import me.zeroX150.atomic.feature.module.ModuleType;
import me.zeroX150.atomic.feature.module.config.BooleanValue;
import me.zeroX150.atomic.helper.Client;
import me.zeroX150.atomic.helper.Renderer;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class ClickTP extends Module {
    BooleanValue autoDisable = this.config.create("Auto disable", false);
    BooleanValue smartTarget = this.config.create("Smart target", true);

    BlockPos goingToTeleportTo = null;
    boolean flag = false;

    public ClickTP() {
        super("ClickTP", "Teleports you to whereever you look at", ModuleType.MOVEMENT);
    }

    @Override
    public void tick() {
        HitResult hr = Atomic.client.player.raycast(1000, 0, false);
        if (!(hr instanceof BlockHitResult bhr)) goingToTeleportTo = null;
        else {
            BlockState br = Atomic.client.world.getBlockState(bhr.getBlockPos());
            if (!br.isAir()) {
                boolean set = true;
                if (smartTarget.getValue()) {
                    set = !Atomic.client.world.getBlockState(bhr.getBlockPos().up()).getMaterial().blocksMovement()
                            && !Atomic.client.world.getBlockState(bhr.getBlockPos().up().up()).getMaterial().blocksMovement();
                }
                if (set) goingToTeleportTo = bhr.getBlockPos();
                else goingToTeleportTo = null;
            } else goingToTeleportTo = null;
        }
        if (Atomic.client.options.keyUse.isPressed() && !flag) {
            if (goingToTeleportTo != null) {
                flag = true;
                if (autoDisable.getValue()) toggle();
                new Thread(() -> {
                    Vec3d base = Atomic.client.player.getPos();
                    Vec3d goal = new Vec3d(goingToTeleportTo.getX() + .5, goingToTeleportTo.getY() + 2, goingToTeleportTo.getZ() + .5);
                    double dist = base.distanceTo(goal);
                    Atomic.client.player.jump();
                    Atomic.client.player.setNoGravity(true);
                    for (double i = 0; i < dist; i += 0.2) {
                        System.out.println(i / dist);
                        Vec3d c = new Vec3d(Renderer.lerp(base.x, goal.x, i / dist), Renderer.lerp(base.y, goal.y, i / dist), Renderer.lerp(base.z, goal.z, i / dist));
                        BlockState bs = Atomic.client.world.getBlockState(new BlockPos(c));
                        if (bs.getMaterial().blocksMovement()) continue;

                        Atomic.client.player.updatePosition(c.x, c.y, c.z);
                        try {
                            Thread.sleep(2);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    Atomic.client.player.setNoGravity(false);
                }).start();
            }
        } else if (!Atomic.client.options.keyUse.isPressed()) flag = false;
    }

    @Override
    public void enable() {

    }

    @Override
    public void disable() {

    }

    @Override
    public String getContext() {
        if (goingToTeleportTo != null) {
            Vec3d v = new Vec3d(goingToTeleportTo.getX(), goingToTeleportTo.getY(), goingToTeleportTo.getZ());
            return (int) Client.roundToN(v.distanceTo(Atomic.client.player.getPos()), 0) + "";
        }
        return null;
    }

    @Override
    public void onWorldRender(MatrixStack matrices) {
        if (goingToTeleportTo != null) {
            Vec3d bruh = new Vec3d(goingToTeleportTo.getX(), goingToTeleportTo.getY(), goingToTeleportTo.getZ());
            Renderer.renderFilled(bruh, new Vec3d(1, 1, 1), Client.getCurrentRGB(), matrices);
        }
    }

    @Override
    public void onHudRender() {

    }
}

