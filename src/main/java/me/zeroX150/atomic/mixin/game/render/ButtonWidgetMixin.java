package me.zeroX150.atomic.mixin.game.render;

import me.zeroX150.atomic.Atomic;
import me.zeroX150.atomic.helper.Renderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.awt.*;

@Mixin(ClickableWidget.class)
public abstract class ButtonWidgetMixin {

    @Shadow
    public int x;
    @Shadow
    public int y;
    @Shadow
    protected int width;
    @Shadow
    protected int height;
    double animProg = 0;
    @Shadow
    private Text message;

    @Shadow
    public abstract boolean isHovered();

    /**
     * @author 0x150
     * @reason yea
     */
    @Overwrite
    public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        double a = this.isHovered() ? 0.06 : -0.06;
        animProg += a;
        animProg = MathHelper.clamp(animProg, 0, 1);
        DrawableHelper.fill(matrices, x, y, x + width, y + height, Renderer.lerp(new Color(32, 32, 32, 190), new Color(22, 22, 22, 120), animProg).getRGB());
        DrawableHelper.drawCenteredText(matrices, Atomic.client.textRenderer, this.message, x + width / 2, y + height / 2 - 9 / 2, 0xFFFFFF);
    }

}
