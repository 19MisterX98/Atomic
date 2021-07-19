package me.zeroX150.atomic.feature.module.impl.misc;

import me.zeroX150.atomic.feature.gui.notifications.Notification;
import me.zeroX150.atomic.feature.module.Module;
import me.zeroX150.atomic.feature.module.ModuleType;
import me.zeroX150.atomic.feature.module.config.BooleanValue;
import me.zeroX150.atomic.feature.module.config.SliderValue;
import me.zeroX150.atomic.helper.event.Events;
import me.zeroX150.atomic.helper.event.PacketEvents;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.network.packet.s2c.play.ParticleS2CPacket;

public class AntiParticleCrash extends Module {
    SliderValue limit = (SliderValue) this.config.create("Particle limit", 100_000, 10_000, 5_000_000, 0).description("Max amount of particles a single packet can have");
    BooleanValue notify = (BooleanValue) this.config.create("Notify", true).description("Notify you when a packet was terminated");
    int attempts = 0;

    public AntiParticleCrash() {
        super("AntiParticleCrash", "prevents you from getting crashed by a ton of particles", ModuleType.MISC);
        Events.Packets.registerEventHandler(PacketEvents.PACKET_RECEIVE, event -> {
            if (!this.isEnabled()) return;
            if (event.getPacket() instanceof ParticleS2CPacket pack) {
                if (pack.getCount() > limit.getValue()) {
                    if (notify.getValue()) {
                        Notification.create(7000, "AntiParticleCrash", "You just received a packet with " + pack.getCount() + " particles. Blocking...");
                    }
                    event.setCancelled(true);
                    attempts++;
                }
            }
        });
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
        return attempts + " PD";
    }

    @Override
    public void onWorldRender(MatrixStack matrices) {

    }

    @Override
    public void onHudRender() {

    }
}

