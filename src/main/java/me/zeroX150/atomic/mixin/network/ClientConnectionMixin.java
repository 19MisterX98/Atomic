package me.zeroX150.atomic.mixin.network;

import me.zeroX150.atomic.helper.event.Event;
import me.zeroX150.atomic.helper.event.EventSystem;
import me.zeroX150.atomic.helper.event.PacketEvent;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.PacketListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientConnection.class)
public class ClientConnectionMixin {
    @Inject(method = "handlePacket", at = @At("HEAD"), cancellable = true)
    private static <T extends PacketListener> void packetReceive(Packet<T> packet, PacketListener listener, CallbackInfo ci) {
        if (EventSystem.fireEvent(Event.PACKET_RECEIVE, new PacketEvent(packet))) ci.cancel();
    }

    @Inject(method = "send(Lnet/minecraft/network/Packet;)V", cancellable = true, at = @At("HEAD"))
    public void send(Packet<?> packet, CallbackInfo ci) {
        if (EventSystem.fireEvent(Event.PACKET_SEND, new PacketEvent(packet))) ci.cancel();
    }
}
