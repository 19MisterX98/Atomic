package me.zeroX150.atomic.feature.gui.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import me.zeroX150.atomic.Atomic;
import me.zeroX150.atomic.feature.gui.clickgui.ClickGUI;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.realms.gui.screen.RealmsMainScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class HomeScreen extends Screen {

    public HomeScreen() {
        super(Text.of("a"));
    }

    @Override
    protected void init() {
        addDrawableChild(createCentered("Singleplayer", 150, height / 2 - 20 - 20 - 10, button -> Atomic.client.openScreen(new SelectWorldScreen(this))));
        addDrawableChild(createCentered("Multiplayer", 150, height / 2 - 25, button -> Atomic.client.openScreen(new MultiplayerScreen(this))));
        addDrawableChild(createCentered("Realms", 150, height / 2, button -> Atomic.client.openScreen(new RealmsMainScreen(this))));
        addDrawableChild(new ButtonWidget(width / 2 - 75, height / 2 + 25, 70, 20, Text.of("Options"), button -> Atomic.client.openScreen(new OptionsScreen(this, Atomic.client.options))));
        addDrawableChild(new ButtonWidget(width / 2 + 5, height / 2 + 25, 70, 20, Text.of("Quit"), button -> Atomic.client.stop()));
        addDrawableChild(new ButtonWidget(width / 2 - (150 / 2), height / 2 + 25 + 25, 150, 20, Text.of("Alts"), button -> Atomic.client.openScreen(new AltManager())));
        super.init();
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        renderBackgroundTexture(0);
        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.setShaderTexture(0, ClickGUI.LOGO);
        double logoSize = 0.3;
        Screen.drawTexture(matrices, (int) (width / 2 - (504 * logoSize / 2)), 10, 0, 0, 0, (int) (504 * logoSize), (int) (130 * logoSize), (int) (130 * logoSize), (int) (504 * logoSize));
        super.render(matrices, mouseX, mouseY, delta);
    }

    ButtonWidget createCentered(String t, int w, int y, ButtonWidget.PressAction action) {
        return new ButtonWidget(width / 2 - (w / 2), y, w, 20, Text.of(t), action);
    }
}
