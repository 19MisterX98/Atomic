package me.zeroX150.atomic.feature.module.impl.movement;

import me.zeroX150.atomic.Atomic;
import me.zeroX150.atomic.feature.module.Module;
import me.zeroX150.atomic.feature.module.ModuleType;
import me.zeroX150.atomic.feature.module.config.SliderValue;
import me.zeroX150.atomic.helper.Client;
import me.zeroX150.atomic.helper.Renderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class AntiVoid extends Module {
    Vec3d lastOnGround = null;
    SliderValue distance = (SliderValue) this.config.create("Fall dist",3,2,10,1).description("how many blocks to fall before tping");
    public AntiVoid() {
        super("AntiFall", "Prevents you from falling too far", ModuleType.MOVEMENT);
    }

    @Override
    public void tick() {
        if (Atomic.client.world.getBlockState(new BlockPos(Atomic.client.player.getPos().subtract(0,0.4,0))).getMaterial().blocksMovement()) {
            lastOnGround = Atomic.client.player.getPos();
        }
        if (lastOnGround != null) {
            if (Atomic.client.player.fallDistance > distance.getValue()) {
                Atomic.client.player.updatePosition(Atomic.client.player.getX(),lastOnGround.y,Atomic.client.player.getZ());
                Atomic.client.player.updatePosition(lastOnGround.x,lastOnGround.y,lastOnGround.z);
                Atomic.client.player.setVelocity(0,-1,0);
                lastOnGround = null;
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
        if (Atomic.client.player.fallDistance > 2) {
            return Client.roundToN(Atomic.client.player.fallDistance,1)+" / "+distance.getValue();
        }
        return null;
    }

    @Override
    public void onWorldRender(MatrixStack matrices) {
        if (lastOnGround != null && Atomic.client.player.fallDistance > 2) {
            Renderer.line(Atomic.client.player.getPos(),lastOnGround, Client.getCurrentRGB(),matrices);
        }
    }

    @Override
    public void onHudRender() {

    }
}

