package me.zeroX150.atomic.mixin.game.render;

import me.zeroX150.atomic.helper.event.Events;
import me.zeroX150.atomic.helper.event.RenderingEvent;
import me.zeroX150.atomic.helper.event.RenderingEvents;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public class BlockModelRendererMixin {
    @Inject(method = "updateBlock", at = @At("HEAD"), cancellable = true)
    public void render(BlockView world, BlockPos pos, BlockState oldState, BlockState newState, int flags, CallbackInfo ci) {
        if (Events.Rendering.fireEvent(RenderingEvents.BLOCK_RENDER, new RenderingEvent(null, new RenderingEvent.BlockTarget(newState, pos), null, null)))
            ci.cancel();
    }
}
