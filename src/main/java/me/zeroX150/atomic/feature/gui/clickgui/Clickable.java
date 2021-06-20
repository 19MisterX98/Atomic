package me.zeroX150.atomic.feature.gui.clickgui;

import me.zeroX150.atomic.Atomic;
import me.zeroX150.atomic.feature.module.Module;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;

public class Clickable {
    Module parent;
    double margin = 4;
    double width = 100;

    public Clickable(Module parent) {
        this.parent = parent;
    }

    public void render(double x, double y, MatrixStack stack, double animProgress, double actualX, double actualY) {
        //Atomic.client.textRenderer.draw(new MatrixStack(),parent.getName(),(float)x,(float)y,0xFFFFFF);
        if (isHovered(actualX, actualY)) {
            ClickGUI.INSTANCE.renderDescription(parent.getDescription());
        }
        DrawableHelper.fill(stack, (int) (x - margin), (int) Math.floor(y - margin), (int) (x + width + margin), (int) Math.floor(y + (margin + 9) * animProgress), parent.isEnabled() || isHovered(actualX, actualY) ? ClickGUI.ACTIVE.getRGB() : ClickGUI.INACTIVE.getRGB());
        DrawableHelper.drawCenteredText(stack, Atomic.client.textRenderer, parent.getName(), (int) (x + (width / 2)), (int) y, 0xFFFFFF);
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
