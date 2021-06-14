package me.zeroX150.atomic.mixin.network;

import me.zeroX150.atomic.feature.module.ModuleRegistry;
import me.zeroX150.atomic.feature.module.impl.movement.NoFall;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerMoveC2SPacket.class)
public class PlayerMoveC2SPacketMixin {
    @Mutable
    @Shadow
    @Final
    protected boolean onGround;

    @Inject(method = "<init>", at = @At("TAIL"))
    public void bruh(double x, double y, double z, float yaw, float pitch, boolean onGround, boolean changePosition, boolean changeLook, CallbackInfo ci) {
        if (NoFall.mode.getValue().equalsIgnoreCase("onground") && ModuleRegistry.getByClass(NoFall.class).isEnabled())
            this.onGround = true;
    }

    @Inject(method = "isOnGround", at = @At("HEAD"), cancellable = true)
    public void isOnGroundOverwrite(CallbackInfoReturnable<Boolean> cir) {
        if (NoFall.mode.getValue().equalsIgnoreCase("onground") && ModuleRegistry.getByClass(NoFall.class).isEnabled())
            cir.setReturnValue(true);
    }
}
