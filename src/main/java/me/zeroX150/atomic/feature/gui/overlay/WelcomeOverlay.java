package me.zeroX150.atomic.feature.gui.overlay;

import me.zeroX150.atomic.Atomic;
import me.zeroX150.atomic.feature.gui.screen.HomeScreen;
import me.zeroX150.atomic.feature.module.Module;
import me.zeroX150.atomic.feature.module.ModuleRegistry;
import me.zeroX150.atomic.helper.Client;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.BackgroundHelper;
import net.minecraft.client.gui.screen.Overlay;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class WelcomeOverlay extends Overlay {
    String[] texts = new String[]{
            "Loading...",
            "Welcome, " + Atomic.client.getSession().getUsername() + ", to Atomic."
    };
    double d = 0;
    boolean done = false;
    boolean decl = false;
    float prevVal = 0;
    boolean finishedLoading = false;
    boolean isLoading = false;
    Thread loader;
    List<String> logs = new ArrayList<>();

    void log(String v) {
        logs.add(v);
    }

    void load() {
        if (isLoading) return;
        isLoading = true;
        loader = new Thread(() -> {
            try {
                for (Module module : ModuleRegistry.getModules().stream().sorted(Comparator.comparingDouble(value -> -Atomic.monoFontRenderer.getStringWidth(value.getName()))).collect(Collectors.toList())) {
                    log("Loaded module " + module.getName());
                    Thread.sleep(20);
                }
                Thread.sleep(1000);
                log("Checking for updates...");
                Client.isClientOutdated(new Client.OutdatedCheckCallback() {
                    @Override
                    public void callback(boolean isOutdated) {
                        log("Checked for updates!");
                        log(isOutdated ? "§cClient is outdated!" : "§aClient is up to date!");
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        finishedLoading = true;
                    }

                    @Override
                    public void log(String message) {
                        WelcomeOverlay.this.log(message);
                    }
                });
            } catch (Exception ignored) {

            }
        });
        loader.start();
    }

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

        double vd = 2.35 * delta;
        if (d > 90) {
            if (finishedLoading) d += vd;
            else {
                load();
            }
        } else d += vd;
        float c = (float) Math.abs(Math.sin(Math.toRadians(d)));
        int index = (int) Math.floor(d / 180);
        int c1 = Color.BLACK.getRGB();
        int a = 255;
        if (index >= texts.length - 1) {
            if (prevVal > c) decl = true;
            if (decl) {
                if (Atomic.client.currentScreen == null) Atomic.client.openScreen(new HomeScreen());
                Atomic.client.currentScreen.render(matrices, mouseX, mouseY, delta);
                c1 = BackgroundHelper.ColorMixer.getArgb((int) (c * 255), 0, 0, 0);
                a = (int) (c * 255);
            }
            prevVal = c;
        }
        DrawableHelper.fill(matrices, 0, 0, w, h, c1);
        if (index >= texts.length) {
            done = true;
            return;
        }
        if (c > 0.07) {
            if (index == 1) {
                logs.clear();
            }
            Atomic.fontRenderer.drawCenteredString(matrices, texts[index], w / 2f, h / 2f, BackgroundHelper.ColorMixer.getArgb((int) (c * 255), 255, 255, 255));
            //DrawableHelper.drawCenteredText(matrices, Atomic.client.textRenderer, texts[index], (int) ((w / 2) / m), (int) ((h / 2 - (9 / 2)) / m), BackgroundHelper.ColorMixer.getArgb((int) (c * 255), 255, 255, 255));
        }
        float scale = 1f;
        while (logs.size() > (Atomic.client.getWindow().getScaledHeight() - 1) / (10 * scale)) {
            logs.remove(0);
        }
        int yOffset = 1;
        for (String log : logs.toArray(new String[0])) {
            matrices.push();
            matrices.scale(scale, scale, 1);
            Atomic.monoFontRenderer.drawString(matrices, log, 1, yOffset, BackgroundHelper.ColorMixer.getArgb(MathHelper.clamp(a, 1, 255), 255, 255, 255));
            yOffset += 10;
            matrices.pop();
        }
    }
}
