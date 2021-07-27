package me.zeroX150.atomic.feature.module.impl.external;

import me.zeroX150.atomic.Atomic;
import me.zeroX150.atomic.feature.module.Module;
import me.zeroX150.atomic.feature.module.ModuleType;
import me.zeroX150.atomic.feature.module.config.BooleanValue;
import me.zeroX150.atomic.helper.event.Events;
import me.zeroX150.atomic.helper.event.RenderingEvents;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EntityType;

import java.util.Arrays;

public class NoRender extends Module {
    public static BooleanValue weather;
    public static BooleanValue hurtAnimation;
    BooleanValue items = (BooleanValue) this.config.create("Items", false).description("Doesnt render items");
    BooleanValue trapdoors = (BooleanValue) this.config.create("Trapdoors", false).description("Doesnt render trapdoors");

    public NoRender() {
        super("NoRender", "doesnt render shit", ModuleType.RENDER);
        Events.Rendering.registerEventHandler(RenderingEvents.ENTITY_RENDER, event -> {
            if (!this.isEnabled() || !items.getValue()) return;
            if (event.getEntityTarget().getType() == EntityType.ITEM) {
                event.setCancelled(true);
            }
        });
        Block[] trapdoors = new Block[]{
                Blocks.ACACIA_TRAPDOOR,
                Blocks.BIRCH_TRAPDOOR,
                Blocks.CRIMSON_TRAPDOOR,
                Blocks.DARK_OAK_TRAPDOOR,
                Blocks.IRON_TRAPDOOR,
                Blocks.JUNGLE_TRAPDOOR, Blocks.OAK_TRAPDOOR,
                Blocks.SPRUCE_TRAPDOOR,
                Blocks.WARPED_TRAPDOOR
        };
        Events.Rendering.registerEventHandler(RenderingEvents.BLOCK_RENDER, event -> {
            if (Arrays.stream(trapdoors).anyMatch(block -> block == event.getState().getBlock()) && this.trapdoors.getValue() && this.isEnabled()) {
                event.setCancelled(true);
            }
        });
        weather = (BooleanValue) this.config.create("Weather", true).description("Doesnt render weather");
        hurtAnimation = (BooleanValue) this.config.create("Hurt animation", true).description("Doesnt render the hurt animation");
    }

    @Override
    public void tick() {

    }

    @Override
    public void enable() {
        disable();
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

