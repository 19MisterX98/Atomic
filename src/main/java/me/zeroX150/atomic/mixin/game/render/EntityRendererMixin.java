package me.zeroX150.atomic.mixin.game.render;

import me.zeroX150.atomic.feature.module.ModuleRegistry;
import me.zeroX150.atomic.feature.module.impl.render.NameTags;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin<T extends Entity> {
    @Shadow
    @Final
    protected EntityRenderDispatcher dispatcher;

    @Inject(method = "renderLabelIfPresent", at = @At("HEAD"), cancellable = true)
    public void renderLabel(T entity, Text text, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        if (entity.getCustomName() != null && entity.getCustomName().equals(Text.of("DoNotRenderThisUsernamePlease"))) {
            ci.cancel();
            return;
        }

        if (entity instanceof PlayerEntity) {
            if (Objects.requireNonNull(ModuleRegistry.getByClass(NameTags.class)).isEnabled()) {
                ((NameTags) Objects.requireNonNull(ModuleRegistry.getByClass(NameTags.class))).renderTag(entity, matrices, dispatcher);
                ci.cancel();
            }
        }
    }
}
