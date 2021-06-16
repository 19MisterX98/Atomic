package me.zeroX150.atomic.feature.module.impl.world;

import me.zeroX150.atomic.Atomic;
import me.zeroX150.atomic.feature.module.Module;
import me.zeroX150.atomic.feature.module.ModuleType;
import me.zeroX150.atomic.feature.module.config.SliderValue;
import me.zeroX150.atomic.helper.Client;
import me.zeroX150.atomic.helper.Renderer;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;

public class Nuker extends Module {
    List<BlockPos> renders = new ArrayList<>();
    SliderValue range = this.config.create("Range", 3, 0, 4, 1);
    SliderValue blocksPerTick = this.config.create("Blocks per tick", 1, 1, 20, 0);
    SliderValue delay = this.config.create("Delay", 5, 0, 20, 0);
    int delayPassed = 0;

    public Nuker() {
        super("Nuker", "nukes the place", ModuleType.WORLD);
    }

    @Override
    public void tick() {
        if (delayPassed < delay.getValue()) {
            delayPassed++;
            return;
        }
        delayPassed = 0;
        BlockPos ppos1 = Atomic.client.player.getBlockPos();
        int blocksBroken = 0;
        renders.clear();
        for (double y = range.getValue(); y > -range.getValue() - 1; y--) {
            for (double x = -range.getValue(); x < range.getValue() + 1; x++) {
                for (double z = -range.getValue(); z < range.getValue() + 1; z++) {
                    if (blocksBroken >= blocksPerTick.getValue()) break;
                    BlockPos vp = new BlockPos(x, y, z);
                    BlockPos np = ppos1.add(vp);
                    Vec3d vp1 = new Vec3d(np.getX(), np.getY(), np.getZ());
                    if (vp1.distanceTo(Atomic.client.player.getPos()) >= Atomic.client.interactionManager.getReachDistance() - 0.2)
                        continue;
                    BlockState bs = Atomic.client.world.getBlockState(np);
                    if (!bs.isAir() && bs.getBlock() != Blocks.WATER && bs.getBlock() != Blocks.LAVA && bs.getBlock() != Blocks.BEDROCK) {
                        renders.add(np);
                        Atomic.client.getNetworkHandler().sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.START_DESTROY_BLOCK, np, Direction.DOWN));
                        Atomic.client.getNetworkHandler().sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.STOP_DESTROY_BLOCK, np, Direction.DOWN));
                        blocksBroken++;
                    }
                }
            }
        }
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
        for (BlockPos render : renders) {
            Vec3d vp = new Vec3d(render.getX(), render.getY(), render.getZ());
            Renderer.renderFilled(vp, new Vec3d(1, 1, 1), Client.getCurrentRGB(), matrices);
        }
    }

    @Override
    public void onHudRender() {

    }
}

