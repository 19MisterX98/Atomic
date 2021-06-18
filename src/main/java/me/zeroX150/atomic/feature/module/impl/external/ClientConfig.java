package me.zeroX150.atomic.feature.module.impl.external;

import me.zeroX150.atomic.feature.module.Module;
import me.zeroX150.atomic.feature.module.ModuleType;
import me.zeroX150.atomic.feature.module.config.DynamicValue;
import net.minecraft.client.util.math.MatrixStack;

public class ClientConfig extends Module {
    public static DynamicValue<String> chatPrefix;

    public ClientConfig() {
        super("ClientConfig", "No description", ModuleType.MISC);
        chatPrefix = this.config.create("Chat prefix", ".");
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

