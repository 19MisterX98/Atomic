package me.zeroX150.atomic.mixin.network;

import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(PlayerMoveC2SPacket.class)
public interface IPlayerMoveC2SPacketAccessor {
    @Mutable
    @Accessor("onGround")
    void setOnGround(boolean onGround);

    @Accessor("x")
    double getX();

    @Mutable
    @Accessor("x")
    void setX(double x);

    @Accessor("y")
    double getY();

    @Mutable
    @Accessor("y")
    void setY(double y);

    @Accessor("z")
    double getZ();

    @Mutable
    @Accessor("z")
    void setZ(double z);
}
