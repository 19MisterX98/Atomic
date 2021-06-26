package me.zeroX150.atomic.feature.module.impl.render;

import me.zeroX150.atomic.Atomic;
import me.zeroX150.atomic.feature.module.Module;
import me.zeroX150.atomic.feature.module.ModuleType;
import me.zeroX150.atomic.feature.module.config.BooleanValue;
import me.zeroX150.atomic.feature.module.config.SliderValue;
import me.zeroX150.atomic.helper.Client;
import me.zeroX150.atomic.helper.event.Event;
import me.zeroX150.atomic.helper.event.EventSystem;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EntityPose;
import net.minecraft.network.packet.c2s.play.*;
import net.minecraft.util.math.Vec3d;

public class Freecam extends Module {
    SliderValue speed = this.config.create("Speed", 1, 0, 10, 1);
    BooleanValue blockInteractions = this.config.create("Block interactions", true);
    Vec3d startloc;
    float pitch = 0f;
    float yaw = 0f;

    public Freecam() {
        super("Freecam", "p", ModuleType.RENDER);
        EventSystem.registerEventHandler(Event.PACKET_SEND, event -> {
            if (!this.isEnabled()) return;
            if (event.getPacket() instanceof PlayerMoveC2SPacket) event.setCancelled(true);
            if (event.getPacket() instanceof PlayerInputC2SPacket) event.setCancelled(true);
            if (blockInteractions.getValue()) {
                if (event.getPacket() instanceof PlayerInteractBlockC2SPacket) event.setCancelled(true);
                if (event.getPacket() instanceof PlayerInteractEntityC2SPacket) event.setCancelled(true);
                if (event.getPacket() instanceof PlayerInteractItemC2SPacket) event.setCancelled(true);
            }
        });
    }

    @Override
    public void tick() {

    }

    @Override
    public void enable() {
        Client.startBlockingMovement();
        startloc = Atomic.client.player.getPos();
        pitch = Atomic.client.player.getPitch();
        yaw = Atomic.client.player.getYaw();
        Atomic.client.gameRenderer.setRenderHand(false);
    }

    @Override
    public void disable() {
        Client.stopBlockingMovement();
        if (startloc != null) {
            Atomic.client.player.updatePosition(startloc.x, startloc.y, startloc.z);
        }
        startloc = null;
        Atomic.client.player.setYaw(yaw);
        Atomic.client.player.setPitch(pitch);
        yaw = pitch = 0f;
        Atomic.client.player.noClip = false;
        Atomic.client.getCameraEntity().noClip = false;
        Atomic.client.gameRenderer.setRenderHand(true);
    }

    @Override
    public String getContext() {
        return null;
    }

    @Override
    public void onWorldRender(MatrixStack matrices) {
        Atomic.client.player.setSwimming(false);
        Atomic.client.player.setPose(EntityPose.STANDING);
        Atomic.client.player.noClip = true;
        Atomic.client.getCameraEntity().noClip = true;
    }

    @Override
    public void onFastTick() {
        GameOptions go = Atomic.client.options;
        float y = Atomic.client.player.getYaw();
        int mx = 0, my = 0, mz = 0;

        if (go.keyJump.isPressed())
            my++;
        if (go.keyBack.isPressed())
            mz++;
        if (go.keyLeft.isPressed())
            mx--;
        if (go.keyRight.isPressed())
            mx++;
        if (go.keySneak.isPressed())
            my--;
        if (go.keyForward.isPressed())
            mz--;
        double ts = speed.getValue() / 200;
        double s = Math.sin(Math.toRadians(y));
        double c = Math.cos(Math.toRadians(y));
        double nx = ts * mz * s;
        double nz = ts * mz * -c;
        double ny = ts * my;
        nx += ts * mx * -c;
        nz += ts * mx * -s;
        Vec3d nv3 = new Vec3d(nx, ny, nz);
        Vec3d ppos = Atomic.client.player.getPos().add(nv3);
        Atomic.client.player.updatePosition(ppos.x, ppos.y, ppos.z);

        Atomic.client.player.setVelocity(0, 0, 0);
    }

    @Override
    public void onHudRender() {

    }
}

