package me.zeroX150.atomic.feature.gui.clickgui;

import me.zeroX150.atomic.feature.module.config.DynamicValue;
import me.zeroX150.atomic.helper.TypeConverter;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class Textbox extends TextFieldWidget {
    public DynamicValue<?> conf;

    public Textbox(TextRenderer textRenderer, int x, int y, int width, Text text, DynamicValue<?> confMod) {
        super(textRenderer, x + 3, y, width, 12, text);
        this.conf = confMod;
        this.height = 11;
        this.setDrawsBackground(false);
        this.setEditableColor(0xFFFFFF);
        this.setText(conf.getValue() + "");
        this.setTextPredicate(s -> {
            if (s.isEmpty()) return true;
            Object t = TypeConverter.convert(s, conf.getType());
            if (t == null) return false;
            else {
                conf.setValue(t);
                return true;
            }
        });
    }

    @Override
    public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        fill(matrices, x - 1, y - 1, x + width + 1, y + 11, this.isFocused() ? ClickGUI.ACTIVE.getRGB() : ClickGUI.INACTIVE.getRGB());
        super.renderButton(matrices, mouseX, mouseY, delta);
    }
}
