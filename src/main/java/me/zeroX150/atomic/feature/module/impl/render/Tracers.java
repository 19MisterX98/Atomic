package me.zeroX150.atomic.feature.module.impl.render;

import me.zeroX150.atomic.Atomic;
import me.zeroX150.atomic.feature.module.Module;
import me.zeroX150.atomic.feature.module.ModuleType;
import me.zeroX150.atomic.feature.module.config.BooleanValue;
import me.zeroX150.atomic.helper.Renderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;

import java.awt.*;
import java.util.Comparator;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class Tracers extends Module {
    BooleanValue fancy = (BooleanValue) this.config.create("Fancy tracers", false).description("Whether or not to show fancy tracers");
    BooleanValue entities = (BooleanValue) this.config.create("Show Entities", false).description("Whether or not to show entities");
    BooleanValue players = (BooleanValue) this.config.create("Show Players", true).description("Whether or not to show players");

    public Tracers() {
        super("Tracers", "hehe tracer", ModuleType.RENDER);
    }

    @Override
    public void tick() {

    }

    @Override
    public void enable() {

    }

    @Override
    public void disable() {

    }

    @Override
    public String getContext() {
        if (Atomic.client.world == null || Atomic.client.player == null) return null;
        return StreamSupport.stream(Atomic.client.world.getEntities().spliterator(), false).filter(entity -> entity.squaredDistanceTo(Atomic.client.player) < 4096 && entity.getUuid() != Atomic.client.player.getUuid() && isEntityApplicable(entity)).count() + "";
    }

    Vec2f getPY(Vec3d target1) {
        Camera c = Atomic.client.gameRenderer.getCamera();
        double vec = 57.2957763671875;
        Vec3d target = target1.subtract(c.getPos());
        double square = Math.sqrt(target.x * target.x + target.z * target.z);
        float pitch = MathHelper.wrapDegrees((float) (-(MathHelper.atan2(target.y, square) * vec)));
        float yaw = MathHelper.wrapDegrees((float) (MathHelper.atan2(target.z, target.x) * vec) - 90.0F);
        return new Vec2f(pitch, yaw);
    }

    @Override
    public void onWorldRender(MatrixStack matrices) {
        if (Atomic.client.world == null || Atomic.client.player == null) return;
        for (Entity entity : StreamSupport.stream(Atomic.client.world.getEntities().spliterator(), false).sorted(Comparator
                .comparingDouble(value -> -value.distanceTo(Atomic.client.player))).collect(Collectors.toList())) {
            if (entity.squaredDistanceTo(Atomic.client.player) > 4096) continue;
            double dc = entity.squaredDistanceTo(Atomic.client.player) / 4096;
            dc = Math.abs(1 - dc);
            if (entity.getUuid().equals(Atomic.client.player.getUuid())) continue;
            Color c;
            if (entity instanceof PlayerEntity) c = Color.RED;
            else if (entity instanceof ItemEntity) c = Color.CYAN;
            else if (entity instanceof EndermanEntity enderman) {
                if (enderman.isProvoked()) {
                    c = Color.YELLOW;
                } else c = Color.GREEN;
            } else if (entity instanceof HostileEntity) c = Color.YELLOW;
            else c = Color.GREEN;
            c = Renderer.modify(c, -1, -1, -1, (int) Math.floor(dc * 255));
            if (isEntityApplicable(entity)) {
                if (fancy.getValue()) {
                    Vec3d ppos = entity.getPos().add(0, entity.getHeight() / 2, 0);
                    Vec2f f = getPY(ppos);
                    double v = 0.5;
                    double yaw = f.y;
                    double pitch = f.x;
                    double rad = Math.toRadians(yaw);
                    double sin = Math.sin(rad) * v;
                    double cos = Math.cos(rad) * v;
                    double rad1 = Math.toRadians(pitch);
                    double sin1 = Math.sin(rad1);
                    Vec3d vv = Renderer.getCrosshairVector().add(-sin, -sin1, cos);
                    Vec3d vv2 = Renderer.getCrosshairVector().add(-sin / 4, -sin1 / 4, cos / 4);
                    Renderer.line(vv2, vv, c, matrices);
                } else
                    Renderer.line(Renderer.getCrosshairVector(), entity.getPos().add(0, entity.getHeight() / 2, 0), c, matrices);
            }
        }
    }

    boolean isEntityApplicable(Entity v) {
        return (v instanceof PlayerEntity && players.getValue()) || entities.getValue();
    }

    @Override
    public void onHudRender() {

    }
}

