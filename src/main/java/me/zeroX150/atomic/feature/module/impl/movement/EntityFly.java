package me.zeroX150.atomic.feature.module.impl.movement;

import me.zeroX150.atomic.Atomic;
import me.zeroX150.atomic.feature.module.Module;
import me.zeroX150.atomic.feature.module.ModuleType;
import me.zeroX150.atomic.helper.Client;
import me.zeroX150.atomic.helper.keybind.Keybind;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.network.packet.c2s.play.VehicleMoveC2SPacket;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

public class EntityFly extends Module {
    Entity lastRide = null;
    Keybind down = new Keybind(GLFW.GLFW_KEY_LEFT_ALT);

    public EntityFly() {
        super("EntityFly", "flies using entities", ModuleType.MOVEMENT);
    }

    @Override
    public void tick() {
        if (Atomic.client.player == null || Atomic.client.getNetworkHandler() == null) return;
        Entity vehicle = Atomic.client.player.getVehicle();
        if (vehicle == null) return;
        lastRide = vehicle;
        vehicle.setNoGravity(true);
        if (vehicle instanceof MobEntity) {
            ((MobEntity) vehicle).setAiDisabled(true);
        }
        Vec3d entityPos = vehicle.getPos();
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
        if (down.isHeld())
            my--;
        if (go.keyForward.isPressed())
            mz--;
        double ts = 1;
        double s = Math.sin(Math.toRadians(y));
        double c = Math.cos(Math.toRadians(y));
        double nx = ts * mz * s;
        double nz = ts * mz * -c;
        double ny = ts * my;
        nx += ts * mx * -c;
        nz += ts * mx * -s;
        Vec3d nv3 = new Vec3d(nx, ny, nz);
        entityPos = entityPos.add(nv3.multiply(0.4));
        vehicle.updatePosition(entityPos.x, entityPos.y, entityPos.z);
        vehicle.setVelocity(0, 0, 0);
        vehicle.setYaw(Atomic.client.player.getYaw());
        VehicleMoveC2SPacket p = new VehicleMoveC2SPacket(vehicle);
        Atomic.client.getNetworkHandler().sendPacket(p);
    }

    @Override
    public void enable() {
        Client.notifyUser("alt = down. dont use shift");
    }

    @Override
    public void disable() {
        if (lastRide != null) {
            lastRide.setNoGravity(false);
        }
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
