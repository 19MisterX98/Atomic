package me.zeroX150.atomic.mixin.chat;

import com.mojang.blaze3d.systems.RenderSystem;
import me.zeroX150.atomic.Atomic;
import me.zeroX150.atomic.feature.command.Command;
import me.zeroX150.atomic.feature.command.CommandRegistry;
import me.zeroX150.atomic.feature.gui.screen.HomeScreen;
import me.zeroX150.atomic.feature.module.impl.external.ClientConfig;
import me.zeroX150.atomic.helper.Client;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;
import java.util.Arrays;

@Mixin(Screen.class)
public class ScreenMixin {
    @Shadow
    public int width;
    @Shadow
    public int height;
    @Shadow
    @Nullable
    protected MinecraftClient client;
    Identifier OPTIONS_BACKGROUND_TEXTURE = new Identifier("atomic", "background.jpg");

    @Inject(method = "sendMessage(Ljava/lang/String;Z)V", at = @At("HEAD"), cancellable = true)
    public void sendMessage(String message, boolean toHud, CallbackInfo ci) {
        if (this.client == null) return;
        if (message.toLowerCase().startsWith(ClientConfig.chatPrefix.getValue().toLowerCase())) {
            ci.cancel();
            this.client.inGameHud.getChatHud().addToMessageHistory(message);
            String[] args = message.substring(ClientConfig.chatPrefix.getValue().length()).split(" +");
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

    /**
     * @author 0x150
     * @reason your mother
     */
    @Inject(method = "renderBackgroundTexture", at = @At("HEAD"), cancellable = true)
    public void renderBackgroundTexture(int vOffset, CallbackInfo ci) {
        ci.cancel();
        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.setShaderTexture(0, OPTIONS_BACKGROUND_TEXTURE);
        Screen.drawTexture(new MatrixStack(), 0, 0, 0, 0, width, height, width, height);
        if (!(Atomic.client.currentScreen instanceof HomeScreen))
            DrawableHelper.fill(new MatrixStack(), 0, 0, width, height, new Color(0, 0, 0, 30).getRGB());
    }
}
