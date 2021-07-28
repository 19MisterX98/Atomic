package me.zeroX150.atomic.feature.module.impl.external;

import me.zeroX150.atomic.Atomic;
import me.zeroX150.atomic.feature.module.Module;
import me.zeroX150.atomic.feature.module.ModuleType;
import net.minecraft.client.util.math.MatrixStack;

public class AntiReducedDebugInfo extends Module {
    public AntiReducedDebugInfo() {
        super("AntiReducedDebugInfo", "why would this even be enabled", ModuleType.EXPLOIT);
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
        boolean origDebugInfoReduce = Atomic.client.player.hasReducedDebugInfo() || Atomic.client.options.reducedDebugInfo;
        return origDebugInfoReduce ? "Active!" : null;
    }

    @Override
    public void onWorldRender(MatrixStack matrices) {

    }

    @Override
    public void onHudRender() {

    }
}

