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

    public void render(double x, double y, MatrixStack stack, double animProgress) {
        //Atomic.client.textRenderer.draw(new MatrixStack(),parent.getName(),(float)x,(float)y,0xFFFFFF);
        DrawableHelper.fill(stack, (int) (x - margin), (int) Math.floor(y - margin), (int) (x + width + margin), (int) Math.floor(y + (margin + 9) * animProgress), parent.isEnabled() ? ClickGUI.ACTIVE.getRGB() : ClickGUI.INACTIVE.getRGB());
        DrawableHelper.drawCenteredText(stack, Atomic.client.textRenderer, parent.getName(), (int) (x + (width / 2)), (int) y, 0xFFFFFF);
    }

    public void clicked(boolean isLeft) {
        if (isLeft) parent.toggle();
        else if (parent.config.getAll().size() != 0) ClickGUI.INSTANCE.showModuleConfig(parent);
    }
}
