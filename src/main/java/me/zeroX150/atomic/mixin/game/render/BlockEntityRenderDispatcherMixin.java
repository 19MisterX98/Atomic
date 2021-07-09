package me.zeroX150.atomic.mixin.game.render;

import me.zeroX150.atomic.helper.event.Events;
import me.zeroX150.atomic.helper.event.RenderingEvent;
import me.zeroX150.atomic.helper.event.RenderingEvents;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockEntityRenderDispatcher.class)
public class BlockEntityRenderDispatcherMixin {
    @Inject(method = "render(Lnet/minecraft/block/entity/BlockEntity;FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;)V", at = @At("HEAD"), cancellable = true)
    public <E extends BlockEntity> void render(E blockEntity, float tickDelta, MatrixStack matrix, VertexConsumerProvider vertexConsumerProvider, CallbackInfo ci) {
        if (Events.Rendering.fireEvent(RenderingEvents.BLOCK_ENTITY_RENDER, new RenderingEvent(null, null, blockEntity, matrix)))
            ci.cancel();
    }
}
