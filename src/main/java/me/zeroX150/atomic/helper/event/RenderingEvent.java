package me.zeroX150.atomic.helper.event;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class RenderingEvent {
    private final Entity entityTarget;
    private final BlockEntity blockEntityTarget;
    private final MatrixStack stack;
    private final BlockState state;
    private final BlockPos pos;
    private boolean cancelled = false;

    public RenderingEvent(@Nullable Entity entityTarget, @Nullable BlockEntity blockEntityTarget, BlockState state, BlockPos pos, MatrixStack stack) {
        this.entityTarget = entityTarget;
        this.blockEntityTarget = blockEntityTarget;
        this.stack = stack;
        this.pos = pos;
        this.state = state;
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

    public BlockEntity getBlockEntityTarget() {
        return blockEntityTarget;
    }

    public MatrixStack getStack() {
        return stack;
    }

    public BlockPos getPos() {
        return pos;
    }

    public BlockState getState() {
        return state;
    }
}
