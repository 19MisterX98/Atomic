package me.zeroX150.atomic.feature.module.impl.testing;

import me.zeroX150.atomic.Atomic;
import me.zeroX150.atomic.feature.module.Module;
import me.zeroX150.atomic.feature.module.ModuleType;
import me.zeroX150.atomic.helper.Client;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.network.packet.c2s.play.BookUpdateC2SPacket;

import java.util.ArrayList;

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
