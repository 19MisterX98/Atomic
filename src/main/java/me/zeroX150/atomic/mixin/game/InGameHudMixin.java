package me.zeroX150.atomic.mixin.game;

import me.zeroX150.atomic.feature.module.Module;
import me.zeroX150.atomic.feature.module.ModuleRegistry;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin {
    @Inject(method = "render", at = @At("RETURN"))
    public void render(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        for (Module module : ModuleRegistry.getModules()) {
            if (module.isEnabled()) module.onHudRender();
        }
    }
}
