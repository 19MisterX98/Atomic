package me.zeroX150.atomic.feature.module.impl.render;

import com.mojang.blaze3d.systems.RenderSystem;
import me.zeroX150.atomic.Atomic;
import me.zeroX150.atomic.feature.gui.clickgui.Themes;
import me.zeroX150.atomic.feature.module.Module;
import me.zeroX150.atomic.feature.module.ModuleType;
import me.zeroX150.atomic.helper.Client;
import me.zeroX150.atomic.helper.Renderer;
import me.zeroX150.atomic.helper.Transitions;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;

import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class TargetHud extends Module {
    double wX = 0;
    double renderWX = 0;
    Entity e = null;
    Entity re = null;
    String valid = "abcdefghijklmnopqrstuvwxyz0123456789_";

    public TargetHud() {
        super("TargetHud", "the bruh", ModuleType.RENDER);
    }

    boolean isApplicable(Entity check) {
        if (check == Atomic.client.player) return false;
        if (check.distanceTo(Atomic.client.player) > 64) return false;
        int l = check.getEntityName().length();
        if(l < 3 || l > 16) return false;
        boolean isValidEntityName = true;
        for (char c : check.getEntityName().toLowerCase().toCharArray()) {
            if (!valid.contains(c+"")) {
                isValidEntityName = false;
                break;
            }
        }
        if (!isValidEntityName) return false;
        return check instanceof PlayerEntity;
    }

    @Override
    public void tick() {
        List<Entity> entitiesQueue = StreamSupport.stream(Atomic.client.world.getEntities().spliterator(), false).filter(this::isApplicable).sorted(Comparator.comparingDouble(value -> value.getPos().distanceTo(Atomic.client.player.getPos()))).collect(Collectors.toList());
        if (entitiesQueue.size() > 0) {
            e = entitiesQueue.get(0);
        } else e = null;
        if (e instanceof LivingEntity ev) {
            if (ev.isDead()) e = null;
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
    }

    @Override
    public void onHudRender() {
        renderWX = Transitions.transition(renderWX, wX, 20);
        //System.out.println(renderWX);
        int w = Atomic.client.getWindow().getScaledWidth();
        int h = Atomic.client.getWindow().getHeight();
        double modalHeight = 55;
        double modalWidth = 130;
        if (e != null) {
            wX = w / 2d + 10;
            re = e;
        } else wX = w / 2d - modalWidth - 1;
        if (renderWX < w / 2d - modalWidth) re = null;
        if (re != null) {
            if (!(re instanceof LivingEntity entity)) return;
            RenderSystem.enableScissor(w, 0, w, h); // cut off entire left part of screen
            MatrixStack stack = new MatrixStack();
            stack.translate(renderWX, h / 4d - modalHeight / 2d, 0);
            Renderer.fill(stack, Themes.Theme.ATOMIC.getPalette().active(), 0, 0, modalWidth, modalHeight);
            double r = Math.toRadians((System.currentTimeMillis() % 5000) / 5000d * 360d);
            double mxOffset = Math.sin(r) * 20;
            double myOffset = Math.cos(r) * 20;
            int hh = (int) (modalHeight / 2.3 / 2.3);
            double hp = entity.getHealth();
            hp = MathHelper.clamp(hp, 0, entity.getMaxHealth());
            hp /= entity.getMaxHealth();
            String t = "";
            if (hp > 0.75) t += "§a";
            else if (hp > 0.5) t += "§e";
            else if (hp > 0.25) t += "§c";
            else t += "§4";
            t += Client.roundToN(entity.getHealth(), 1);
            String n = re.getEntityName();
            float wm = Atomic.monoFontRenderer.getStringWidth(n)+1;
            Atomic.monoFontRenderer.drawString(stack,n,modalWidth-wm,1,0xFFFFFF);
            //Atomic.monoFontRenderer.drawCenteredString(stack, re.getEntityName(), modalWidth / 2f, 1, 0xFFFFFF);
            Atomic.fontRenderer.drawString(stack, "Health: " + t + "§r / " + Client.roundToN(entity.getMaxHealth(), 1), hh * 2.5 + 6, 10, 0xFFFFFF);
            Atomic.fontRenderer.drawString(stack, "Distance: " + Client.roundToN(entity.getPos().distanceTo(Atomic.client.player.getPos()), 1), hh * 2.5 + 6, 19, 0xFFFFFF);
            String tv;
            if(entity.getHealth() > Atomic.client.player.getHealth()) tv = "§cThreat";
            else if (entity.getHealth() == Atomic.client.player.getHealth()) tv = "Neutral";
            else tv = "§aNo threat";
            Atomic.fontRenderer.drawString(stack, tv, hh * 2.5 + 6, 28, 0xFFFFFF);
            PlayerListEntry ple = Atomic.client.getNetworkHandler().getPlayerListEntry(entity.getUuid());
            if (ple != null) {
                int ping = ple.getLatency();
                Atomic.fontRenderer.drawString(stack, ping + " ms ping", hh * 2.5 + 6, 37, 0xFFFFFF);
            }
            //Atomic.fontRenderer.drawString(stack,"The sus",hh*2.5+6,10,0xFFFFFF);

            Text cname = re.getCustomName();
            re.setCustomName(Text.of("DoNotRenderThisUsernamePlease"));
            InventoryScreen.drawEntity((int) (renderWX + hh + 5), (int) (h / 4d - modalHeight / 2d + modalHeight - 3), (int) (hh * 2.5), (float) mxOffset, (float) myOffset, (LivingEntity) re);
            re.setCustomName(cname);
        }

        RenderSystem.disableScissor();
    }
}

