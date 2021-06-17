package me.zeroX150.atomic.feature.module.impl.world;

import com.google.common.collect.Lists;
import me.zeroX150.atomic.Atomic;
import me.zeroX150.atomic.feature.module.Module;
import me.zeroX150.atomic.feature.module.ModuleType;
import net.minecraft.block.Block;
import net.minecraft.block.OreBlock;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.registry.Registry;

import java.util.List;

public class XRAY extends Module {
    public static List<Block> blocks = Lists.newArrayList();

    public XRAY() {
        super("XRAY", "me when the when the me", ModuleType.WORLD);
        Registry.BLOCK.forEach(block -> {
            if (block instanceof OreBlock) blocks.add(block);
        });
    }

    @Override
    public void tick() {

    }

    @Override
    public void enable() {
        Atomic.client.worldRenderer.reload();
    }

    @Override
    public void disable() {
        Atomic.client.worldRenderer.reload();
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

