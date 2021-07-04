package me.zeroX150.atomic.feature.module.impl.render;

import com.mojang.blaze3d.systems.RenderSystem;
import me.zeroX150.atomic.Atomic;
import me.zeroX150.atomic.feature.module.Module;
import me.zeroX150.atomic.feature.module.ModuleType;
import me.zeroX150.atomic.helper.Renderer;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class NameTags extends Module {
    Map<UUID, Double> trackedProgress = new HashMap<>();

    public NameTags() {
        super("NameTags", "big nametag.mp4", ModuleType.RENDER);
    }

    public void renderTag(Entity entity, MatrixStack matrices, EntityRenderDispatcher dispatcher) {
        RenderSystem.enableDepthTest();
        RenderSystem.disableBlend();
        RenderSystem.depthFunc(GL11.GL_ALWAYS);
        if (Atomic.client.player == null) return;
        double d = dispatcher.getSquaredDistanceToCamera(entity);
        if (d > 4096) return;
        if (entity.getUuid() == Atomic.client.player.getUuid()) return;
        String name = entity.getEntityName();
        float f = entity.getHeight() + 0.5f;
        matrices.push();
        float scale = 3f;
        scale /= 50f;
        scale *= 0.55f;
        if (Atomic.client.player.distanceTo(entity) > 10) scale *= Atomic.client.player.distanceTo(entity) / 10;
        matrices.translate(0, f + (scale * 6), 0f);
        matrices.multiply(dispatcher.getRotation());
        matrices.scale(-scale, -scale, scale);
        int health = (int) ((PlayerEntity) entity).getHealth();
        double hp = (((PlayerEntity) entity).getHealth() / ((PlayerEntity) entity).getMaxHealth());
        hp = MathHelper.clamp(hp, 0, 1);
        if (!trackedProgress.containsKey(entity.getUuid())) trackedProgress.put(entity.getUuid(), 0d);
        String t = name + " ";
        if (hp > 0.75) t += "§a";
        else if (hp > 0.5) t += "§e";
        else if (hp > 0.25) t += "§c";
        else t += "§4";
        t += health;
        float width = Atomic.client.textRenderer.getWidth(t) / 2f;
        double tp = trackedProgress.get(entity.getUuid());
        double trackDiff = hp - tp;
        trackDiff /= 10d;
        tp += trackDiff;
        RenderSystem.polygonOffset(1, -15000000);
        RenderSystem.enablePolygonOffset();
        Renderer.fill(matrices, new Color(20, 20, 20, 220), -width - 4, 8 + 2, width + 4, 1);
        Renderer.fill(matrices, new Color(0, 194, 111, 255), -width - 4, 8 + 2, (width + 4) * (tp * 2 - 1), 8 + 1);
        trackedProgress.put(entity.getUuid(), tp);
        Atomic.client.textRenderer.draw(matrices, t, -Atomic.client.textRenderer.getWidth(t) / 2f, f - 0.75f, 0xFFFFFF);
        matrices.pop();
        RenderSystem.disableDepthTest();
        RenderSystem.enableBlend();
        RenderSystem.polygonOffset(1, 15000000);
        RenderSystem.disablePolygonOffset();
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
        return null;
    }

    @Override
    public void onWorldRender(MatrixStack matrices) {

    }

    @Override
    public void onHudRender() {

    }
}

