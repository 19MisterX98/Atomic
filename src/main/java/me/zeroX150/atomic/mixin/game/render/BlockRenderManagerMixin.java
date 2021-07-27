package me.zeroX150.atomic.mixin.game.render;

import me.zeroX150.atomic.helper.event.Events;
import me.zeroX150.atomic.helper.event.RenderingEvent;
import me.zeroX150.atomic.helper.event.RenderingEvents;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

@Mixin(BlockRenderManager.class)
public class BlockRenderManagerMixin {
    @Inject(method = "renderBlock", at = @At("HEAD"), cancellable = true)
    public void renderBlock(BlockState state, BlockPos pos, BlockRenderView world, MatrixStack matrix, VertexConsumer vertexConsumer, boolean cull, Random random, CallbackInfoReturnable<Boolean> cir) {
        if (Events.Rendering.fireEvent(RenderingEvents.BLOCK_RENDER, new RenderingEvent(null, null, state, pos, matrix)))
            cir.setReturnValue(true);
    }
}
