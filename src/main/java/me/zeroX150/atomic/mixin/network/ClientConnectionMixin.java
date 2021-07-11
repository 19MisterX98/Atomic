package me.zeroX150.atomic.mixin.network;

import io.netty.channel.ChannelHandlerContext;
import me.zeroX150.atomic.Atomic;
import me.zeroX150.atomic.feature.module.ModuleRegistry;
import me.zeroX150.atomic.feature.module.impl.external.AntiPacketKick;
import me.zeroX150.atomic.helper.Client;
import me.zeroX150.atomic.helper.event.Events;
import me.zeroX150.atomic.helper.event.PacketEvent;
import me.zeroX150.atomic.helper.event.PacketEvents;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.network.packet.s2c.play.DisconnectS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientConnection.class)
public class ClientConnectionMixin {
    @Inject(method = "handlePacket", at = @At("HEAD"), cancellable = true)
    private static <T extends PacketListener> void packetReceive(Packet<T> packet, PacketListener listener, CallbackInfo ci) {
        if (packet instanceof DisconnectS2CPacket) Client.latestServerInfo = Atomic.client.getCurrentServerEntry();
        if (Events.Packets.fireEvent(PacketEvents.PACKET_RECEIVE, new PacketEvent(packet))) ci.cancel();
    }

    @Inject(method = "exceptionCaught", at = @At("HEAD"), cancellable = true)
    public void exceptionCaught(ChannelHandlerContext context, Throwable ex, CallbackInfo ci) {
        if (ModuleRegistry.getByClass(AntiPacketKick.class).isEnabled()) ci.cancel();
    }

    @Inject(method = "send(Lnet/minecraft/network/Packet;)V", cancellable = true, at = @At("HEAD"))
    public void send(Packet<?> packet, CallbackInfo ci) {
        if (Events.Packets.fireEvent(PacketEvents.PACKET_SEND, new PacketEvent(packet))) ci.cancel();
    }
}
