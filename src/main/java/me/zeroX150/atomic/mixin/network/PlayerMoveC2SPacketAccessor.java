package me.zeroX150.atomic.mixin.network;

import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(PlayerMoveC2SPacket.class)
public interface PlayerMoveC2SPacketAccessor {
    @Accessor("onGround")
    boolean getOnGround();

    @Accessor("onGround")
    void setOnGround(boolean onGround);
}
