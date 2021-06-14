package me.zeroX150.atomic.feature.gui.clickgui;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import me.zeroX150.atomic.Atomic;
import me.zeroX150.atomic.feature.module.Module;
import me.zeroX150.atomic.feature.module.ModuleRegistry;
import me.zeroX150.atomic.feature.module.ModuleType;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ClickGUI extends Screen {
    public static ClickGUI INSTANCE;

    public static Color INACTIVE = new Color(17, 17, 17, 220);
    public static Color ACTIVE = new Color(40, 40, 40, 220);
    public static Color HEADER_RET = new Color(38, 38, 38, 255);
    public static Color HEADER_EXP = new Color(49, 49, 49, 255);

    double animProgress = 0;
    Identifier LOGO = new Identifier("atomic", "logo.png");

    ConfigWidget currentConfig = null;

    long lastRender = System.currentTimeMillis();

    List<Draggable> containers = new ArrayList<>();
    int currentSortMode = 0;

    public ClickGUI() {
        super(Text.of(""));
        double width = Atomic.client.getWindow().getScaledWidth();
        double height = Atomic.client.getWindow().getScaledHeight();
        INSTANCE = this;
        double cR = 0;
        double rotCircle = 360d / ModuleType.values().length;
        for (ModuleType value : ModuleType.values()) {
            double rot = Math.toRadians(cR);
            Draggable d = new Draggable(value.getName(), false);
            d.lastRenderX = width / 2d - (100 / 2d);
            d.lastRenderY = height / 2d - (9) / 2d;
            d.posX = d.lastRenderX + (Math.sin(rot) * 120);
            d.posY = d.lastRenderY + (Math.cos(rot) * 120);
            for (Module module : ModuleRegistry.getModules()) {
                if (module.getModuleType() == value) {
                    Clickable w = new Clickable(module);
                    d.addChild(w);
                }
            }
            containers.add(d);
            cR += rotCircle;
        }
    }

    @Override
    protected void init() {
        animProgress = 0;
        ButtonWidget sort = new ButtonWidget(width - 21, height - 21, 20, 20, Text.of("S"), button -> {
            currentSortMode++;
            currentSortMode %= 3;
            switch (currentSortMode) {
                case 0 -> {
                    double cR = 0;
                    double rotCircle = 360d / containers.size();
                    for (Draggable container : containers) {
                        container.expanded = false;
                        double rot = Math.toRadians(cR);
                        container.posX = width / 2d - (100 / 2d) + (Math.sin(rot) * 120);
                        container.posY = height / 2d - (9 / 2d) + (Math.cos(rot) * 120);
                        cR += rotCircle;
                    }
                }
                case 1 -> {
                    double offsetX = 20;
                    double offsetY = me.zeroX150.atomic.feature.module.impl.render.ClickGUI.logoSize.getValue() * 130 + 20;
                    for (Draggable container : containers) {
                        container.posX = offsetX;
                        container.posY = offsetY;
                        offsetX += 120;
                        if (offsetX + 120 > width) {
                            offsetX = 20;
                            offsetY += 27;
                        }
                    }
                }
                case 2 -> {
                    Random r = new Random();
                    for (Draggable container : containers) {
                        container.posX = r.nextInt(width - 120);
                        container.posY = r.nextInt(height - 27);
                    }
                }
            }
        });
        this.addDrawableChild(sort);
        super.init();
    }

    public void showModuleConfig(Module m) {
        if (m == null) {
            currentConfig = null;
            return;
        }
        if (currentConfig == null) {
            ConfigWidget currentConfig1 = new ConfigWidget(m);
            currentConfig1.posX = width / 2d - (210 / 2d);
            currentConfig1.posY = 10;
            currentConfig1.lastRenderX = currentConfig1.posX;
            currentConfig1.lastRenderY = -height;
            currentConfig = currentConfig1;
            return;
        }
        double currentLRX = currentConfig.lastRenderX;
        double currentLRY = currentConfig.lastRenderY;
        double currentPX = currentConfig.posX;
        double currentPY = currentConfig.posY;
        ConfigWidget w = new ConfigWidget(m);
        w.lastRenderX = currentLRX;
        w.lastRenderY = currentLRY;
        w.posX = currentPX;
        w.posY = currentPY;
        currentConfig = w;
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {

        fill(matrices, 0, 0, width, height, 0x10000000);
        animProgress += (System.currentTimeMillis() - lastRender) / 600d;
        if (System.currentTimeMillis() - lastRender > 1) lastRender = System.currentTimeMillis();
        animProgress = MathHelper.clamp(animProgress, 0, 1);
        double animProgressInter = animProgress < 0.5 ? 4 * animProgress * animProgress * animProgress : 1 - Math.pow(-2 * animProgress + 2, 3) / 2;
        double logoSize = me.zeroX150.atomic.feature.module.impl.render.ClickGUI.logoSize.getValue();
        if (logoSize != 0) {
            RenderSystem.setShaderColor(1, 1, 1, (float) animProgressInter);
            RenderSystem.setShaderTexture(0, LOGO);
            Screen.drawTexture(matrices, 1, 1, 0, 0, 0, (int) (504 * logoSize), (int) (130 * logoSize), (int) (130 * logoSize), (int) (504 * logoSize));
            RenderSystem.setShaderColor(1, 1, 1, 1);
        }

        MatrixStack ms = new MatrixStack();
        //ms.translate(0, (Math.abs(animProgressInter - 1) * height), 0);
        if (currentConfig != null) currentConfig.render(mouseX, mouseY, delta);
        for (Draggable container : containers) {
            container.render(ms);
        }


        super.render(matrices, mouseX, mouseY, delta);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        boolean flag = false;
        for (Draggable container : Lists.reverse(containers).toArray(new Draggable[0])) {
            if (container.mouseClicked(button == 0, mouseX, mouseY)) {
                // put element all the way to the back if its clicked, creating the
                // overlaying effect each time you move something
                containers.remove(container);
                containers.add(container);
                flag = true;
                break;
            }
        }
        if (!flag && currentConfig != null) currentConfig.mouseClicked(mouseX, mouseY, button);
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        for (Draggable container : containers) {
            container.mouseReleased();
        }
        if (currentConfig != null) currentConfig.mouseReleased(mouseX, mouseY, button);
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        for (Draggable container : containers) {
            container.mouseMove(deltaX, deltaY);
        }
        if (currentConfig != null) currentConfig.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        if (currentConfig != null) currentConfig.mouseMoved(mouseX, mouseY);
        super.mouseMoved(mouseX, mouseY);
    }

    @Override
    public boolean charTyped(char chr, int modifiers) {
        if (currentConfig != null) currentConfig.charTyped(chr, modifiers);
        return super.charTyped(chr, modifiers);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (currentConfig != null) currentConfig.keyPressed(keyCode, scanCode, modifiers);
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        if (currentConfig != null) currentConfig.keyReleased(keyCode, scanCode, modifiers);
        return super.keyReleased(keyCode, scanCode, modifiers);
    }
}


