package me.zeroX150.atomic.mixin.game;

import me.zeroX150.atomic.Atomic;
import me.zeroX150.atomic.feature.module.ModuleRegistry;
import me.zeroX150.atomic.feature.module.impl.movement.Jesus;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @Inject(method = "canWalkOnFluid", at = @At("HEAD"), cancellable = true)
    public void canWalkOnFluid(Fluid fluid, CallbackInfoReturnable<Boolean> cir) {
        if (((Object) this) instanceof PlayerEntity pe) {
            if (!pe.getUuid().equals(Atomic.client.player.getUuid())) return;
            if (ModuleRegistry.getByClass(Jesus.class).isEnabled() && Jesus.mode.getValue().equalsIgnoreCase("solid"))
                cir.setReturnValue(true);
        }
    }
}
