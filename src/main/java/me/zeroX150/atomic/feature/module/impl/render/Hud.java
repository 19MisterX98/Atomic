package me.zeroX150.atomic.feature.module.impl.render;

import me.zeroX150.atomic.Atomic;
import me.zeroX150.atomic.feature.gui.notifications.NotificationRenderer;
import me.zeroX150.atomic.feature.module.Module;
import me.zeroX150.atomic.feature.module.ModuleRegistry;
import me.zeroX150.atomic.feature.module.ModuleType;
import me.zeroX150.atomic.feature.module.config.BooleanValue;
import me.zeroX150.atomic.feature.module.config.SliderValue;
import me.zeroX150.atomic.helper.Client;
import me.zeroX150.atomic.helper.Renderer;
import me.zeroX150.atomic.helper.event.Events;
import me.zeroX150.atomic.helper.event.PacketEvents;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.network.packet.s2c.play.WorldTimeUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.apache.commons.lang3.ArrayUtils;

import java.awt.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class Hud extends Module {
    public SliderValue smoothSelectTransition = config.create("Selection smooth", 10, 1, 30, 1);

    BooleanValue fps = config.create("FPS", true);
    BooleanValue tps = config.create("TPS", true);
    BooleanValue coords = config.create("Coordinates", true);
    BooleanValue time = config.create("Time", true);
    BooleanValue ping = config.create("Ping", true);
    BooleanValue bps = config.create("Speed", true);
    BooleanValue modules = config.create("Modules", true);

    long lastTimePacketReceived;
    double currentTps = 0;

    DateFormat df = new SimpleDateFormat("h:mm aa");

    public Hud() {
        super("Hud", "poggies", ModuleType.RENDER);
        lastTimePacketReceived = System.currentTimeMillis();

        Events.Packets.registerEventHandler(PacketEvents.PACKET_RECEIVE, event -> {
            if (event.getPacket() instanceof WorldTimeUpdateS2CPacket) {
                currentTps = Client.roundToN(calcTps(System.currentTimeMillis() - lastTimePacketReceived), 2);
                lastTimePacketReceived = System.currentTimeMillis();

            }
        });
    }

    double calcTps(double n) {
        return (20.0 / Math.max((n - 1000.0) / (500.0), 1.0));
    }

    @Override
    public void tick() {

    }

    @Override
    public void enable() {

    }

    @Override
    public void disable() {

    }

    @Override
    public String getContext() {
        return null;
    }

    @Override
    public void onWorldRender(MatrixStack matrices) {

    }

    @Override
    public void onHudRender() {
        if (Atomic.client.getNetworkHandler() == null) return;
        if (Atomic.client.player == null) return;
        MatrixStack ms = new MatrixStack();
        List<HudEntry> entries = new ArrayList<>();
        if (coords.getValue()) {
            BlockPos bp = Atomic.client.player.getBlockPos();
            entries.add(new HudEntry("XYZ", bp.getX() + " " + bp.getY() + " " + bp.getZ(), false, false));
        }
        if (fps.getValue()) entries.add(new HudEntry("FPS", Atomic.client.fpsDebugString.split(" ")[0], false, false));
        if (tps.getValue()) {
            entries.add(new HudEntry("TPS", (currentTps == -1 ? "Calculating" : currentTps) + "", false, false));
        }
        if (ping.getValue()) {
            PlayerListEntry e = Atomic.client.getNetworkHandler().getPlayerListEntry(Atomic.client.player.getUuid());
            entries.add(new HudEntry("Ping", (e == null ? "?" : e.getLatency()) + " ms", false, false));
        }
        if (bps.getValue()) {
            double px = Atomic.client.player.prevX;
            double py = Atomic.client.player.prevY;
            double pz = Atomic.client.player.prevZ;
            Vec3d v = new Vec3d(px, py, pz);
            double dist = v.distanceTo(Atomic.client.player.getPos());
            entries.add(new HudEntry("Speed", Client.roundToN(dist * 20, 2) + "", false, false));
        }
        if (time.getValue()) {
            entries.add(new HudEntry("", df.format(new Date()), true, true));
        }
        //entries.sort(Comparator.comparingInt(entry -> Atomic.client.textRenderer.getWidth((entry.t.isEmpty()?"":entry.t+" ")+entry.v)));
        int yOffset = 23 / 2 + 9;
        int changedYOffset = -3;
        int xOffset = 2;
        for (HudEntry entry : entries) {
            String t = (entry.t.isEmpty() ? "" : entry.t + " ") + entry.v;
            float width = Atomic.fontRenderer.getStringWidth(t);
            int offsetToUse = Atomic.client.getWindow().getScaledHeight() - (entry.renderTaskBar ? ((23 / 2 + 9 / 2)) : yOffset);
            float xL = (entry.renderTaskBar && entry.renderRTaskBar) ? (Atomic.client.getWindow().getScaledWidth() - 5 - width) : xOffset;
            if (xL == xOffset) xOffset += width + Atomic.fontRenderer.getStringWidth(" ");
            changedYOffset++;
            if (!entry.renderTaskBar && changedYOffset == 0) {
                yOffset -= 10;
                xOffset = 2;
            }
            //Atomic.client.textRenderer.draw(ms,t,xL,offsetToUse,0xFFFFFF);
            if (!entry.t.isEmpty()) {
                Color rgb = Client.getCurrentRGB();
                Atomic.fontRenderer.drawString(ms, entry.t, xL, offsetToUse, Client.getCurrentRGB().getRGB());
                //Atomic.client.textRenderer.draw(ms, entry.t, xL, offsetToUse, Client.getCurrentRGB().getRGB());
                Atomic.fontRenderer.drawString(ms, entry.v, xL + Atomic.fontRenderer.getStringWidth(entry.t + " "), offsetToUse, rgb.darker().getRGB());
                //Atomic.client.textRenderer.draw(ms, entry.v, xL + Atomic.client.textRenderer.getWidth(entry.t + " "), offsetToUse, rgb.darker().getRGB());
            } else {
                Atomic.fontRenderer.drawString(ms, t, xL, offsetToUse, Client.getCurrentRGB().getRGB());
                //Atomic.client.textRenderer.draw(ms, t, xL, offsetToUse, Client.getCurrentRGB().getRGB());
            }
        }

        if (modules.getValue()) {
            int moduleOffset = 0;
            float rgbIncrementer = 0.03f;
            float currentRgbSeed = (System.currentTimeMillis() % 4500) / 4500f;
            // jesus fuck
            Module[] v = ModuleRegistry.getModules().stream()
                    .filter(Module::isEnabled)
                    .sorted(Comparator.comparingDouble(value -> Atomic.fontRenderer.getStringWidth(value.getName() + (value.getContext() != null ? " " + value.getContext() : "")))) // i mean it works?
                    .toArray(Module[]::new);
            ArrayUtils.reverse(v);
            for (Module module : v) {
                currentRgbSeed %= 1f;
                int r = Color.HSBtoRGB(currentRgbSeed, 0.7f, 1f);
                currentRgbSeed += rgbIncrementer;
                String w = module.getName() + (module.getContext() == null ? "" : " " + module.getContext());
                float wr = Atomic.client.getWindow().getScaledWidth() - Atomic.fontRenderer.getStringWidth(w) - 3;
                Atomic.fontRenderer.drawString(ms, module.getName(), wr, moduleOffset + 1, r);
                Color c = new Color(r);
                Color inv = new Color(Math.abs(c.getRed() - 255), Math.abs(c.getGreen() - 255), Math.abs(c.getBlue() - 255));
                Renderer.fill(c, Atomic.client.getWindow().getScaledWidth() - 2, moduleOffset, Atomic.client.getWindow().getScaledWidth(), moduleOffset + 10);
                if (module.getContext() != null)
                    Atomic.fontRenderer.drawString(ms, module.getContext(), wr + Atomic.fontRenderer.getStringWidth(module.getName() + " "), moduleOffset + 1, inv.getRGB());
                moduleOffset += 10;
            }
        }
        NotificationRenderer.render();
    }

    static class HudEntry {
        public String t;
        public String v;
        public boolean renderTaskBar;
        public boolean renderRTaskBar;

        public HudEntry(String t, String v, boolean renderInTaskBar, boolean renderRightTaskBar) {
            this.t = t;
            this.v = v;
            this.renderRTaskBar = renderRightTaskBar;
            this.renderTaskBar = renderInTaskBar;
        }
    }
}
