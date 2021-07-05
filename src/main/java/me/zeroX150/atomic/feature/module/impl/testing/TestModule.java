package me.zeroX150.atomic.feature.module.impl.testing;

import me.zeroX150.atomic.feature.module.Module;
import me.zeroX150.atomic.feature.module.ModuleType;
import net.minecraft.client.util.math.MatrixStack;

public class TestModule extends Module {
    public TestModule() {
        super("Test", "The dupe has been moved over to Dupe:.d 2 btw", ModuleType.MISC);
    }

    @Override
    public void tick() {
    }

    @Override
    public void enable() {
        setEnabled(false);


    }

    @Override
    public void disable() {

    }

    @Override
    public String getContext() {
        return "amog stuff";
    }

    @Override
    public void onWorldRender(MatrixStack matrices) {
    }

    @Override
    public void onHudRender() {

    }
}
