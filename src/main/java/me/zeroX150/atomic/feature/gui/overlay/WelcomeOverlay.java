package me.zeroX150.atomic.feature.gui.overlay;

import me.zeroX150.atomic.Atomic;
import me.zeroX150.atomic.feature.gui.screen.HomeScreen;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.BackgroundHelper;
import net.minecraft.client.gui.screen.Overlay;
import net.minecraft.client.util.math.MatrixStack;

import java.awt.*;

public class WelcomeOverlay extends Overlay {
    String[] texts = new String[]{
            "Welcome, " + Atomic.client.getSession().getUsername() + ".",
            "We are getting things ready, for you.",
            "Almost done...",
            "Welcome, " + Atomic.client.getSession().getUsername() + ", to Atomic."
    };
    double d = 0;
    boolean done = false;
    boolean decl = false;
    float prevVal = 0;

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        int w = Atomic.client.getWindow().getScaledWidth();
        int h = Atomic.client.getWindow().getScaledHeight();
        if (done) {
            d = 0;
            done = false;
            decl = false;
            Atomic.client.setOverlay(null);
            return;
        }

        d += 2 * delta;
        float c = (float) Math.abs(Math.sin(Math.toRadians(d)));
        int index = (int) Math.floor(d / 180);
        int c1 = Color.BLACK.getRGB();
        if (index >= texts.length - 1) {
            if (prevVal > c) decl = true;
            if (decl) {
                if (Atomic.client.currentScreen == null) Atomic.client.openScreen(new HomeScreen());
                Atomic.client.currentScreen.render(matrices, mouseX, mouseY, delta);
                c1 = BackgroundHelper.ColorMixer.getArgb((int) (c * 255), 0, 0, 0);
            }
            prevVal = c;
        }
        DrawableHelper.fill(matrices, 0, 0, w, h, c1);
        if (index >= texts.length) {
            done = true;
            return;
        }
        if (c > 0.07) {
            matrices.scale(1.5f, 1.5f, 1.5f);
            DrawableHelper.drawCenteredText(matrices, Atomic.client.textRenderer, texts[index], (int) ((w / 2) / 1.5), (int) ((h / 2 - (9 / 2)) / 1.5), BackgroundHelper.ColorMixer.getArgb((int) (c * 255), 255, 255, 255));
        }
    }
}
