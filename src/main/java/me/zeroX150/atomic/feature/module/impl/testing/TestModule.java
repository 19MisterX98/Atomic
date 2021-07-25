package me.zeroX150.atomic.feature.module.impl.testing;

import me.zeroX150.atomic.Atomic;
import me.zeroX150.atomic.feature.module.Module;
import me.zeroX150.atomic.feature.module.ModuleType;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.listener.ServerPlayPacketListener;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import java.util.Date;

public class TestModule extends Module {
    public TestModule() {
        super("Test", "The dupe has been moved over to Dupe:.d 2 btw", ModuleType.MISC);
    }

    @Override
    public void tick() {
        Atomic.client.getNetworkHandler().sendPacket(new Packet<ServerPlayPacketListener>() {
            @Override
            public void write(PacketByteBuf buf) {
                buf.writeDate(new Date(-0xFFFFFF));
                buf.writeBlockHitResult(new BlockHitResult(Atomic.client.player.getPos(), Direction.DOWN, new BlockPos(0xFFFFFF, 0xFFFFFF, 0xFFFFFF), true));
            }

            @Override
            public void apply(ServerPlayPacketListener listener) {

            }
        });
    }

    @Override
    public void enable() {
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

    @Override
    public void onFastTick() {

    }
}
