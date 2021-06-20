package me.zeroX150.atomic.feature.module.impl.movement;

import me.zeroX150.atomic.Atomic;
import me.zeroX150.atomic.feature.module.Module;
import me.zeroX150.atomic.feature.module.ModuleType;
import me.zeroX150.atomic.feature.module.config.BooleanValue;
import me.zeroX150.atomic.feature.module.config.MultiValue;
import me.zeroX150.atomic.feature.module.config.SliderValue;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.Vec3d;

import java.util.Random;

public class Flight extends Module {
    MultiValue mode = this.config.create("Mode", "Static", "Vanilla", "Static", "3D", "Jetpack");
    BooleanValue bypassVanillaAc = this.config.create("Vanilla AC bypass", true);
    SliderValue speed = this.config.create("Speed", 2, 0.1, 10, 1);

    int bypassTimer = 0;

    public Flight() {
        super("Flight", "weee 2", ModuleType.MOVEMENT);
    }

    @Override
    public void tick() {
        if (Atomic.client.player == null || Atomic.client.world == null || Atomic.client.getNetworkHandler() == null) return;
        double speed = this.speed.getValue();
        if (bypassVanillaAc.getValue()) {
            bypassTimer++;
            if (bypassTimer > 10) {
                bypassTimer = 0;
                Vec3d p = Atomic.client.player.getPos();
                Atomic.client.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(p.x, p.y - 0.2, p.z, false));
                Atomic.client.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(p.x, p.y + 0.2, p.z, false));
            }
        }
        switch (mode.getValue().toLowerCase()) {
            case "vanilla":
                Atomic.client.player.getAbilities().flying = true;
                break;
            case "static":
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
                double ts = speed / 2;
                double s = Math.sin(Math.toRadians(y));
                double c = Math.cos(Math.toRadians(y));
                double nx = ts * mz * s;
                double nz = ts * mz * -c;
                double ny = ts * my;
                nx += ts * mx * -c;
                nz += ts * mx * -s;
                Vec3d nv3 = new Vec3d(nx, ny, nz);
                Atomic.client.player.setVelocity(nv3);
                break;
            case "jetpack":
                if (Atomic.client.options.keyJump.isPressed()) {
                    assert Atomic.client.player != null;
                    Atomic.client.player.addVelocity(0, speed / 30, 0);
                    Vec3d vp = Atomic.client.player.getPos();
                    for (int i = 0; i < 10; i++) {
                        Random r = new Random();
                        Atomic.client.world.addParticle(ParticleTypes.SOUL_FIRE_FLAME, vp.x, vp.y, vp.z,
                                (r.nextDouble() * 0.25) - .125, (r.nextDouble() * 0.25) - .125, (r.nextDouble() * 0.25) - .125);
                    }
                }
                break;
            case "3d":
                Atomic.client.player.setVelocity(Atomic.client.player.getRotationVector().multiply(speed));
                break;

        }
    }

    @Override
    public void enable() {
        bypassTimer = 0;
    }

    @Override
    public void disable() {

    }

    @Override
    public String getContext() {
        return mode.getValue();
    }

    @Override
    public void onWorldRender(MatrixStack matrices) {

    }

    @Override
    public void onHudRender() {

    }
}
