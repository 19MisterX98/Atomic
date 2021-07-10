package me.zeroX150.atomic.mixin.game.screen;

import me.zeroX150.atomic.Atomic;
import me.zeroX150.atomic.feature.gui.screen.EditServerInfoScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerServerListWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MultiplayerScreen.class)
public class MultiplayerScreenMixin extends Screen {
    @Shadow
    protected MultiplayerServerListWidget serverListWidget;
    ButtonWidget editMotd;

    public MultiplayerScreenMixin() {
        super(Text.of(""));
    }

    @Inject(method = "init", at = @At("HEAD"))
    public void init(CallbackInfo ci) {
        editMotd = new ButtonWidget(8, height - 28, 100, 20, Text.of("Edit MOTD"), button -> {
            MultiplayerServerListWidget.ServerEntry se = (MultiplayerServerListWidget.ServerEntry) this.serverListWidget.getSelectedOrNull();
            Atomic.client.openScreen(new EditServerInfoScreen(se.getServer(), this));
        });
        addDrawableChild(editMotd);
    }

    @Inject(method = "render", at = @At("HEAD"))
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        MultiplayerServerListWidget.Entry e = this.serverListWidget.getSelectedOrNull();
        editMotd.active = (e instanceof MultiplayerServerListWidget.ServerEntry);
    }
}
