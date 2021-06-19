package me.zeroX150.atomic.mixin.game.render;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.EntryListWidget;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntryListWidget.class)
public class EntryListWidgetMixin {

    @Shadow
    protected int width;
    @Shadow
    protected int height;
    @Shadow
    private boolean renderBackground;
    @Shadow
    private boolean renderHorizontalShadows;

    @Inject(method = "render", at = @At("HEAD"))
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        this.renderBackground = false;
        this.renderHorizontalShadows = false;
        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.setShaderTexture(0, DrawableHelper.OPTIONS_BACKGROUND_TEXTURE);
        Screen.drawTexture(new MatrixStack(), 0, 0, 0, 0, width, height, width, height);
    }
}
