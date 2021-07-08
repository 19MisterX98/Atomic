package me.zeroX150.atomic.feature.gui.widget;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class PasswordFieldWidget extends TextFieldWidget {

    private boolean showText;

    public PasswordFieldWidget(TextRenderer textRenderer, int x, int y, int width, int height, Text text) {
        super(textRenderer, x, y, width, height, text);
    }

    public boolean isShowText() {
        return showText;
    }

    public void setShowText(boolean showText) {
        this.showText = showText;
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        String text = this.getText();
        if (this.showText) {
            this.setText("x".repeat(text.length()));
        }
        super.render(matrices, mouseX, mouseY, delta);
        if (this.showText) {
            this.setText(text);
        }
    }
}
