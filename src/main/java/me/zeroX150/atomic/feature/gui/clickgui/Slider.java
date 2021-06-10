package me.zeroX150.atomic.feature.gui.clickgui;

import me.zeroX150.atomic.Atomic;
import me.zeroX150.atomic.feature.module.config.SliderValue;
import me.zeroX150.atomic.helper.Client;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;

public class Slider extends ClickableWidget {
    double min;
    double max;
    double value;
    boolean dragged = false;
    SliderValue v;
    int prec;

    public Slider(int x, int y, int width, SliderValue conf) {
        super(x - 1, y - 1, width + 1, 12, Text.of(conf.getKey()));
        this.min = conf.getMin();
        this.max = conf.getMax();
        this.v = conf;
        this.value = conf.getValue();
        this.prec = conf.getPrec();
    }

    @Override
    public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        fill(matrices, x, y, x + width, y + height, ClickGUI.INACTIVE.getRGB());
        fill(matrices, x, y, (int) (x + (width * getValue())), y + height, ClickGUI.ACTIVE.getRGB());
        drawCenteredText(matrices, Atomic.client.textRenderer, Client.roundToN(value, prec) + "", x + (width / 2), y + (height / 2 - (9 / 2)), 0xFFFFFF);
    }

    double getValue() {
        return (value - min) / (max - min);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (isHovered()) dragged = true;
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        dragged = false;
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (dragged) {
            double mxTranslated = mouseX - x;
            double perIn = MathHelper.clamp(mxTranslated / width, 0, 1);
            this.value = Client.roundToN(perIn * (max - min) + min, prec);
            v.setValue(this.value);
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public void appendNarrations(NarrationMessageBuilder builder) {

    }
}
