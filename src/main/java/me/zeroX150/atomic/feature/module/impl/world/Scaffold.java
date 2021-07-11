package me.zeroX150.atomic.feature.module.impl.world;

import me.zeroX150.atomic.Atomic;
import me.zeroX150.atomic.feature.module.Module;
import me.zeroX150.atomic.feature.module.ModuleType;
import me.zeroX150.atomic.feature.module.config.SliderValue;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

public class Scaffold extends Module {
    SliderValue extend = this.config.create("Extend", 3, 0, 5, 1);

    public Scaffold() {
        super("Scaffold", "scaffold", ModuleType.WORLD);
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
        Vec3d ppos = Atomic.client.player.getPos().add(0, -1, 0);
        BlockPos bp = new BlockPos(ppos);
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
            Atomic.client.execute(() -> placeBlockWithSlot(finalSelIndex, bp));
            if (extend.getValue() != 0) {
                Vec3d dir1 = Atomic.client.player.getRotationVector();
                Vec3d dir = new Vec3d(dir1.x, 0, dir1.z);
                Vec3d v = ppos;
                for (double i = 0; i < extend.getValue(); i += 0.5) {
                    v = v.add(dir);
                    if (v.distanceTo(Atomic.client.player.getPos()) >= Atomic.client.interactionManager.getReachDistance())
                        break;
                    BlockPos bp1 = new BlockPos(v);
                    Atomic.client.execute(() -> placeBlockWithSlot(finalSelIndex, bp1));
                }

            }
        }
    }

    void placeBlockWithSlot(int s, BlockPos bp) {
        BlockState st = Atomic.client.world.getBlockState(bp);
        if (!st.getMaterial().isReplaceable()) return;
        int c = Atomic.client.player.getInventory().selectedSlot;
        Atomic.client.player.getInventory().selectedSlot = s;
        BlockHitResult bhr = new BlockHitResult(new Vec3d(bp.getX(), bp.getY(), bp.getZ()), Direction.DOWN, bp, false);
        Atomic.client.interactionManager.interactBlock(Atomic.client.player, Atomic.client.world, Hand.MAIN_HAND, bhr);
        Atomic.client.player.getInventory().selectedSlot = c;
    }
}
