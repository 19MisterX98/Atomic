package me.zeroX150.atomic.feature.module.impl.world;

import me.zeroX150.atomic.Atomic;
import me.zeroX150.atomic.feature.module.Module;
import me.zeroX150.atomic.feature.module.ModuleType;
import me.zeroX150.atomic.feature.module.config.SliderValue;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CropBlock;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

import java.util.Arrays;

public class FarmingAura extends Module {
    SliderValue max = this.config.create("Blocks per tick", 3, 1, 20, 0);

    public FarmingAura() {
        super("FarmingAura", "farms", ModuleType.WORLD);
    }

    @Override
    public void tick() {
        int r = 4;
        int i = 0;
        double max = this.max.getValue();
        BlockPos ppos = Atomic.client.player.getBlockPos();
        Vec3d ppos1 = Atomic.client.player.getPos();
        for (int x = -r; x < r + 1; x++) {
            if (i > max) break;
            for (int y = -r; y < r + 1; y++) {
                if (i > max) break;
                for (int z = -r; z < r + 1; z++) {
                    if (i > max) break;
                    Vec3d off = new Vec3d(x, y, z);
                    Vec3d poff = ppos1.add(off);
                    if (poff.distanceTo(ppos1) >= Atomic.client.interactionManager.getReachDistance()) continue;
                    BlockPos c = ppos.add(x, y, z);
                    BlockState s = Atomic.client.world.getBlockState(c);
                    if (s.getBlock() instanceof CropBlock p) {
                        if (p.isMature(s)) {
                            i++;
                            Atomic.client.interactionManager.attackBlock(c, Direction.DOWN);
                        }
                    }
                }
            }
        }
        Item[] V = new Item[]{
                Items.WHEAT_SEEDS,
                Items.BEETROOT_SEEDS,
                Items.MELON_SEEDS,
                Items.PUMPKIN_SEEDS,
                Items.CARROT, Items.POTATO
        };
        if (Arrays.stream(V).toList().contains(Atomic.client.player.getInventory().getMainHandStack().getItem())) {
            i = 0;
            for (int x = -r; x < r + 1; x++) {
                if (i > max) break;
                for (int y = -r; y < r + 1; y++) {
                    if (i > max) break;
                    for (int z = -r; z < r + 1; z++) {
                        if (i > max) break;
                        Vec3d off = new Vec3d(x, y, z);
                        Vec3d poff = ppos1.add(off);
                        if (poff.distanceTo(ppos1) >= Atomic.client.interactionManager.getReachDistance()) continue;
                        BlockPos c = ppos.add(x, y, z);
                        BlockState s = Atomic.client.world.getBlockState(c);
                        if (s.getBlock() == Blocks.FARMLAND) {
                            if (!Atomic.client.world.getBlockState(c.up()).isAir()) continue;
                            i++;
                            BlockHitResult bhr = new BlockHitResult(poff.add(0, 1, 0), Direction.DOWN, c.add(0, 1, 0), false);
                            Atomic.client.interactionManager.interactBlock(Atomic.client.player, Atomic.client.world, Hand.MAIN_HAND, bhr);
                        }
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

    }

    @Override
    public void onHudRender() {

    }
}

