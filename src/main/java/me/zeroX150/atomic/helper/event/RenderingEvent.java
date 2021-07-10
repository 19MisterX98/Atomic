package me.zeroX150.atomic.helper.event;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import org.jetbrains.annotations.Nullable;

public class RenderingEvent {
    private final Entity entityTarget;
    private final BlockEntity blockEntityTarget;
    private final MatrixStack stack;
    private boolean cancelled = false;

    public RenderingEvent(@Nullable Entity entityTarget, @Nullable BlockEntity blockEntityTarget, MatrixStack stack) {
        this.entityTarget = entityTarget;
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

    public BlockEntity getBlockEntityTarget() {
        return blockEntityTarget;
    }

    public MatrixStack getStack() {
        return stack;
    }
}
