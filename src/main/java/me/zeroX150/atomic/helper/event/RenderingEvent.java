package me.zeroX150.atomic.helper.event;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import org.jetbrains.annotations.Nullable;

public class RenderingEvent {
    private final Entity entityTarget;
    private final BlockState blockTarget;
    private boolean cancelled = false;

    public RenderingEvent(@Nullable Entity entityTarget, @Nullable BlockState blockTarget) {
        this.entityTarget = entityTarget;
        this.blockTarget = blockTarget;
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

    public BlockState getBlockTarget() {
        return blockTarget;
    }
}
