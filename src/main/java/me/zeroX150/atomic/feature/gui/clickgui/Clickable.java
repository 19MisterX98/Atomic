package me.zeroX150.atomic.feature.gui.clickgui;

import me.zeroX150.atomic.Atomic;
import me.zeroX150.atomic.feature.module.Module;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;

public class Clickable {
    Module parent;
    double margin = 4;
    double width = 100;
    double animProg = 0;
    double animProg1 = 0;

    public Clickable(Module parent) {
        this.parent = parent;
    }

    public void render(double x, double y, MatrixStack stack, double animProgress, double actualX, double actualY, float delta) {
        //Atomic.client.textRenderer.draw(new MatrixStack(),parent.getName(),(float)x,(float)y,0xFFFFFF);
        if (isHovered(actualX, actualY)) {
            ClickGUI.INSTANCE.renderDescription(parent.getDescription());
        }
        if (isHovered(actualX, actualY)) {
            animProg += 0.03 * (delta + 0.5);
        } else animProg -= 0.03 * (delta + 0.5);
        if (parent.isEnabled()) {
            animProg1 += 0.03 * (delta + 0.5);
        } else animProg1 -= 0.03 * (delta + 0.5);
        animProg1 = MathHelper.clamp(animProg1, 0, 1);
        animProg = MathHelper.clamp(animProg, 0, 1);
        double animProg1Inter = easeOutBounce(animProg1);
        double animProgInter = easeOutBounce(animProg);
        DrawableHelper.fill(stack, (int) (x - margin), (int) Math.floor(y - margin), (int) (x + width + margin), (int) Math.floor(y + (margin + 9) * animProgress), ClickGUI.INACTIVE.getRGB());
        DrawableHelper.fill(stack, (int) (x - margin), (int) Math.floor(y - margin), (int) (x - margin + (width + margin * 2) * animProgInter), (int) Math.floor(y + (margin + 9) * animProgress), ClickGUI.ACTIVE.getRGB());
        DrawableHelper.fill(stack, (int) (x - margin), (int) Math.floor(y - margin), (int) (x - margin + 1), (int) Math.floor(y - margin + ((margin * 2 + 9) * animProg1Inter) * animProgress), ClickGUI.L_HIGHLIGHT.getRGB());
        Atomic.fontRenderer.drawCenteredString(stack, parent.getName(), (float) (x + (width / 2f)), (float) y, 0xFFFFFF);
        //DrawableHelper.drawCenteredText(stack, Atomic.client.textRenderer, parent.getName(), (int) (x + (width / 2)), (int) y, 0xFFFFFF);
    }

    double easeOutBounce(double x) {
        return x < 0.5 ? 16 * x * x * x * x * x : 1 - Math.pow(-2 * x + 2, 5) / 2;
    }

    public void clicked(boolean isLeft) {
        if (isLeft) parent.toggle();
        else if (parent.config.getAll().size() != 0) ClickGUI.INSTANCE.showModuleConfig(parent);
    }

    boolean isHovered(double x, double y) {
        double mx = Atomic.client.mouse.getX() / 2;
        double my = Atomic.client.mouse.getY() / 2;
        return mx < x + width + margin && mx > x - margin && my < y + 9 + margin && my > y - margin;
    }
}
