package me.zeroX150.atomic.feature.module.impl.render;

import me.zeroX150.atomic.Atomic;
import me.zeroX150.atomic.feature.module.Module;
import me.zeroX150.atomic.feature.module.ModuleType;
import me.zeroX150.atomic.feature.module.config.BooleanValue;
import me.zeroX150.atomic.helper.Client;
import me.zeroX150.atomic.helper.Renderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;

import java.awt.*;
import java.util.stream.StreamSupport;

public class Tracers extends Module {
    BooleanValue onlyBox = this.config.create("Only show box", false);
    BooleanValue entities = this.config.create("Show Entities", false);
    BooleanValue players = this.config.create("Show Players", true);

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
        return StreamSupport.stream(Atomic.client.world.getEntities().spliterator(), false).filter(entity -> entity.squaredDistanceTo(Atomic.client.player) < 4096 && entity.getUuid() != Atomic.client.player.getUuid()).count() + "";
    }

    @Override
    public void onWorldRender(MatrixStack matrices) {
        if (Atomic.client.world == null || Atomic.client.player == null) return;
        for (Entity entity : Atomic.client.world.getEntities()) {
            if (entity.squaredDistanceTo(Atomic.client.player) > 4096) continue;
            if (entity.getUuid().equals(Atomic.client.player.getUuid())) continue;
            Color c;
            if (entity instanceof PlayerEntity) c = Color.RED;
            else if (entity instanceof ItemEntity) c = Color.CYAN;
            else if (entity instanceof HostileEntity) c = Color.YELLOW;
            else c = Color.GREEN;
            if (((entity instanceof PlayerEntity && players.getValue()) || entities.getValue())) {
                Renderer.renderFilled(entity.getPos().subtract(new Vec3d(entity.getWidth(), 0, entity.getWidth()).multiply(0.5)), new Vec3d(entity.getWidth(), entity.getHeight(), entity.getWidth()), Renderer.modify(Client.getCurrentRGB(), -1, -1, -1, 130), matrices);
                //Renderer.renderOutline(entity.getPos().subtract(new Vec3d(entity.getWidth(), 0, entity.getWidth()).multiply(0.5)), new Vec3d(entity.getWidth(), entity.getHeight(), entity.getWidth()), Client.getCurrentRGB(), matrices);
                if (!onlyBox.getValue())
                    Renderer.line(Renderer.getCrosshairVector(), entity.getPos().add(0, entity.getHeight() / 2, 0), c, matrices);
            }
        }
    }

    @Override
    public void onHudRender() {

    }
}

