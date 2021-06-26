package me.zeroX150.atomic.mixin.game.render;

import me.zeroX150.atomic.helper.Renderer;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;

@Mixin(TextFieldWidget.class)
public abstract class TextFieldWidgetMixin extends ClickableWidget {
    boolean isSpecial = false;
    @Shadow
    private boolean drawsBackground;

    public TextFieldWidgetMixin(int x, int y, int width, int height, Text message) {
        super(x, y, width, height, message);
    }

    @Shadow
    public abstract void setDrawsBackground(boolean drawsBackground);

    @Shadow
    public abstract void setEditableColor(int color);

    @Shadow
    public abstract String getText();

    @Shadow
    public abstract void setText(String text);

    @Shadow
    public abstract void setSuggestion(@Nullable String suggestion);

    @Shadow
    protected abstract void drawSelectionHighlight(int x1, int y1, int x2, int y2);

    @Inject(method = "<init>(Lnet/minecraft/client/font/TextRenderer;IIIILnet/minecraft/text/Text;)V", at = @At("TAIL"))
    public void init(TextRenderer textRenderer, int x, int y, int width, int height, Text text, CallbackInfo ci) {
        if (text.asString().startsWith("SPECIAL:")) {
            this.setMessage(Text.of(text.asString().substring("SPECIAL:".length())));
            isSpecial = true;
            this.setDrawsBackground(false);
            this.setEditableColor(0xFFFFFF);

        }
    }

    @Inject(method = "renderButton", at = @At("HEAD"))
    public void renderButton1(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (!isSpecial) return;
        if (this.getText().isEmpty()) this.setSuggestion(this.getMessage().asString());
        else this.setSuggestion("");
        int w = this.isFocused() ? 2 : 1;
        Renderer.fill(matrices, new Color(20, 20, 20, 220), x, y, x + width, y + height);
        Renderer.fill(matrices, new Color(0, 194, 111, 255), x, y, x + w, y + height);
        matrices.translate(4, 1 + (height / 2d) - (9 / 2d), 0);
    }

    @Inject(method = "setDrawsBackground", at = @At("HEAD"), cancellable = true)
    public void sDB(boolean drawsBackground, CallbackInfo ci) {
        if (isSpecial) this.drawsBackground = false;
    }

    @Inject(method = "renderButton", at = @At("TAIL"))
    public void renderButton2(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (isSpecial) matrices.translate(-4, -(1 + (height / 2d) - (9 / 2d)), 0);
    }

    @Redirect(method = "renderButton", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/widget/TextFieldWidget;drawSelectionHighlight(IIII)V"))
    public void drawSelectionHighlightRedirect(TextFieldWidget textFieldWidget, int x1, int y1, int x2, int y2) {
        if (isSpecial)
            this.drawSelectionHighlight(x1 + 4, y1 + (height / 2) - (9 / 2), x2 + 4, y2 + (height / 2) - (9 / 2));
        else this.drawSelectionHighlight(x1, y1, x2, y2);
    }
}
