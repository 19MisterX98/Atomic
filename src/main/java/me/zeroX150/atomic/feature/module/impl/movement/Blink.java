package me.zeroX150.atomic.feature.module.impl.movement;

import me.zeroX150.atomic.Atomic;
import me.zeroX150.atomic.feature.module.Module;
import me.zeroX150.atomic.feature.module.ModuleType;
import me.zeroX150.atomic.feature.module.config.MultiValue;
import me.zeroX150.atomic.helper.event.Events;
import me.zeroX150.atomic.helper.event.PacketEvents;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.c2s.play.KeepAliveC2SPacket;

import java.util.ArrayList;
import java.util.List;

public class Blink extends Module {
    MultiValue mode = (MultiValue) this.config.create("Mode", "delay", "delay", "drop").description("Whether or not to delay or drop the packets");
    List<Packet<?>> queue = new ArrayList<>();

    public Blink() {
        super("Blink", "hehe blink go brrr", ModuleType.MOVEMENT);
        Module parent = this;
        Events.Packets.registerEventHandler(PacketEvents.PACKET_SEND, event -> {
            if (!parent.isEnabled()) return;
            if (event.getPacket() instanceof KeepAliveC2SPacket) return;
            event.setCancelled(true);
            if (mode.getValue().equalsIgnoreCase("delay")) {
                queue.add(event.getPacket());
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
        if (Atomic.client.player == null || Atomic.client.getNetworkHandler() == null) return;
        for (Packet<?> packet : queue.toArray(new Packet<?>[0])) {
            Atomic.client.getNetworkHandler().sendPacket(packet);
        }
        queue.clear();
    }

    @Override
    public String getContext() {
        return queue.size() + "";
    }

    @Override
    public void onWorldRender(MatrixStack matrices) {

    }

    @Override
    public void onHudRender() {

    }
}
