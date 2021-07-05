package me.zeroX150.atomic.mixin.game;

import com.mojang.datafixers.DataFixerBuilder;
import me.zeroX150.atomic.helper.LazyDataFixerBuilder;
import net.minecraft.datafixer.Schemas;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

// mixin to enable lazy dfu loading
// mod that does this on its own and where i got the shit from
// https://github.com/astei/lazydfu
@Mixin(Schemas.class)
public class SchemasMixin {
    @Redirect(method = "create", at = @At(value = "NEW", target = "com/mojang/datafixers/DataFixerBuilder"))
    private static DataFixerBuilder create$replaceBuilder(int dataVersion) {
        return new LazyDataFixerBuilder(dataVersion);
    }
}
