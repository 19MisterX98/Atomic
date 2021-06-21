package me.zeroX150.atomic.mixin.game.screen;

import me.zeroX150.atomic.Atomic;
import me.zeroX150.atomic.feature.gui.screen.ItemExploitsScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CreativeInventoryScreen.class)
public class CreativeInventoryScreenMixin extends Screen {
    protected CreativeInventoryScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "init", at = @At("TAIL"))
    public void init(CallbackInfo ci) {
        ButtonWidget bw = new ButtonWidget(1, 1, 150, 20, Text.of("Item exploits"), button -> {
            Atomic.client.openScreen(new ItemExploitsScreen());
        });
        addDrawableChild(bw);
    }
}
