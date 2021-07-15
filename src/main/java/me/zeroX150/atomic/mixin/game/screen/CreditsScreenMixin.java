package me.zeroX150.atomic.mixin.game.screen;

import net.minecraft.client.gui.screen.CreditsScreen;
import net.minecraft.util.Identifier;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(CreditsScreen.class)
public class CreditsScreenMixin {
    Identifier nomodV = new Identifier("nomod.textures/gui/options_background.png");

    @Redirect(method = "renderBackground", at = @At(
            value = "FIELD",
            opcode = Opcodes.GETSTATIC,
            target = "Lnet/minecraft/client/gui/DrawableHelper;OPTIONS_BACKGROUND_TEXTURE:Lnet/minecraft/util/Identifier;"
    ))
    public Identifier redirect() {
        return nomodV;
    }
}
