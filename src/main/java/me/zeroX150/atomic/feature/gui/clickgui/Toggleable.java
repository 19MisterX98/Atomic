package me.zeroX150.atomic.feature.gui.clickgui;

import me.zeroX150.atomic.Atomic;
import me.zeroX150.atomic.feature.module.config.BooleanValue;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class Toggleable extends ButtonWidget {
    BooleanValue parent;

    public Toggleable(int x, int y, int width, BooleanValue parent) {
        super(x, y, width, 12, Text.of(parent.getValue() ? "Enabled" : "Disabled"), (buttonWidget) -> {
        });
        this.parent = parent;
    }

    @Override
    public void onPress() {
        parent.setValue(!parent.getValue());
    }

    @Override
    public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.setMessage(Text.of(parent.getValue() ? "Enabled" : "Disabled"));
        fill(matrices, x, y, x + width, y + height, this.parent.getValue() ? ClickGUI.ACTIVE.getRGB() : ClickGUI.INACTIVE.getRGB());
        DrawableHelper.drawCenteredText(matrices, Atomic.client.textRenderer, this.getMessage(), x + (width / 2), y + (height / 2 - 9 / 2), 0xFFFFFF);
    }
}
