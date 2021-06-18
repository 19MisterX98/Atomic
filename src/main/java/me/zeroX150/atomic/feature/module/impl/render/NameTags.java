package me.zeroX150.atomic.feature.module.impl.render;

import com.mojang.blaze3d.platform.GlStateManager;
import me.zeroX150.atomic.Atomic;
import me.zeroX150.atomic.feature.module.Module;
import me.zeroX150.atomic.feature.module.ModuleType;
import me.zeroX150.atomic.helper.Renderer;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class NameTags extends Module {
    Map<UUID, Double> trackedProgress = new HashMap<>();

    public NameTags() {
        super("NameTags", "shows better-ish name tags", ModuleType.RENDER);
    }

    public void renderTag(Entity entity, Text text, MatrixStack matrices, VertexConsumerProvider provider, int l, EntityRenderDispatcher dispatcher) {
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
        matrices.translate(0, f + (scale * 6), 0);
        matrices.multiply(dispatcher.getRotation());
        matrices.scale(-scale, -scale, scale);
        TextRenderer tr = Atomic.client.textRenderer;
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
        int width = tr.getWidth(t) / 2;
        double tp = trackedProgress.get(entity.getUuid());
        double trackDiff = hp - tp;
        trackDiff /= 10d;
        tp += trackDiff;
        GlStateManager._enablePolygonOffset();
        GlStateManager._polygonOffset(1f, -1500000);
        Renderer.fill(matrices, new Color(20, 20, 20, 220), -width - 4, tr.fontHeight + 2, width + 4, 1);
        //DrawableHelper.fill(matrices,-width-4,tr.fontHeight+2,width+4,1,new Color(20, 20, 20, 220).getRGB());
        Renderer.fill(matrices, new Color(0, 194, 111, 255), -width - 4, tr.fontHeight + 2, (width + 4) * (tp * 2 - 1), tr.fontHeight + 1);
        trackedProgress.put(entity.getUuid(), tp);
        //DrawableHelper.fill(matrices,-width-4,tr.fontHeight+2,(int)((width+4)*(trackedProgress*2-1)),tr.fontHeight+1,new Color(0, 194, 111, 255).getRGB());
        tr.draw(matrices, t, -tr.getWidth(t) / 2f, f, 0xFFFFFF);
        GlStateManager._disablePolygonOffset();
        GlStateManager._polygonOffset(1f, 1500000);
        matrices.pop();
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

