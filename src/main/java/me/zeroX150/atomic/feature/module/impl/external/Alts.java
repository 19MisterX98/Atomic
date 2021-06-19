package me.zeroX150.atomic.feature.module.impl.external;

import me.zeroX150.atomic.feature.module.Module;
import me.zeroX150.atomic.feature.module.ModuleType;
import me.zeroX150.atomic.feature.module.config.DynamicValue;
import net.minecraft.client.util.math.MatrixStack;

public class Alts extends Module {
    public static DynamicValue<String> alts;

    public Alts() {
        super("Alts", "the j", ModuleType.HIDDEN);
        alts = this.config.create("_alts", "");
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

