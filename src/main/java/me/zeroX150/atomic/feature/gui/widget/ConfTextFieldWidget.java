package me.zeroX150.atomic.feature.gui.widget;

import com.google.common.collect.Lists;
import me.zeroX150.atomic.Atomic;
import me.zeroX150.atomic.feature.gui.clickgui.ClickGUI;
import me.zeroX150.atomic.helper.Renderer;
import net.minecraft.SharedConstants;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ConfTextFieldWidget extends ClickableWidget implements Drawable, Element {

    boolean selected = false;
    String text = "";
    int cursorIndex = 0;
    int rStartIndex = 0;

    public ConfTextFieldWidget(int x, int y, int width, int height, Text message) {
        super(x, y, width, height, message);
    }

    @Override
    public void appendNarrations(NarrationMessageBuilder builder) {

    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        selected = mouseX > x && mouseX < x + width && mouseY > y && mouseY < y + height;
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (!selected) return false;
        if (keyCode == 259) { // backspace
            List<Character> v = new ArrayList<>(Lists.charactersOf(text));
            if (v.size() > 0 && cursorIndex != 0) v.remove(cursorIndex - 1);
            StringBuilder sb = new StringBuilder();
            for (Character character : v) {
                sb.append(character);
            }
            text = sb.toString();
            cursorIndex--;
        } else if (keyCode == 262) { // arrow right
            cursorIndex++;
        } else if (keyCode == 263) { // arrow left
            cursorIndex--;
        }
        return false;
    }

    @Override
    public boolean charTyped(char chr, int modifiers) {
        if (selected && SharedConstants.isValidChar(chr)) {
            List<Character> v = new ArrayList<>(Lists.charactersOf(text));
            v.add(cursorIndex, chr);
            StringBuilder sb = new StringBuilder();
            for (Character character : v) {
                sb.append(character);
            }
            text = sb.toString();
            cursorIndex++;
            event_onTextChange();
        }
        return false;
    }

    @Override
    public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        cursorIndex = MathHelper.clamp(cursorIndex, 0, text.isEmpty() ? 0 : (text.length()));
        Renderer.fill(matrices, ClickGUI.currentActiveTheme.inactive(), x, y, x + width, y + height);
        while (rStartIndex > cursorIndex) {
            rStartIndex--;
        }
        if (rStartIndex > text.length()) rStartIndex = text.length();
        if (rStartIndex < 0) rStartIndex = 0;
        String v1 = text.substring(rStartIndex);
        while (Atomic.monoFontRenderer.getStringWidth(v1) > width) {
            if (v1.isEmpty()) break;
            v1 = v1.substring(0, v1.length() - 1);
        }
        Atomic.monoFontRenderer.drawString(matrices, v1, x + 1, y + (height / 2f) - (8 / 2f), 0xFFFFFF);
        float w = text.isEmpty() ? 0 : Atomic.monoFontRenderer.getStringWidth(text.substring(rStartIndex, cursorIndex));

        float v = (System.currentTimeMillis() % 1000) / 1000f;
        double opacity = Math.sin(v * Math.PI);
        if (w > width) {
            w = width - 2;
            rStartIndex++;
        }
        if (selected) {
            Renderer.fill(new Color(255, 255, 255, (int) Math.floor(opacity * 255)), x + w + 1, y + 1, x + w + 2, y + height - 1);
        }
    }

    public String getText() {
        return text;
    }

    public void event_onTextChange() {

    }
}
