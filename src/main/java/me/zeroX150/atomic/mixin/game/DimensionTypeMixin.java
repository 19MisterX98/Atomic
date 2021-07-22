package me.zeroX150.atomic.mixin.game;

import me.zeroX150.atomic.feature.module.impl.render.OreSim.OreSim;
import net.minecraft.util.Identifier;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(DimensionType.class)
public class DimensionTypeMixin implements OreSim.DimensionTypeCaller {

    @Shadow @Final private Identifier infiniburn;

    @Override
    public Identifier getInfiniburn() {
        return this.infiniburn;
    }

}
