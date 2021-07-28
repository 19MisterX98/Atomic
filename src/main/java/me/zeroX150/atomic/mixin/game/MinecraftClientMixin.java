package me.zeroX150.atomic.mixin.game;

import me.zeroX150.atomic.feature.module.ModuleRegistry;
import me.zeroX150.atomic.feature.module.impl.external.AntiReducedDebugInfo;
import me.zeroX150.atomic.helper.ConfigManager;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Inject(method = "stop", at = @At("HEAD"))
    public void onStop(CallbackInfo ci) {
        ConfigManager.saveState();
    }

    @Inject(method = "hasReducedDebugInfo", at = @At("HEAD"), cancellable = true)
    public void overwriteReducedDebugInfo(CallbackInfoReturnable<Boolean> cir) {
        if (ModuleRegistry.getByClass(AntiReducedDebugInfo.class).isEnabled()) cir.setReturnValue(false);
    }
}
