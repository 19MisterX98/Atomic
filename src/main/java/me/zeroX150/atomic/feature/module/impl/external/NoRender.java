package me.zeroX150.atomic.feature.module.impl.external;

import me.zeroX150.atomic.feature.module.Module;
import me.zeroX150.atomic.feature.module.ModuleType;
import me.zeroX150.atomic.feature.module.config.BooleanValue;
import me.zeroX150.atomic.helper.event.Events;
import me.zeroX150.atomic.helper.event.RenderingEvents;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EntityType;

public class NoRender extends Module {
    public static BooleanValue weather;
    public static BooleanValue hurtAnimation;
    BooleanValue items = (BooleanValue) this.config.create("Items", false).description("Doesnt render items");

    public NoRender() {
        super("NoRender", "doesnt render shit", ModuleType.RENDER);
        Events.Rendering.registerEventHandler(RenderingEvents.ENTITY_RENDER, event -> {
            if (!this.isEnabled() || !items.getValue()) return;
            if (event.getEntityTarget().getType() == EntityType.ITEM) {
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

