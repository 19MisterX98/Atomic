package me.zeroX150.atomic.feature.gui.clickgui;

import com.google.common.collect.Lists;
import me.zeroX150.atomic.Atomic;
import me.zeroX150.atomic.feature.module.Module;
import me.zeroX150.atomic.feature.module.config.*;
import me.zeroX150.atomic.helper.Renderer;
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
    Map<DynamicValue<?>, List<ClickableWidget>> children = new LinkedHashMap<>();

    public ConfigWidget(Module p) {
        this.posX = 1;
        this.posY = 1;
        this.lastRenderX = 1;
        this.lastRenderY = 1;
        this.parent = p;
        int yOffset = (int) Math.ceil(9 + (margin * 2));
        List<DynamicValue<?>> v = parent.config.getAll();
        if (parent.config.organizeClickGUIList) {
            v.sort(Comparator.comparingInt(value -> value.getKey().equalsIgnoreCase("keybind") ? -1 : Atomic.client.textRenderer.getWidth(value.getKey())));
        }

        for (DynamicValue<?> dynamicValue : v) {
            if (dynamicValue.getKey().equalsIgnoreCase("Keybind")) {
                KeyListenerBtn t = new KeyListenerBtn(1, yOffset, 100, parent);
                children.put(dynamicValue, Lists.asList(t, new ClickableWidget[0]));
            } else if (dynamicValue instanceof BooleanValue) {
                Toggleable t = new Toggleable(1, yOffset, 100, (BooleanValue) dynamicValue);
                children.put(dynamicValue, Lists.asList(t, new ClickableWidget[0]));
            } else if (dynamicValue instanceof SliderValue) {
                Slider t = new Slider(1, yOffset, 99, (SliderValue) dynamicValue);
                children.put(dynamicValue, Lists.asList(t, new ClickableWidget[0]));
            } else if (dynamicValue instanceof MultiValue) {
                ButtonMultiSelectable t = new ButtonMultiSelectable(1, yOffset, 100, (MultiValue) dynamicValue);
                children.put(dynamicValue, Lists.asList(t, new ClickableWidget[0]));
            } else if (dynamicValue instanceof ColorValue orig) {
                Slider red = new Slider(1, yOffset, 99, new SliderValue("0", orig.getColor().getRed(), 0, 255, 0) {
                    @Override
                    public void setValue(Object value) {
                        super.setValue(value);
                        Color v = orig.getColor();
                        orig.setValue(Renderer.modify(v, (int) Math.floor(this.getValue()), -1, -1, -1).getRGB() + (orig.isRGB() ? ";" : ""));
                    }
                }) {
                    @Override
                    public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
                        if (orig.isRGB()) {
                            this.value = orig.getColor().getRed();
                        }
                        super.renderButton(matrices, mouseX, mouseY, delta);
                    }
                };
                Slider green = new Slider(1, yOffset + 12, 99, new SliderValue("0", orig.getColor().getGreen(), 0, 255, 0) {
                    @Override
                    public void setValue(Object value) {
                        super.setValue(value);
                        Color v = orig.getColor();
                        orig.setValue(Renderer.modify(v, -1, (int) Math.floor(this.getValue()), -1, -1).getRGB() + (orig.isRGB() ? ";" : ""));
                    }
                }) {
                    @Override
                    public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
                        if (orig.isRGB()) {
                            this.value = orig.getColor().getGreen();
                        }
                        super.renderButton(matrices, mouseX, mouseY, delta);
                    }
                };
                Slider blue = new Slider(1, yOffset + 12 + 12, 99, new SliderValue("0", orig.getColor().getBlue(), 0, 255, 0) {
                    @Override
                    public void setValue(Object value) {
                        super.setValue(value);
                        Color v = orig.getColor();
                        orig.setValue(Renderer.modify(v, -1, -1, (int) Math.floor(this.getValue()), -1).getRGB() + (orig.isRGB() ? ";" : ""));
                    }
                }) {
                    @Override
                    public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
                        if (orig.isRGB()) {
                            this.value = orig.getColor().getBlue();
                        }
                        super.renderButton(matrices, mouseX, mouseY, delta);
                    }
                };
                Toggleable isRGB = new Toggleable(1, yOffset + 12 + 12 + 12 + 12, 100, new BooleanValue("0", orig.isRGB()) {
                    @Override
                    public void setValue(Object value) {
                        super.setValue(value);
                        orig.setRGB(this.value);
                        red.value = orig.getColor().getRed();
                        green.value = orig.getColor().getGreen();
                        blue.value = orig.getColor().getBlue();
                    }
                });
                children.put(dynamicValue, Lists.asList(red, new ClickableWidget[]{green, blue, isRGB}));

            } else {
                Textbox t = new Textbox(Atomic.client.textRenderer, 1, yOffset, 98, Text.of(dynamicValue.getKey()), dynamicValue);
                children.put(dynamicValue, Lists.asList(t, new ClickableWidget[0]));
            }
            yOffset += 12;
        }
    }

    public void render(int mx, int my, float delta) {
        double xDiff = posX - lastRenderX;
        double yDiff = posY - lastRenderY;
        lastRenderX += (xDiff / me.zeroX150.atomic.feature.module.impl.render.ClickGUI.smooth.getValue());
        lastRenderY += (yDiff / me.zeroX150.atomic.feature.module.impl.render.ClickGUI.smooth.getValue());
        if (lastRenderY > Atomic.client.getWindow().getScaledHeight() + 5) ClickGUI.INSTANCE.showModuleConfig(null);

        DrawableHelper.fill(new MatrixStack(), (int) (lastRenderX - margin), (int) (lastRenderY - margin), (int) (lastRenderX + width + margin), (int) (lastRenderY + 9 + margin), ClickGUI.HEADER_EXP.getRGB());
        DrawableHelper.fill(new MatrixStack(), (int) (lastRenderX + width + 1), (int) (lastRenderY - margin + 1), (int) (lastRenderX + width + margin - 1), (int) (lastRenderY + 9 + margin - 1), new Color(238, 37, 37, 255).getRGB());
        int maxOffset = (int) Math.ceil(9 + (margin * 2));
        for (DynamicValue<?> dynamicValue : children.keySet()) {
            if (!dynamicValue.shouldShow()) continue;
            List<ClickableWidget> w = children.get(dynamicValue);
            maxOffset += 12 * w.size();
        }
        DrawableHelper.fill(new MatrixStack(), (int) lastRenderX, (int) (lastRenderY + 9 + margin), (int) (lastRenderX + width), (int) (lastRenderY + maxOffset + 1), ClickGUI.HEADER_RET.getRGB());
        DrawableHelper.drawCenteredText(new MatrixStack(), Atomic.client.textRenderer, parent.getName() + " config", (int) (lastRenderX + (width / 2)), (int) (lastRenderY + 1), 0xFFFFFF);
        int yOffset = (int) Math.ceil(9 + (margin * 2)) - 1;
        List<DynamicValue<?>> dvL = new ArrayList<>(children.keySet());
        for (DynamicValue<?> child1 : dvL) {
            if (!child1.shouldShow()) continue;
            List<ClickableWidget> children = this.children.get(child1);
            int c = 0xFFFFFF;
            MatrixStack ms = new MatrixStack();
            if (child1 instanceof ColorValue a) {
                c = a.getColor().getRGB();
                DrawableHelper.drawCenteredText(ms, Atomic.client.textRenderer, child1.getKey() + " R", (int) (lastRenderX + (width / 4)), (int) lastRenderY + yOffset + 1, c);
                DrawableHelper.drawCenteredText(ms, Atomic.client.textRenderer, "G", (int) (lastRenderX + (width / 4)), (int) lastRenderY + yOffset + 13, c);
                DrawableHelper.drawCenteredText(ms, Atomic.client.textRenderer, "B", (int) (lastRenderX + (width / 4)), (int) lastRenderY + yOffset + 13 + 12, c);
                DrawableHelper.drawCenteredText(ms, Atomic.client.textRenderer, "Chroma", (int) (lastRenderX + (width / 4)), (int) lastRenderY + yOffset + 13 + 12 + 12, c);
            } else
                DrawableHelper.drawCenteredText(ms, Atomic.client.textRenderer, child1.getKey(), (int) (lastRenderX + (width / 4)), (int) lastRenderY + yOffset + 1, c);
            for (ClickableWidget child : children) {
                if (!(child instanceof Textbox)) {
                    child.x = (int) (lastRenderX + width - child.getWidth() - 2);
                    child.y = (int) lastRenderY + yOffset;
                } else {
                    child.x = (int) (lastRenderX + width - child.getWidth() - 3);
                    child.y = (int) lastRenderY + yOffset + 1;
                }
                child.render(new MatrixStack(), mx, my, delta);
                yOffset += 12;
            }
        }
    }

    public void mouseClicked(double mouseX, double mouseY, int button) {
        if (lastRenderX + width > mouseX && lastRenderX - margin < mouseX && lastRenderY + 9 + margin > mouseY && lastRenderY - margin < mouseY) {
            this.dragged = true;
            return;
        } else if (lastRenderX + width + margin > mouseX && lastRenderX + width < mouseX && lastRenderY + 9 + margin > mouseY && lastRenderY - margin < mouseY) {
            this.posY = Atomic.client.getWindow().getScaledHeight() + 30;
            this.posX = Atomic.client.getWindow().getScaledWidth() / 2d - (210 / 2d);
            return;
        }
        for (List<ClickableWidget> children : this.children.values()) {
            for (ClickableWidget child : children) {
                child.mouseClicked(mouseX, mouseY, button);
            }
        }
    }

    public void mouseReleased(double mx, double my, int b) {
        this.dragged = false;
        for (List<ClickableWidget> children : this.children.values()) {
            for (ClickableWidget child : children) {
                child.mouseReleased(mx, my, b);
            }
        }
    }

    public void charTyped(char c, int mod) {
        for (List<ClickableWidget> children : this.children.values()) {
            for (ClickableWidget child : children) {
                if (!(child instanceof Toggleable)) child.charTyped(c, mod);
            }
        }
    }

    public void mouseMoved(double x, double y) {
        for (List<ClickableWidget> children : this.children.values()) {
            for (ClickableWidget child : children) {
                child.mouseMoved(x, y);
            }
        }
    }

    public void keyPressed(int kc, int sc, int m) {
        for (List<ClickableWidget> children : this.children.values()) {
            for (ClickableWidget child : children) {
                if (!(child instanceof Toggleable)) child.keyPressed(kc, sc, m);
            }
        }
    }

    public void keyReleased(int kc, int sc, int m) {
        for (List<ClickableWidget> children : this.children.values()) {
            for (ClickableWidget child : children) {
                if (!(child instanceof Toggleable)) child.keyReleased(kc, sc, m);
            }
        }
    }

    public void mouseDragged(double mx, double my, int button, double dx, double dy) {
        for (List<ClickableWidget> children : this.children.values()) {
            for (ClickableWidget child : children) {
                child.mouseDragged(mx, my, button, dx, dy);
            }
        }
        if (this.dragged) {
            this.posX += dx;
            this.posY += dy;
        }
    }


}
