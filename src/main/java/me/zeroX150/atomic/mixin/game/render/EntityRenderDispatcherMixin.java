package me.zeroX150.atomic.mixin.game.render;

import me.zeroX150.atomic.helper.event.Events;
import me.zeroX150.atomic.helper.event.RenderingEvent;
import me.zeroX150.atomic.helper.event.RenderingEvents;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderDispatcher.class)
public class EntityRenderDispatcherMixin {
    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    public <E extends Entity> void render(E entity, double x, double y, double z, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        if (Events.Rendering.fireEvent(RenderingEvents.ENTITY_RENDER, new RenderingEvent(entity, null, null, null, matrices)))
            ci.cancel();
    }
}
