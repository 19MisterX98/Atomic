package me.zeroX150.atomic.mixin.network;

import net.minecraft.client.network.MultiplayerServerListPinger;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(MultiplayerServerListPinger.class)
public interface MultiplayerServerListPingerAccessor {
    @Invoker("createPlayerCountText")
    static Text createPlayerCountText(int current, int max) {
        throw new RuntimeException("untransformed mixin!");
    }
}
