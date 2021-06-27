package me.zeroX150.atomic.mixin.network;

import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.util.hit.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(PlayerInteractBlockC2SPacket.class)
public interface IPlayerInteractBlockC2SPacket {
    @Mutable
    @Accessor("blockHitResult")
    void setBlockHitResult(BlockHitResult newBlockHitResult);
}
