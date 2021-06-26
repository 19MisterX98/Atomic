package me.zeroX150.atomic.mixin.game.entity;

import me.zeroX150.atomic.Atomic;
import me.zeroX150.atomic.feature.module.ModuleRegistry;
import me.zeroX150.atomic.feature.module.impl.external.NoPush;
import me.zeroX150.atomic.feature.module.impl.movement.Jesus;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.Fluid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @Inject(method = "canWalkOnFluid", at = @At("HEAD"), cancellable = true)
    public void canWalkOnFluid(Fluid fluid, CallbackInfoReturnable<Boolean> cir) {
        if (Atomic.client.player == null) return;
        // shut up monkey these are mixins you fucking idiot
        if (this.equals(Atomic.client.player)) {
            if (ModuleRegistry.getByClass(Jesus.class).isEnabled() && Jesus.mode.getValue().equalsIgnoreCase("solid"))
                cir.setReturnValue(true);
        }
    }

    @Inject(method = "pushAwayFrom", at = @At("HEAD"), cancellable = true)
    public void pushAwayFrom(Entity entity, CallbackInfo ci) {
        if (Atomic.client.player == null) return;
        if (this.equals(Atomic.client.player)) {
            if (ModuleRegistry.getByClass(NoPush.class).isEnabled())
                ci.cancel();
        }
    }
}
