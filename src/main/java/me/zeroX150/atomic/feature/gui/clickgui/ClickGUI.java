package me.zeroX150.atomic.feature.gui.clickgui;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import me.zeroX150.atomic.Atomic;
import me.zeroX150.atomic.feature.module.Module;
import me.zeroX150.atomic.feature.module.ModuleRegistry;
import me.zeroX150.atomic.feature.module.ModuleType;
import me.zeroX150.atomic.helper.Renderer;
import me.zeroX150.atomic.mixin.game.render.IGameRendererMixin;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class ClickGUI extends Screen {
    public static ClickGUI INSTANCE;

    public static Color INACTIVE = new Color(17, 17, 17, 220);
    public static Color ACTIVE = new Color(40, 40, 40, 220);
    public static Color L_HIGHLIGHT = new Color(0, 194, 111, 220);
    public static Color HEADER_RET = new Color(38, 38, 38, 255);
    public static Color HEADER_EXP = new Color(49, 49, 49, 255);

    public static Identifier LOGO = new Identifier("atomic", "logo.png");

    ConfigWidget currentConfig = null;
    double aProg = 2.0;

    int clicks = 0;
    int p = 0;

    long lastRender = System.currentTimeMillis();

    List<Draggable> containers = new ArrayList<>();
    int currentSortMode = 0;
    boolean closed = false;
    String desc = "";
    boolean alreadyInitialized = false;

    public ClickGUI() {
        super(Text.of(""));
        double width = Atomic.client.getWindow().getScaledWidth();
        INSTANCE = this;
        double maxWidth = (ModuleType.values().length - 1) * 114;
        double originalX = width / 2 - (maxWidth / 2);
        double offsetX = originalX;
        double offsetY = me.zeroX150.atomic.feature.module.impl.render.ClickGUI.logoSize.getValue() * 130 + 20;

        for (ModuleType value : ModuleType.values()) {
            if (value == ModuleType.HIDDEN) continue;
            Draggable d = new Draggable(value.getName(), false);
            d.lastRenderX = width;
            d.lastRenderY = offsetY;
            d.posX = offsetX;
            d.posY = offsetY;
            offsetX += 120;
            if (offsetX + 120 > width) {
                offsetX = originalX;
                offsetY += 27;
            }
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
    public void tick() {
        p++;
        if (p > 8) {
            p = 0;
            clicks--;
            clicks = MathHelper.clamp(clicks, 0, 3);
        }
        super.tick();
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return true;
    }

    @Override
    public void onClose() {
        closed = true;
        alreadyInitialized = false;
    }

    @Override
    protected void init() {
        if (!alreadyInitialized)
            ((IGameRendererMixin) Atomic.client.gameRenderer).callLoadShader(new Identifier("shaders/post/antialias.json"));
        alreadyInitialized = true;
        aProg = 1;
        closed = false;
        int off = 21;
        int offY = 70;
        ButtonWidget sort = new ButtonWidget(width - offY, height - off, 20, 20, Text.of("S"), button -> {
            currentSortMode++;
            currentSortMode %= 2;
            double maxWidth = (ModuleType.values().length - 1) * 114;
            double originalX = this.width / 2d - (maxWidth / 2);
            double offsetX = originalX;
            double offsetY = me.zeroX150.atomic.feature.module.impl.render.ClickGUI.logoSize.getValue() * 130 + 20;
            for (Draggable container : containers.stream().sorted(Comparator.comparingInt(value -> currentSortMode == 0 ? value.children.size() : -value.children.size())).collect(Collectors.toList())) {
                container.posX = offsetX;
                container.posY = offsetY;
                offsetX += 120;
                if (offsetX + 120 > width) {
                    offsetX = originalX;
                    offsetY += 27;
                }
            }
        });
        ButtonWidget expand = new ButtonWidget(width - offY - 21, height - off, 20, 20, Text.of("E"), button -> {
            for (Draggable container : containers) {
                container.expanded = true;
            }
        });
        ButtonWidget retract = new ButtonWidget(width - offY - 42, height - off, 20, 20, Text.of("R"), button -> {
            for (Draggable container : containers) {
                container.expanded = false;
            }
        });
        this.addDrawableChild(sort);
        addDrawableChild(expand);
        addDrawableChild(retract);
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
            currentConfig1.posY = height / 2d;
            currentConfig1.lastRenderX = currentConfig1.posX;
            currentConfig1.lastRenderY = height;
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

    public void renderDescription(String desc) {
        this.desc = desc;
    }

    double easeOutBounce(double x) {
        return x < 0.5 ? 16 * x * x * x * x * x : 1 - Math.pow(-2 * x + 2, 5) / 2;
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        double a = 0.009 * (delta + 1);
        aProg += closed ? a : -a;
        aProg = MathHelper.clamp(aProg, 0, 1);

        if (aProg == 1 && closed) {
            Atomic.client.openScreen(null);
            ((IGameRendererMixin) Atomic.client.gameRenderer).callLoadShader(new Identifier("shaders/post/none.json"));
            return;
        }
        double aProgI = easeOutBounce(aProg);
        fill(matrices, 0, 0, width, height, Renderer.lerp(new Color(0, 0, 0, 0), new Color(0, 0, 0, 0x50), aProgI).getRGB());
        matrices.translate(-aProgI * width, 0, 0);
        if (System.currentTimeMillis() - lastRender > 1) lastRender = System.currentTimeMillis();
        double logoSize = me.zeroX150.atomic.feature.module.impl.render.ClickGUI.logoSize.getValue();
        if (logoSize != 0) {
            RenderSystem.setShaderTexture(0, ClickGUI.LOGO);
            RenderSystem.enableBlend();
            RenderSystem.blendEquation(32774);
            RenderSystem.blendFunc(770, 1);
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1F, 1F);
            drawTexture(matrices, (int) (width / 2 - (504 * logoSize / 2)), 10, 0, 0, 0, (int) (504 * logoSize), (int) (130 * logoSize), (int) (130 * logoSize), (int) (504 * logoSize));
            RenderSystem.defaultBlendFunc();
            RenderSystem.disableBlend();
        }
        matrices.translate(2 * aProgI * width, 0, 0);
        if (currentConfig != null) currentConfig.render(matrices, mouseX, mouseY, delta);
        for (Draggable container : containers) {
            MatrixStack ms = new MatrixStack();
            ms.translate(0, aProgI * height, 0);
            container.render(ms, delta);
        }
        Atomic.fontRenderer.drawCenteredString(matrices, desc, width / 2f, height - 70, 0xFFFFFF);
        //DrawableHelper.drawCenteredText(matrices, Atomic.client.textRenderer, desc, width / 2, height - 70, 0xFFFFFF);
        desc = "";
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
        if (!flag && currentConfig != null) {
            currentConfig.mouseClicked(mouseX, mouseY, button);
        }
        if (!flag & button == 1) {
            clicks++;
            if (clicks >= 2) {
                for (Draggable container : containers) {
                    container.posX = mouseX;
                    container.posY = mouseY;
                }
            }
            if (clicks >= 3) {
                Random r = new Random();
                for (Draggable container : containers) {
                    container.posX = r.nextInt(width);
                    container.posY = r.nextInt(height);
                }

            }
        }
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


