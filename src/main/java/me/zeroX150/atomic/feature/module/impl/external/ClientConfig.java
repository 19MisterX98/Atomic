package me.zeroX150.atomic.feature.module.impl.external;

import me.zeroX150.atomic.feature.module.Module;
import me.zeroX150.atomic.feature.module.ModuleType;
import me.zeroX150.atomic.feature.module.config.BooleanValue;
import me.zeroX150.atomic.feature.module.config.DynamicValue;
import net.minecraft.client.util.math.MatrixStack;

public class ClientConfig extends Module {
    public static DynamicValue<String> chatPrefix;
    public static BooleanValue customMainMenu;
    public static BooleanValue customButtons;

    public ClientConfig() {
        super("ClientConfig", "config for da client", ModuleType.MISC);
        chatPrefix = this.config.create("Chat prefix", ".").description("The prefix used in chat to issue commands");
        customMainMenu = (BooleanValue) this.config.create("Custom main menu", true).description("Shows a custom home screen");
        customButtons = (BooleanValue) this.config.create("Custom buttons",true).description("Whether or not to render the custom client buttons");
    }

    @Override
    public void tick() {
    }

    @Override
    public void enable() {

    }

    @Override
    public void disable() {

    }

    @Override
    public String getContext() {
        return null;
    }

    @Override
    public void onWorldRender(MatrixStack matrices) {

    }

    @Override
    public void onHudRender() {

    }
}

