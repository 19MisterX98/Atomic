package me.zeroX150.atomic.feature.gui.screen;

import me.zeroX150.atomic.feature.gui.widget.ConfTextFieldWidget;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class TestScreen extends Screen {
    ConfTextFieldWidget widget;

    public TestScreen() {
        super(Text.of(""));
    }

    @Override
    protected void init() {
        widget = new ConfTextFieldWidget(width / 2, height / 2, 100, 12, Text.of("amogus"));
        addDrawableChild(widget);
        super.init();
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        renderBackground(matrices);

        super.render(matrices, mouseX, mouseY, delta);
    }
}
