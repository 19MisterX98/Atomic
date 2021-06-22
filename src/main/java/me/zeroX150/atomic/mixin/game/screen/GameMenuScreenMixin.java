package me.zeroX150.atomic.mixin.game.screen;


import me.zeroX150.atomic.Atomic;
import me.zeroX150.atomic.feature.gui.screen.ServerInfoScreen;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameMenuScreen.class) // who the fuck named it that way i searched like 20 minutes for this
public class GameMenuScreenMixin extends Screen {
    public GameMenuScreenMixin() {
        super(Text.of(""));
    }

    @Inject(method = "initWidgets", at = @At("RETURN"))
    public void init(CallbackInfo ci) {
        ButtonWidget bw = new ButtonWidget(2, 2, 150, 20, Text.of("Server info"), button -> Atomic.client.openScreen(new ServerInfoScreen()));
        this.addDrawableChild(bw);

    }
}
