package me.zeroX150.atomic.mixin.network;

import me.zeroX150.atomic.feature.module.ModuleRegistry;
import me.zeroX150.atomic.feature.module.impl.render.OreSim;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.ChunkDataS2CPacket;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerRespawnS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {
    OreSim oresim;

    @Inject(method = "onChunkData", at = @At(value = "TAIL"))
    private void onChunkData(ChunkDataS2CPacket packet, CallbackInfo ci) {
        if (oresim == null)
            oresim = (OreSim) ModuleRegistry.getByClass(OreSim.class);
        if (oresim.isEnabled()) {
            oresim.doMathOnChunk(packet.getX(), packet.getZ());
        }
    }

    @Inject(method = "onGameJoin", at = @At(value = "TAIL"))
    private void onGameJoin(GameJoinS2CPacket packet, CallbackInfo ci) {
        reloadOresim();
    }

    @Inject(method = "onPlayerRespawn", at = @At("TAIL"))
    private void respawn(PlayerRespawnS2CPacket packet, CallbackInfo ci) {
        reloadOresim();
    }

    private void reloadOresim() {
        if (oresim == null)
            oresim = (OreSim) ModuleRegistry.getByClass(OreSim.class);
        if (oresim.isEnabled()) {
            oresim.reload();
        }
    }
}
