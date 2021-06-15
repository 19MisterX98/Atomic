package me.zeroX150.atomic.feature.gui.clickgui;

import me.zeroX150.atomic.Atomic;
import me.zeroX150.atomic.feature.module.Module;
import me.zeroX150.atomic.feature.module.config.BooleanValue;
import me.zeroX150.atomic.feature.module.config.DynamicValue;
import me.zeroX150.atomic.feature.module.config.MultiValue;
import me.zeroX150.atomic.feature.module.config.SliderValue;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.awt.*;
import java.util.List;
import java.util.*;

public class ConfigWidget {
    double margin = 4;
    double posX;
    double posY;
    double lastRenderX;
    double lastRenderY;
    double width = 200;
    boolean dragged = false;
    Module parent;
    Map<DynamicValue<?>, ClickableWidget> children = new LinkedHashMap<>();
    //List<ClickableWidget> children = new ArrayList<>();

    public ConfigWidget(Module p) {
        this.posX = 1;
        this.posY = 1;
        this.lastRenderX = 1;
        this.lastRenderY = 1;
        this.parent = p;
        int yOffset = (int) Math.ceil(9 + (margin * 2));
        List<DynamicValue<?>> v = parent.config.getAll();
        if(parent.config.organizeClickGUIList) {
            v.sort(Comparator.comparingInt(value -> value.getKey().equalsIgnoreCase("keybind") ? -1 : Atomic.client.textRenderer.getWidth(value.getKey())));
        }

        for (DynamicValue<?> dynamicValue : v) {
            if (dynamicValue.getKey().equalsIgnoreCase("Keybind")) {
                KeyListenerBtn t = new KeyListenerBtn(1, yOffset, 100, parent);
                children.put(dynamicValue, t);
            } else if (dynamicValue instanceof BooleanValue) {
                Toggleable t = new Toggleable(1, yOffset, 100, (BooleanValue) dynamicValue);
                children.put(dynamicValue, t);
            } else if (dynamicValue instanceof SliderValue) {
                Slider t = new Slider(1, yOffset, 99, (SliderValue) dynamicValue);
                children.put(dynamicValue, t);
            } else if (dynamicValue instanceof MultiValue) {
                ButtonMultiSelectable btn = new ButtonMultiSelectable(1, yOffset, 100, (MultiValue) dynamicValue);
                children.put(dynamicValue, btn);
            } else {
                Textbox t = new Textbox(Atomic.client.textRenderer, 1, yOffset, 98, Text.of(dynamicValue.getKey()), dynamicValue);
                children.put(dynamicValue, t);
            }
            yOffset += 12;
        }
    }

    public void render(int mx, int my, float delta) {
        double xDiff = posX - lastRenderX;
        double yDiff = posY - lastRenderY;
        lastRenderX += (xDiff / me.zeroX150.atomic.feature.module.impl.render.ClickGUI.smooth.getValue());
        lastRenderY += (yDiff / me.zeroX150.atomic.feature.module.impl.render.ClickGUI.smooth.getValue());

        DrawableHelper.fill(new MatrixStack(), (int) (lastRenderX - margin), (int) (lastRenderY - margin), (int) (lastRenderX + width + margin), (int) (lastRenderY + 9 + margin), ClickGUI.HEADER_EXP.getRGB());
        DrawableHelper.fill(new MatrixStack(), (int) (lastRenderX + width + 1), (int) (lastRenderY - margin + 1), (int) (lastRenderX + width + margin - 1), (int) (lastRenderY + 9 + margin - 1), new Color(238, 37, 37, 255).getRGB());
        int maxOffset = (int) Math.ceil(9 + (margin * 2)) + (12 * (int) children.keySet().stream().filter(DynamicValue::shouldShow).count());
        DrawableHelper.fill(new MatrixStack(), (int) lastRenderX, (int) (lastRenderY + 9 + margin), (int) (lastRenderX + width), (int) (lastRenderY + maxOffset + 1), ClickGUI.HEADER_RET.getRGB());
        DrawableHelper.drawCenteredText(new MatrixStack(), Atomic.client.textRenderer, parent.getName() + " config", (int) (lastRenderX + (width / 2)), (int) (lastRenderY + 1), 0xFFFFFF);
        int yOffset = (int) Math.ceil(9 + (margin * 2)) - 1;
        List<DynamicValue<?>> dvL = new ArrayList<>(children.keySet());
        for (DynamicValue<?> child1 : dvL) {
            if (!child1.shouldShow()) continue;
            ClickableWidget child = children.get(child1);
            if (!(child instanceof Textbox)) {
                child.x = (int) (lastRenderX + width - child.getWidth() - 2);
                child.y = (int) lastRenderY + yOffset;
            } else {
                child.x = (int) (lastRenderX + width - child.getWidth() - 3);
                child.y = (int) lastRenderY + yOffset + 1;
            }
            DrawableHelper.drawCenteredText(new MatrixStack(), Atomic.client.textRenderer, child1.getKey(), (int) (lastRenderX + (width / 4)), (int) lastRenderY + yOffset + 1, 0xFFFFFF);
            child.render(new MatrixStack(), mx, my, delta);
            yOffset += 12;
        }
    }

    public void mouseClicked(double mouseX, double mouseY, int button) {
        if (lastRenderX + width > mouseX && lastRenderX - margin < mouseX && lastRenderY + 9 + margin > mouseY && lastRenderY - margin < mouseY) {
            this.dragged = true;
            return;
        } else if (lastRenderX + width + margin > mouseX && lastRenderX + width < mouseX && lastRenderY + 9 + margin > mouseY && lastRenderY - margin < mouseY) {
            ClickGUI.INSTANCE.showModuleConfig(null);
            return;
        }
        for (ClickableWidget child : children.values()) {
            child.mouseClicked(mouseX, mouseY, button);
        }
    }

    public void mouseReleased(double mx, double my, int b) {
        this.dragged = false;
        for (ClickableWidget child : children.values()) {
            child.mouseReleased(mx, my, b);
        }
    }

    public void charTyped(char c, int mod) {
        for (ClickableWidget child : children.values()) {
            child.charTyped(c, mod);
        }
    }

    public void mouseMoved(double x, double y) {
        for (ClickableWidget child : children.values()) {
            child.mouseMoved(x, y);
        }
    }

    public void keyPressed(int kc, int sc, int m) {
        for (ClickableWidget child : children.values()) {
            child.keyPressed(kc, sc, m);
        }
    }

    public void keyReleased(int kc, int sc, int m) {
        for (ClickableWidget child : children.values()) {
            child.keyReleased(kc, sc, m);
        }
    }

    public void mouseDragged(double mx, double my, int button, double dx, double dy) {
        for (ClickableWidget value : children.values()) {
            value.mouseDragged(mx, my, button, dx, dy);
        }
        if (this.dragged) {
            this.posX += dx;
            this.posY += dy;
        }
    }


}
