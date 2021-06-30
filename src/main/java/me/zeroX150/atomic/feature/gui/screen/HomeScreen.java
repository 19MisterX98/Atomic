package me.zeroX150.atomic.feature.gui.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import me.zeroX150.atomic.Atomic;
import me.zeroX150.atomic.feature.gui.clickgui.ClickGUI;
import me.zeroX150.atomic.feature.gui.overlay.WelcomeOverlay;
import me.zeroX150.atomic.feature.gui.particles.ParticleManager;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.realms.gui.screen.RealmsMainScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class HomeScreen extends Screen {
    static boolean shownWelcome = false;
    ParticleManager pm = new ParticleManager(200);

    public HomeScreen() {
        super(Text.of("a"));
    }

    @Override
    protected void init() {
        if (!shownWelcome) {
            shownWelcome = true;
            Atomic.client.setOverlay(new WelcomeOverlay());
        }
        addDrawableChild(createCentered("Singleplayer", height / 2 - 20 - 20 - 10, button -> Atomic.client.openScreen(new SelectWorldScreen(this))));
        addDrawableChild(createCentered("Multiplayer", height / 2 - 25, button -> Atomic.client.openScreen(new MultiplayerScreen(this))));
        addDrawableChild(createCentered("Realms", height / 2, button -> Atomic.client.openScreen(new RealmsMainScreen(this))));
        addDrawableChild(new ButtonWidget(width / 2 - 75, height / 2 + 25, 70, 20, Text.of("Options"), button -> Atomic.client.openScreen(new OptionsScreen(this, Atomic.client.options))));
        addDrawableChild(new ButtonWidget(width / 2 + 5, height / 2 + 25, 70, 20, Text.of("Quit"), button -> Atomic.client.stop()));
        addDrawableChild(new ButtonWidget(width / 2 - (150 / 2), height / 2 + 25 + 25, 150, 20, Text.of("Alts"), button -> Atomic.client.openScreen(new AltManager())));
        addDrawableChild(new ButtonWidget(1, 1, 20, 20, Text.of("D"), button -> Atomic.client.openScreen(new TestScreen())));
        super.init();
    }

    @Override
    public void tick() {
        pm.tick();
        super.tick();
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        renderBackgroundTexture(0);
        pm.render();
        double logoSize = 0.3;
        RenderSystem.setShaderTexture(0, ClickGUI.LOGO);
        RenderSystem.enableBlend();
        RenderSystem.blendEquation(32774);
        RenderSystem.blendFunc(770, 1);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1F, 1F);
        drawTexture(matrices, (int) (width / 2 - (504 * logoSize / 2)), 10, 0, 0, 0, (int) (504 * logoSize), (int) (130 * logoSize), (int) (130 * logoSize), (int) (504 * logoSize));
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableBlend();
        super.render(matrices, mouseX, mouseY, delta);
    }

    ButtonWidget createCentered(String t, int y, ButtonWidget.PressAction action) {
        return new ButtonWidget(width / 2 - (150 / 2), y, 150, 20, Text.of(t), action);
    }
}
