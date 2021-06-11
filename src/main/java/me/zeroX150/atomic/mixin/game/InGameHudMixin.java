package me.zeroX150.atomic.mixin.game;

import com.mojang.blaze3d.systems.RenderSystem;
import me.zeroX150.atomic.Atomic;
import me.zeroX150.atomic.feature.module.Module;
import me.zeroX150.atomic.feature.module.ModuleRegistry;
import me.zeroX150.atomic.feature.module.impl.render.Hud;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.option.AttackIndicator;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin extends DrawableHelper {

    @Shadow private int scaledHeight;

    @Shadow private int scaledWidth;

    @Inject(method = "render", at = @At("RETURN"))
    public void render(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        for (Module module : ModuleRegistry.getModules()) {
            if (module.isEnabled()) module.onHudRender();
        }
    }
    double interpolatedSlotValue = 0;

    @Inject(method="renderHotbar",at=@At("HEAD"))
    public void renderHotbarBackground(float tickDelta, MatrixStack matrices, CallbackInfo ci) {
        Hud c = (Hud) ModuleRegistry.getByClass(Hud.class);
        if (!c.isEnabled()) return;
        int i = this.scaledWidth/2;
        double slotDiff = Atomic.client.player.getInventory().selectedSlot-interpolatedSlotValue;
        slotDiff = Math.abs(slotDiff)<0.07?(slotDiff*c.smoothSelectTransition.getValue()):slotDiff;
        interpolatedSlotValue += (slotDiff/c.smoothSelectTransition.getValue());
        DrawableHelper.fill(matrices,0, Atomic.client.getWindow().getScaledHeight()-23,Atomic.client.getWindow().getScaledWidth(),Atomic.client.getWindow().getScaledHeight(), new Color(28, 28, 28, 255).getRGB());
        DrawableHelper.fill(matrices, (int) (i-91-1+interpolatedSlotValue*20),this.scaledHeight-23, (int) ((i-91-1+interpolatedSlotValue*20)+24),(this.scaledHeight),new Color(28, 28, 28, 255).brighter().getRGB());
    }

    @Redirect(method = "renderHotbar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;drawTexture(Lnet/minecraft/client/util/math/MatrixStack;IIIIII)V", ordinal = 0))
    private void blockCall1(InGameHud inGameHud, MatrixStack matrices, int x, int y, int u, int v, int width, int height) {
        if(!ModuleRegistry.getByClass(Hud.class).isEnabled()) drawTexture(matrices, x, y, this.getZOffset(), (float)u, (float)v, width, height, 256, 256);
    }

    @Redirect(method = "renderHotbar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;drawTexture(Lnet/minecraft/client/util/math/MatrixStack;IIIIII)V", ordinal = 1))
    private void blockCall2(InGameHud inGameHud, MatrixStack matrices, int x, int y, int u, int v, int width, int height) {
        if(!ModuleRegistry.getByClass(Hud.class).isEnabled()) drawTexture(matrices, x, y, this.getZOffset(), (float)u, (float)v, width, height, 256, 256);
    }
}
