package me.zeroX150.atomic.feature.gui.clickgui;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import me.zeroX150.atomic.feature.module.Module;
import me.zeroX150.atomic.feature.module.ModuleRegistry;
import me.zeroX150.atomic.feature.module.ModuleType;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ClickGUI extends Screen {
    public static ClickGUI INSTANCE;
    public static double SMOOTH_DIV = 20;

    public static Color INACTIVE = new Color(17, 17, 17, 186);
    public static Color ACTIVE = new Color(49, 49, 49, 186);
    public static Color HEADER_RET = new Color(38, 38, 38, 255);
    public static Color HEADER_EXP = new Color(49, 49, 49, 255);

    double animProgress = 0;
    Identifier LOGO = new Identifier("atomic", "icon.png");

    ConfigWidget currentConfig = null;

    List<Draggable> containers = new ArrayList<>();

    public ClickGUI() {
        super(Text.of(""));

        INSTANCE = this;
        for (ModuleType value : ModuleType.values()) {
            Draggable d = new Draggable(value.getName(), false);
            for (Module module : ModuleRegistry.getModules()) {
                if (module.getModuleType() == value) {
                    Clickable w = new Clickable(module);
                    d.addChild(w);
                }
            }
            containers.add(d);
        }
    }

    @Override
    protected void init() {
        animProgress = 0;
        super.init();
    }

    public void showModuleConfig(Module m) {
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

        fill(matrices, 0, 0, width, height, 0x50000000);
        animProgress += 0.006;
        animProgress = MathHelper.clamp(animProgress, 0, 1);
        double animProgressInter = animProgress < 0.5 ? 16 * animProgress * animProgress * animProgress * animProgress * animProgress : 1 - Math.pow(-2 * animProgress + 2, 5) / 2;
        /*RenderSystem.setShaderColor(1,1,1,1);
        RenderSystem.setShaderTexture(0,LOGO);*/
        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.setShaderTexture(0, LOGO);
        double i = easeOutBack(animProgress);
        Screen.drawTexture(matrices, (int) (width / 2 - (128 * i / 2)), (int) (height / 2 - (128 * i / 2)), 0, 0, 0, (int) (128 * i), (int) (128 * i), (int) (128 * i), (int) (128 * i));

        MatrixStack ms = new MatrixStack();
        ms.translate(0, (Math.abs(animProgressInter - 1) * height), 0);
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

    double easeOutBack(double x) {
        double c1 = 1.70158;
        double c3 = c1 + 1;

        return 1 + c3 * Math.pow(x - 1, 3) + c1 * Math.pow(x - 1, 2);

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


