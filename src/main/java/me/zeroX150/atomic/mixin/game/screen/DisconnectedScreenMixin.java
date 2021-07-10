package me.zeroX150.atomic.mixin.game.screen;

import me.zeroX150.atomic.Atomic;
import net.minecraft.client.gui.screen.ConnectScreen;
import net.minecraft.client.gui.screen.DisconnectedScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.network.ServerAddress;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DisconnectedScreen.class)
public class DisconnectedScreenMixin extends Screen {
    @Shadow
    private int reasonHeight;

    @Shadow
    @Final
    private Screen parent;

    public DisconnectedScreenMixin() {
        super(Text.of(""));
    }

    @Inject(method = "init", at = @At("TAIL"))
    public void init(CallbackInfo ci) {
        int var10004 = this.height / 2 + this.reasonHeight / 2;
        ButtonWidget reconnect = new ButtonWidget(this.width / 2 - 100, Math.min(var10004 + 9, this.height - 30) + 25, 200, 20, Text.of("Reconnect"), button -> ConnectScreen.connect(this.parent, Atomic.client, ServerAddress.parse(Atomic.client.getCurrentServerEntry().address), Atomic.client.getCurrentServerEntry()));
        addDrawableChild(reconnect);
    }
}
