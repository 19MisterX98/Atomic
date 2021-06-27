package me.zeroX150.atomic.feature.module.impl.world;

import me.zeroX150.atomic.Atomic;
import me.zeroX150.atomic.feature.module.Module;
import me.zeroX150.atomic.feature.module.ModuleType;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class WaterClutch extends Module {
    public WaterClutch() {
        super("WaterClutch", "YOOOO LOOK AT THAT SKILLZ BRUHH", ModuleType.WORLD);
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

    }

    @Override
    public void onFastTick() {
        Vec3d ppos = Atomic.client.player.getPos();
        Vec3d np = ppos.subtract(0, MathHelper.clamp(-Atomic.client.player.getVelocity().getY(), 0, 6), 0);
        BlockPos bp = new BlockPos(np);
        BlockState bs = Atomic.client.world.getBlockState(bp);
        if (bs.getFluidState().getLevel() == 0) return;
        int selIndex = Atomic.client.player.getInventory().selectedSlot;
        if (!(Atomic.client.player.getInventory().getStack(selIndex).getItem() instanceof BlockItem))
            for (int i = 0; i < 9; i++) {
                ItemStack is = Atomic.client.player.getInventory().getStack(i);
                if (is.getItem() == Items.AIR) continue;
                if (is.getItem() instanceof BlockItem) {
                    selIndex = i;
                    break;
                }
            }
        if (Atomic.client.player.getInventory().getStack(selIndex).getItem() != Items.AIR) {
            // fucking multithreading moment
            int finalSelIndex = selIndex;
            Atomic.client.execute(() -> {
                int c = Atomic.client.player.getInventory().selectedSlot;
                Atomic.client.player.getInventory().selectedSlot = finalSelIndex;
                BlockHitResult bhr = new BlockHitResult(np, Direction.DOWN, bp, false);
                Atomic.client.interactionManager.interactBlock(Atomic.client.player, Atomic.client.world, Hand.MAIN_HAND, bhr);
                Atomic.client.player.getInventory().selectedSlot = c;
            });
        }
    }
}

