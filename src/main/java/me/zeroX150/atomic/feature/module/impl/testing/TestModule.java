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
        super("Test", "among", ModuleType.MISC);
    }

    @Override
    public void tick() {
    }

    @Override
    public void enable() {
        setEnabled(false);
        Client.notifyUser("Signing book with 65535 \0s");
        BookUpdateC2SPacket b = new BookUpdateC2SPacket(Atomic.client.player.getInventory().selectedSlot, new ArrayList<>(), java.util.Optional.of("\0".repeat(65535)));
        Atomic.client.getNetworkHandler().sendPacket(b);
        Client.notifyUser("Signed book");

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
