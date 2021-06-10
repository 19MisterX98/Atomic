package me.zeroX150.atomic.helper.event;

import net.minecraft.network.Packet;

public class PacketEvent {
    private final Packet<?> packet;
    private boolean cancelled = false;

    public PacketEvent(Packet<?> packet) {
        this.packet = packet;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean isCancelled) {
        this.cancelled = isCancelled;
    }

    public Packet<?> getPacket() {
        return packet;
    }
}
