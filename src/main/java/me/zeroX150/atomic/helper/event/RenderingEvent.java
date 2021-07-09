package me.zeroX150.atomic.helper.event;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class RenderingEvent {
    private final Entity entityTarget;
    private final BlockTarget blockTarget;
    private final BlockEntity blockEntityTarget;
    private final MatrixStack stack;
    private boolean cancelled = false;

    public RenderingEvent(@Nullable Entity entityTarget, @Nullable BlockTarget blockTarget, @Nullable BlockEntity blockEntityTarget, MatrixStack stack) {
        this.entityTarget = entityTarget;
        this.blockTarget = blockTarget;
        this.blockEntityTarget = blockEntityTarget;
        this.stack = stack;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean isCancelled) {
        this.cancelled = isCancelled;
    }

    public Entity getEntityTarget() {
        return entityTarget;
    }

    public BlockTarget getBlockTarget() {
        return blockTarget;
    }

    public BlockEntity getBlockEntityTarget() {
        return blockEntityTarget;
    }

    public MatrixStack getStack() {
        return stack;
    }

    public static record BlockTarget(BlockState state, BlockPos position) {

    }
}
