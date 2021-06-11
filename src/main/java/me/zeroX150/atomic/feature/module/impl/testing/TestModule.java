package me.zeroX150.atomic.feature.module.impl.testing;

import me.zeroX150.atomic.feature.module.Module;
import me.zeroX150.atomic.feature.module.ModuleType;
import net.minecraft.client.util.math.MatrixStack;

public class TestModule extends Module {
    public TestModule() {
        super("Test", "among", ModuleType.TEST);
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
        //Renderer.lineScreen(Color.GREEN,new Point(0,10), new Point(10,20), new Point(20,50), new Point(60,100));
    }
}
