package me.zeroX150.atomic.feature.gui.screen;

import me.zeroX150.atomic.Atomic;
import me.zeroX150.atomic.helper.Client;
import me.zeroX150.atomic.helper.Renderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.StreamSupport;

public class ServerInfoScreen extends Screen {
    static List<Double> c2sLog = new ArrayList<>();
    static List<Double> s2cLog = new ArrayList<>();
    int timer = 0;
    int margin = 2;

    public ServerInfoScreen() {
        super(Text.of(""));
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        renderBackground(matrices);

        float pSent = Atomic.client.getNetworkHandler().getConnection().getAveragePacketsSent();
        float pRecv = Atomic.client.getNetworkHandler().getConnection().getAveragePacketsReceived();


        timer++;
        boolean activeFrame = timer > 50;
        if (activeFrame) timer = 0;
        int h = 80;
        Color c = new Color(38, 83, 92, 70);
        // c2s traffic
        Renderer.fill(c, margin, margin, width - margin, h - (margin / 2d));
        DrawableHelper.drawCenteredText(matrices, textRenderer, "Client -> Server traffic  " + Client.roundToN(pSent, 1) + " PPS", width / 2, margin + 2, 0xFFFFFF);
        double max = 10;
        if (activeFrame) c2sLog.add((double) pSent);
        if (activeFrame) s2cLog.add((double) pRecv);
        while (c2sLog.size() > (width / 3) - 2) c2sLog.remove(0);
        while (s2cLog.size() > (width / 3) - 2) s2cLog.remove(0);
        for (Double aDouble : c2sLog) {
            max = Math.max(max, aDouble);
        }
        int xOffset = margin + 4;
        double lastY = 0;
        double baseY = h - (margin / 2d) - 1;
        for (Double aDouble : c2sLog) {
            double interY = (aDouble / max) * (h - 18);
            if (lastY == 0) lastY = interY;
            Renderer.lineScreenD(Client.getCurrentRGB(), xOffset - 3, baseY - lastY, xOffset, baseY - interY);
            lastY = interY;
            xOffset += 3;
        }


        // s2c traffic
        Renderer.fill(c, margin, h + (margin / 2d), width - margin, (h + (margin / 2d)) * 2);
        DrawableHelper.drawCenteredText(matrices, textRenderer, "Server -> Client traffic  " + Client.roundToN(pRecv, 1) + " PPS", width / 2, (int) (h + (margin / 2d) + 2), 0xFFFFFF);
        double max1 = 1;
        for (Double aDouble : s2cLog) {
            max1 = Math.max(max1, aDouble);
        }
        int xOffset1 = margin + 4;
        double lastY1 = 0;
        double baseY1 = (h + (margin / 2d)) * 2 - 1;
        for (Double aDouble : s2cLog) {
            double interY = (aDouble / max1) * (h - 18);
            if (lastY1 == 0) lastY1 = interY;
            Renderer.lineScreenD(Client.getCurrentRGB(), xOffset1 - 3, baseY1 - lastY1, xOffset1, baseY1 - interY);
            lastY1 = interY;
            xOffset1 += 3;
        }

        Map<String, String> contents = new HashMap<>();
        contents.put("Average Packets Received", pRecv + "");
        contents.put("Average Packets Sent", pSent + "");
        contents.put("Server Address", Atomic.client.getNetworkHandler().getConnection().getAddress().toString());
        contents.put("Is connection encrypted?", Atomic.client.getNetworkHandler().getConnection().isEncrypted() ? "Yes" : "No");
        contents.put("Is connection local?", Atomic.client.getNetworkHandler().getConnection().isLocal() ? "Yes" : "No");
        contents.put("Players", Atomic.client.getNetworkHandler().getPlayerList().size() + "");
        contents.put("Players in render distance", StreamSupport.stream(Atomic.client.world.getEntities().spliterator(), false).filter(entity -> entity instanceof PlayerEntity).count() + "");

        int maxWidth = 0;
        for (String s : contents.keySet()) {
            maxWidth = Math.max(textRenderer.getWidth(s), maxWidth);
        }
        maxWidth += textRenderer.getWidth("  ");
        int yO = 0;
        float baseY2 = (float) (baseY1 + margin + 1);
        for (String s : contents.keySet()) {
            textRenderer.draw(matrices, s, margin, baseY2 + yO, 0xFFFFFF);
            textRenderer.draw(matrices, contents.get(s), margin + maxWidth, baseY2 + yO, 0xAAFFAA);
            yO += 10;
        }

        super.render(matrices, mouseX, mouseY, delta);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
