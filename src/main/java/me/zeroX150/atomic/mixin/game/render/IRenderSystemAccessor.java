package me.zeroX150.atomic.mixin.game.render;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.util.math.Vec3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(RenderSystem.class)
public interface IRenderSystemAccessor {
    @Accessor("shaderLightDirections")
    static Vec3f[] getShaderLightDirections() {
        throw new AssertionError();
    }
}
