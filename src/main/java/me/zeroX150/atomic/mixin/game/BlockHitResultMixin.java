package me.zeroX150.atomic.mixin.game;

import me.zeroX150.atomic.feature.module.ModuleRegistry;
import me.zeroX150.atomic.feature.module.impl.external.BuildLimit;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockHitResult.class)
public class BlockHitResultMixin {
    @Inject(method = "getSide", at = @At("HEAD"), cancellable = true)
    public void getSide(CallbackInfoReturnable<Direction> cir) {
        if (ModuleRegistry.getByClass(BuildLimit.class).isEnabled()) cir.setReturnValue(Direction.DOWN);
    }
}
