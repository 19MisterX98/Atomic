package me.zeroX150.atomic.mixin.game;

import me.zeroX150.atomic.feature.module.Module;
import me.zeroX150.atomic.feature.module.ModuleRegistry;
import me.zeroX150.atomic.helper.ConfigManager;
import me.zeroX150.atomic.helper.keybind.KeybindManager;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin {
    @Inject(method = "tick", at = @At("HEAD"))
    public void tick(CallbackInfo ci) {
        if (!ConfigManager.enabled) ConfigManager.enableModules();
        for (Module module : ModuleRegistry.getModules()) {
            if (module.isEnabled()) module.tick();
        }
        KeybindManager.update();
    }
}
