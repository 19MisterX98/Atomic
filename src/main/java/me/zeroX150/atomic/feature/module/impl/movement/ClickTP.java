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
    BooleanValue tpUp = this.config.create("2 stage tp", false);
    BooleanValue smartTarget = this.config.create("Smart target", true);

    BlockPos goingToTeleportTo = null;
    boolean tpInProgress = false;

    public ClickTP() {
        super("ClickTP", "Teleports you to whereever you look at", ModuleType.MOVEMENT);
    }

    @Override
    public void tick() {
        HitResult hr = Atomic.client.player.raycast(100, 0, false);
        if (!(hr instanceof BlockHitResult bhr)) goingToTeleportTo = null;
        else if (!tpInProgress) {
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
        if (tpInProgress) {
            if (autoDisable.getValue()) toggle();
            Atomic.client.player.updatePosition(goingToTeleportTo.getX() + .5, goingToTeleportTo.getY() + 1, goingToTeleportTo.getZ() + .5);
            tpInProgress = false;
        }
        if (Atomic.client.options.keySneak.wasPressed()) {
            if (goingToTeleportTo != null && !tpInProgress) {
                if (tpUp.getValue()) {
                    tpInProgress = true;
                    Vec3d ppos = Atomic.client.player.getPos();
                    Atomic.client.player.updatePosition(ppos.x, ppos.y + 3, ppos.z);
                } else {
                    if (autoDisable.getValue()) toggle();
                    Atomic.client.player.updatePosition(goingToTeleportTo.getX() + .5, goingToTeleportTo.getY() + 1, goingToTeleportTo.getZ() + .5);
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

