package me.zeroX150.atomic.feature.module.impl.combat;

import me.zeroX150.atomic.Atomic;
import me.zeroX150.atomic.feature.module.Module;
import me.zeroX150.atomic.feature.module.ModuleType;
import me.zeroX150.atomic.helper.Packets;
import me.zeroX150.atomic.helper.Renderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.util.math.Vec3d;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class AutoEndermanAngry extends Module {
    Entity e;
    int t = 0;

    public AutoEndermanAngry() {
        super("AutoEndermanAngry", "automatically makes the nearest enderman pissed", ModuleType.COMBAT);
    }

    @Override
    public void tick() {
        t++;
        if (t > 6) {
            t = 0;
        } else return;
        this.e = null;
        List<Entity> e1 = StreamSupport.stream(Atomic.client.world.getEntities().spliterator(), false).filter(entity -> entity.getType() == EntityType.ENDERMAN && entity.getPos().distanceTo(Atomic.client.player.getPos()) < 100 && !((EndermanEntity) entity).isProvoked() && this.e != entity).collect(Collectors.toList());
        List<Entity> e = new ArrayList<>();
        for (Entity entity : e1) {
            e.add((int) Math.floor(Math.random() * e.size()), entity);
        }
        if (e.size() > 0) {
            this.e = e.get(0);
            Packets.sendServerSideLook(this.e.getEyePos());
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
        if (e != null) {
            Vec3d dim = new Vec3d(e.getWidth() / 2, 0.05, e.getWidth() / 2);
            Renderer.renderOutline(e.getEyePos().subtract(dim), dim.multiply(2), Color.RED, matrices);
            Renderer.line(Renderer.getCrosshairVector(), e.getEyePos(), Color.WHITE, matrices);
        }
    }

    @Override
    public void onHudRender() {

    }
}

