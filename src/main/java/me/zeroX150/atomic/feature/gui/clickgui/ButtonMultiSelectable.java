package me.zeroX150.atomic.feature.gui.clickgui;

import me.zeroX150.atomic.Atomic;
import me.zeroX150.atomic.feature.module.config.MultiValue;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class ButtonMultiSelectable extends ButtonWidget {
    MultiValue parent;
    int currentIndex;

    public ButtonMultiSelectable(int x, int y, int width, MultiValue parent) {
        super(x, y, width, 12, Text.of(parent.getValue()), button -> {
        });
        currentIndex = parent.getIndex();
        this.parent = parent;
    }

    @Override
    public void onPress() {
        parent.cycle();
        this.setMessage(Text.of(parent.getValue()));
    }

    @Override
    public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        fill(matrices, x, y, x + width, y + height, (this.isHovered() ? ClickGUI.currentActiveTheme.active() : ClickGUI.currentActiveTheme.inactive()).getRGB());
        Atomic.fontRenderer.drawCenteredString(matrices, this.getMessage().asString(), x + (width / 2f), y + (height / 4f - 1), ClickGUI.currentActiveTheme.fontColor().getRGB());
        //DrawableHelper.drawCenteredText(matrices, Atomic.client.textRenderer, this.getMessage(), x + (width / 2), y + (height / 2 - 9 / 2), 0xFFFFFF);
    }
}
