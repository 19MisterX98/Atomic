package me.zeroX150.atomic.mixin.game.render;

import me.zeroX150.atomic.Atomic;
import me.zeroX150.atomic.feature.module.Module;
import me.zeroX150.atomic.feature.module.ModuleRegistry;
import me.zeroX150.atomic.feature.module.impl.render.Zoom;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    private boolean vb;
    private boolean dis;

    @Inject(
            at = {@At(value = "FIELD",
                    target = "Lnet/minecraft/client/render/GameRenderer;renderHand:Z",
                    opcode = Opcodes.GETFIELD,
                    ordinal = 0)},
            method = {
                    "renderWorld(FJLnet/minecraft/client/util/math/MatrixStack;)V"})
    void onRenderWorld(float tickDelta, long limitTime, MatrixStack matrix, CallbackInfo ci) {
        if (vb) {
            Atomic.client.options.bobView = true;
            vb = false;
        }
        for (Module module : ModuleRegistry.getModules()) {
            if (module.isEnabled()) module.onWorldRender(matrix);
        }
    }

    @Inject(method = "getFov", at = @At("RETURN"), cancellable = true)
    public void getFov(Camera camera, float tickDelta, boolean changingFov, CallbackInfoReturnable<Double> cir) {
        double zv = Zoom.getZoomValue(cir.getReturnValue());
        cir.setReturnValue(zv);
    }

    // Mixins are broken as shit in this version or something so I have to do it this fucking dumbass way

    @Inject(at = @At("HEAD"), method = "renderWorld")
    private void renderWorldHead(float tickDelta, long limitTime, MatrixStack matrix, CallbackInfo ci) {
        dis = true;
    }

    @Inject(at = @At("HEAD"), method = "bobView", cancellable = true)
    private void fixTracerBobbing(MatrixStack matrices, float f, CallbackInfo ci) {
        if (Atomic.client.options.bobView && dis) {
            vb = true;
            Atomic.client.options.bobView = false;
            dis = false;
            ci.cancel();
        }
    }
}
