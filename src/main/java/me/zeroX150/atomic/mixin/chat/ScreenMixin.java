package me.zeroX150.atomic.mixin.chat;

import me.zeroX150.atomic.Atomic;
import me.zeroX150.atomic.feature.command.Command;
import me.zeroX150.atomic.feature.command.CommandRegistry;
import me.zeroX150.atomic.helper.Client;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;

@Mixin(Screen.class)
public class ScreenMixin {
    @Shadow
    @Nullable
    protected MinecraftClient client;

    @Inject(method = "sendMessage(Ljava/lang/String;Z)V", at = @At("HEAD"), cancellable = true)
    public void sendMessage(String message, boolean toHud, CallbackInfo ci) {
        if (message.toLowerCase().startsWith(Atomic.chatPrefix.toLowerCase())) {
            ci.cancel();
            this.client.inGameHud.getChatHud().addToMessageHistory(message);
            String[] args = message.substring(Atomic.chatPrefix.length()).split(" +");
            String command = args[0].toLowerCase();
            args = Arrays.copyOfRange(args, 1, args.length);
            Command c = CommandRegistry.getByAlias(command);
            if (c == null) Client.notifyUser("Command not found.");
            else {
                Client.notifyUser(command);
                c.onExecute(args);
            }
        }
    }
}
