package me.zeroX150.atomic.feature.module.impl.render;

import me.zeroX150.atomic.Atomic;
import me.zeroX150.atomic.feature.module.Module;
import me.zeroX150.atomic.feature.module.ModuleType;
import me.zeroX150.atomic.feature.module.config.SliderValue;
import me.zeroX150.atomic.helper.Renderer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChestESP extends Module {
    SliderValue range = (SliderValue) this.config.create("Range", 30, 10, 100, 0).description("The range to scan for chests in");
    List<RenderBlock> blocksToRender = new ArrayList<>();
    Map<Block, Color> renders = new HashMap<>();
    Thread updater;
    int t = 0;

    public ChestESP() {
        super("ChestESP", "shows all chests", ModuleType.RENDER);
        renders.put(Blocks.CHEST, new Color(1, 161, 182));
        renders.put(Blocks.ENDER_CHEST, new Color(83, 3, 196));
        renders.put(Blocks.HOPPER, new Color(47, 66, 62));
        renders.put(Blocks.DROPPER, new Color(52, 52, 52));
        renders.put(Blocks.DISPENSER, new Color(52, 52, 52));
        Color shulker = new Color(0, 130, 76);
        for (Block b : new Block[]{ // jesus fuck
                Blocks.SHULKER_BOX,
                Blocks.WHITE_SHULKER_BOX,
                Blocks.ORANGE_SHULKER_BOX,
                Blocks.MAGENTA_SHULKER_BOX,
                Blocks.LIGHT_BLUE_SHULKER_BOX,
                Blocks.YELLOW_SHULKER_BOX,
                Blocks.LIME_SHULKER_BOX,
                Blocks.PINK_SHULKER_BOX,
                Blocks.GRAY_SHULKER_BOX,
                Blocks.LIGHT_GRAY_SHULKER_BOX,
                Blocks.CYAN_SHULKER_BOX,
                Blocks.PURPLE_SHULKER_BOX,
                Blocks.BLUE_SHULKER_BOX,
                Blocks.BROWN_SHULKER_BOX,
                Blocks.GREEN_SHULKER_BOX,
                Blocks.RED_SHULKER_BOX,
                Blocks.BLACK_SHULKER_BOX
        })
            renders.put(b, shulker);
        updater = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(50);
                    if (!this.isEnabled()) continue;
                    if (Atomic.client.player == null || Atomic.client.world == null) continue;
                    t++;
                    if (t > 40) { // scan every 2 secs on another thread to avoid lag
                        scan();
                        t = 0;
                    }
                } catch (Exception ignored) {
                }
            }
        }, "chestesp-updater");
        updater.start();
    }

    void scan() {
        List<RenderBlock> cache = new ArrayList<>();
        double rangeMid = range.getValue() / 2;
        for (double y = -rangeMid; y < rangeMid; y++) {
            for (double x = -rangeMid; x < rangeMid; x++) {
                for (double z = -rangeMid; z < rangeMid; z++) {
                    Vec3d pos = new Vec3d(x, y, z);
                    BlockPos bp = new BlockPos(pos);
                    BlockPos bp1 = Atomic.client.player.getBlockPos().add(bp);
                    if (bp1.getY() < 0 || bp1.getY() > 255) break;
                    BlockState state = Atomic.client.world.getBlockState(bp1);
                    if (getBlock(state.getBlock()) != null) {
                        cache.add(new RenderBlock(bp1, state.getBlock()));
                    }
                }
            }
        }
        blocksToRender.clear();
        blocksToRender.addAll(cache);
        cache.clear();
    }

    Color getBlock(Block b) {
        return renders.get(b);
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
        for (RenderBlock blockPos : blocksToRender.toArray(new RenderBlock[0])) {
            Vec3d v = new Vec3d(blockPos.bp.getX(), blockPos.bp.getY(), blockPos.bp.getZ());
            Color c = getBlock(blockPos.b);
            double d = v.distanceTo(Atomic.client.player.getPos());
            if (d < 10) {
                int r = (int) Math.floor(d / 10d * 255d);
                c = Renderer.modify(c, -1, -1, -1, r);
            }
            Renderer.renderFilled(v, new Vec3d(1, 1, 1), c, matrices);
        }
    }

    @Override
    public void onHudRender() {

    }

    record RenderBlock(BlockPos bp, Block b) {

    }
}

