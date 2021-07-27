package me.zeroX150.atomic.feature.module.impl.render;

import me.zeroX150.atomic.Atomic;
import me.zeroX150.atomic.feature.module.Module;
import me.zeroX150.atomic.feature.module.ModuleType;
import me.zeroX150.atomic.helper.Renderer;
import me.zeroX150.atomic.helper.event.Events;
import me.zeroX150.atomic.helper.event.RenderingEvents;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChestESP extends Module {
    List<RenderBlock> blocksToRender = new ArrayList<>();
    Map<Block, Color> renders = new HashMap<>();

    public ChestESP() {
        super("ChestESP", "shows all chests", ModuleType.RENDER);
        renders.put(Blocks.CHEST, new Color(1, 161, 182));
        renders.put(Blocks.ENDER_CHEST, new Color(83, 3, 196));
        renders.put(Blocks.HOPPER, new Color(0, 255, 209));
        renders.put(Blocks.DROPPER, new Color(255, 221, 0));
        renders.put(Blocks.DISPENSER, new Color(255, 221, 0));
        Color shulker = new Color(0, 255, 64);
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
        /*updater = new Thread(() -> {
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
        }, "chestesp-updater");*/

        Events.Rendering.registerEventHandler(RenderingEvents.BLOCK_ENTITY_RENDER, event -> {
            BlockState bs = event.getBlockEntityTarget().getCachedState();
            Block b = bs.getBlock();
            Color c = getBlock(b);
            if (c == null) return;
            BlockPos bp = event.getBlockEntityTarget().getPos();
            blocksToRender.add(new RenderBlock(bp, b, bs));
        });

        //updater.start();
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
            Renderer.renderVoxOutline(v, c, matrices, blockPos.bs.getOutlineShape(Atomic.client.world, blockPos.bp, ShapeContext.of(Atomic.client.player)));
        }
        blocksToRender.clear();
    }

    @Override
    public void onHudRender() {

    }

    record RenderBlock(BlockPos bp, Block b, BlockState bs) {

    }
}

