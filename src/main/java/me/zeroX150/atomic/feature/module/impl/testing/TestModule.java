package me.zeroX150.atomic.feature.module.impl.testing;

import me.zeroX150.atomic.Atomic;
import me.zeroX150.atomic.feature.module.Module;
import me.zeroX150.atomic.feature.module.ModuleType;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;

public class TestModule extends Module {
    Vec3d ePos;

    public TestModule() {
        super("Test", "among", ModuleType.MISC);
    }

    @Override
    public void tick() {
    }

    @Override
    public void enable() {
        ePos = Atomic.client.player.getPos();
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
        Atomic.client.textRenderer.draw(new MatrixStack(), "Screen: " + Atomic.client.currentScreen, 1, 1, 0xFFFFFF);
        if (System.currentTimeMillis() % 1000 == 0) {
            System.out.println(Atomic.client.currentScreen);
        }
    }
}
