package me.zeroX150.atomic.mixin.game.render;

import me.zeroX150.atomic.Atomic;
import me.zeroX150.atomic.helper.Renderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.CyclingButtonWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;

@Mixin(ClickableWidget.class)
public abstract class ButtonWidgetMixin {

    @Shadow
    public int x;
    @Shadow
    public int y;
    @Shadow
    public boolean active;
    @Shadow
    protected int width;
    @Shadow
    protected int height;
    Color unselectedColor = new Color(25, 44, 49, 50);
    Color disabledColor = new Color(0, 0, 0, 70);
    long lastCache = 0;
    double timer = 0;

    @Shadow
    public abstract boolean isHovered();

    @Shadow
    public abstract Text getMessage();

    @Inject(method = "renderButton", at = @At("HEAD"), cancellable = true)
    public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        int dxStart, dyStart, dWidth, dHeight;

        if ((((Object) this) instanceof ButtonWidget) || (((Object) this) instanceof CyclingButtonWidget<?>)) {
            dxStart = x;
            dyStart = y;
            dWidth = width;
            dHeight = height;
        } else if (((Object) this) instanceof SliderWidget inst) {
            ISliderWidgetAccessor accessor = (ISliderWidgetAccessor) inst;
            double prog = accessor.getValue();
            dHeight = 20;
            dWidth = 4;
            dxStart = (int) (x + (prog * (width - 4))); // wtf why
            dyStart = y;
        } else return;
        ci.cancel();
        long current = System.currentTimeMillis();
        if (current - lastCache > 3) {
            double cvalA = (double) (current - lastCache) / 400.0;
            double cval = this.isHovered() ? cvalA : -cvalA;
            timer += cval;
            lastCache = current;
        }
        timer = MathHelper.clamp(timer, 0, 1);
        timer = this.active ? timer : 0;
        double a = easeInOutQuart(timer);
        boolean b = ((Object) this) instanceof SliderWidget;
        if (!b) {
            Renderer.fill(matrices, new Color(0, 194, 111, 255), x, y, x + (a + 1), y + height);
        } else {
            DrawableHelper.fill(matrices, dxStart, dyStart, dxStart + dWidth, dyStart + dHeight, new Color(0, 194, 111, 255).getRGB());
        }
        DrawableHelper.fill(matrices, x, y, x + width, y + height, this.active ? Renderer.lerp(new Color(38, 83, 92, 70), unselectedColor, a).getRGB() : disabledColor.getRGB());
        DrawableHelper.drawCenteredText(matrices, Atomic.client.textRenderer, this.getMessage(), this.x + this.width / 2,
                this.y + (this.height - 8) / 2, Color.white.getRGB());
    }

    double easeInOutQuart(double x) {
        return x < 0.5 ? 4 * x * x * x : 1 - Math.pow(-2 * x + 2, 3) / 2;
    }

}
