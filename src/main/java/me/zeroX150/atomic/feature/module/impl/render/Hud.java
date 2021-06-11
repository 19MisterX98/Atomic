package me.zeroX150.atomic.feature.module.impl.render;

import me.zeroX150.atomic.Atomic;
import me.zeroX150.atomic.feature.module.Module;
import me.zeroX150.atomic.feature.module.ModuleRegistry;
import me.zeroX150.atomic.feature.module.ModuleType;
import me.zeroX150.atomic.feature.module.config.BooleanValue;
import me.zeroX150.atomic.feature.module.config.SliderValue;
import me.zeroX150.atomic.helper.Client;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.awt.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class Hud extends Module {
    public SliderValue smoothSelectTransition = config.create("Selection smooth",10,1,30,1);

    BooleanValue fps = config.create("FPS",true);
    BooleanValue coords = config.create("Coordinates", true);
    BooleanValue time = config.create("Time", true);
    BooleanValue ping = config.create("Ping", true);
    BooleanValue bps = config.create("Speed", true);
    BooleanValue modules = config.create("Modules", true);

    DateFormat df = new SimpleDateFormat("h:mm aa");

    public Hud() {
        super("Hud", "poggies", ModuleType.RENDER);
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
        MatrixStack ms = new MatrixStack();
        List<HudEntry> entries = new ArrayList<>();
        if (fps.getValue()) entries.add(new HudEntry("FPS","lmao", false,false));
        if (coords.getValue()) {
            BlockPos bp = Atomic.client.player.getBlockPos();
            entries.add(new HudEntry("XYZ", bp.getX()+" "+bp.getY()+" "+bp.getZ(), false,false));
        }
        if (time.getValue()) {
            entries.add(new HudEntry("",df.format(new Date()),true,true));
        }
        if (ping.getValue()) entries.add(new HudEntry("Ping",Atomic.client.getNetworkHandler().getPlayerListEntry(Atomic.client.player.getUuid()).getLatency()+" ms",false,false));
        if (bps.getValue()) {
            double px = Atomic.client.player.prevX;
            double py = Atomic.client.player.prevY;
            double pz = Atomic.client.player.prevZ;
            Vec3d v = new Vec3d(px,py,pz);
            double dist = v.distanceTo(Atomic.client.player.getPos());
            entries.add(new HudEntry("Speed", Client.roundToN(dist,2)+"",false,false));
        }
        entries.sort(Comparator.comparingInt(entry -> Atomic.client.textRenderer.getWidth((entry.t.isEmpty()?"":entry.t+" ")+entry.v)));
        int yOffset = 24+9;
        for (HudEntry entry : entries) {
            String t = (entry.t.isEmpty()?"":entry.t+" ")+entry.v;
            int width = Atomic.client.textRenderer.getWidth(t);
            int offsetToUse = Atomic.client.getWindow().getScaledHeight()-(entry.renderTaskBar?((22/2+9/2)):yOffset);
            int xL = (entry.renderTaskBar&&entry.renderRTaskBar)?(Atomic.client.getWindow().getScaledWidth()-5-width):4;
            yOffset += entry.renderTaskBar?0:11;
            //Atomic.client.textRenderer.draw(ms,t,xL,offsetToUse,0xFFFFFF);
            if (!entry.t.isEmpty()) {
                Color rgb = Client.getCurrentRGB();
                Atomic.client.textRenderer.draw(ms,entry.t,xL,offsetToUse, Client.getCurrentRGB().getRGB());
                Atomic.client.textRenderer.draw(ms,entry.v,xL+Atomic.client.textRenderer.getWidth(entry.t+" "),offsetToUse,rgb.darker().getRGB());
            } else {
                Atomic.client.textRenderer.draw(ms,t,xL,offsetToUse,Client.getCurrentRGB().getRGB());
            }
        }

        if (modules.getValue()) {
            int moduleOffset = 0;
            float rgbIncrementer = 0.03f;
            float currentRgbSeed = 0;
            for (Module module : ModuleRegistry.getModules().stream().filter(Module::isEnabled).toArray(Module[]::new)) {
                int r = Color.HSBtoRGB(currentRgbSeed,0.7f,1f);
                currentRgbSeed += rgbIncrementer;

                String w = module.getName()+(module.getContext()==null?"":" "+module.getContext());
                int wr = Atomic.client.getWindow().getScaledWidth()-Atomic.client.textRenderer.getWidth(w)-3;
                Atomic.client.textRenderer.draw(ms,w,wr,moduleOffset+1,r);
                moduleOffset += 11;
            }

        }
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
