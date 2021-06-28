package me.zeroX150.atomic.mixin.game.render;

import net.minecraft.client.render.GameRenderer;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(GameRenderer.class)
public interface IGameRendererMixin {
    @Invoker("loadShader")
    void callLoadShader(Identifier id);

}
