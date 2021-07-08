package me.zeroX150.atomic.mixin.game.entity;

import me.zeroX150.atomic.feature.module.ModuleRegistry;
import me.zeroX150.atomic.feature.module.impl.external.NoBreakDelay;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ClientPlayerInteractionManager.class)
public class ClientPlayerInteractionManagerMixin {
    @Shadow
    private int blockBreakingCooldown;

    @Redirect(method = "updateBlockBreakingProgress", at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/client/network/ClientPlayerInteractionManager;blockBreakingCooldown:I",
            opcode = Opcodes.GETFIELD,
            ordinal = 0
    ))
    public int getCooldownOverride(ClientPlayerInteractionManager clientPlayerInteractionManager) {
        int cd = this.blockBreakingCooldown;
        return ModuleRegistry.getByClass(NoBreakDelay.class).isEnabled() ? 0 : cd;
        // always set cooldown to 0
        /*
         * if (this.blockBreakingCooldown > 0) {... return true;}
         * -> if (0 > 0) {
         * -> false
         * -> we continue, whatever happens
         * */
    }
}
