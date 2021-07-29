package me.zeroX150.atomic.mixin.game;

import me.zeroX150.atomic.feature.module.ModuleRegistry;
import me.zeroX150.atomic.feature.module.impl.external.AntiReducedDebugInfo;
import me.zeroX150.atomic.feature.module.impl.external.FastUse;
import me.zeroX150.atomic.helper.ConfigManager;
import net.minecraft.client.MinecraftClient;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Shadow
    private int itemUseCooldown;

    @Inject(method = "stop", at = @At("HEAD"))
    public void onStop(CallbackInfo ci) {
        ConfigManager.saveState();
    }

    @Inject(method = "hasReducedDebugInfo", at = @At("HEAD"), cancellable = true)
    public void overwriteReducedDebugInfo(CallbackInfoReturnable<Boolean> cir) {
        if (ModuleRegistry.getByClass(AntiReducedDebugInfo.class).isEnabled()) cir.setReturnValue(false);
    }

    @Redirect(method = "handleInputEvents", at = @At(
            value = "FIELD",
            opcode = Opcodes.GETFIELD,
            target = "Lnet/minecraft/client/MinecraftClient;itemUseCooldown:I"
    ))
    public int replaceItemUseCooldown(MinecraftClient minecraftClient) {
        if (ModuleRegistry.getByClass(FastUse.class).isEnabled()) return 0;
        else return this.itemUseCooldown;
    }
}
