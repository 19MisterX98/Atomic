package me.zeroX150.atomic.feature.module.impl.external;

import me.zeroX150.atomic.Atomic;
import me.zeroX150.atomic.feature.gui.notifications.Notification;
import me.zeroX150.atomic.feature.module.Module;
import me.zeroX150.atomic.feature.module.ModuleType;
import me.zeroX150.atomic.feature.module.config.SliderValue;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;

public class SlotSpammer extends Module {
    public static Slot slotToSpam = null;
    SliderValue amountPerTick = this.config.create("Amount / tick", 5, 1, 100, 0);

    public SlotSpammer() {
        super("SlotSpammer", "Spams slots", ModuleType.MISC);
    }

    @Override
    public void tick() {
        if (slotToSpam == null) return;
        if (!(Atomic.client.currentScreen instanceof HandledScreen)) {
            this.setEnabled(false);
            return;
        }
        for (int i = 0; i < amountPerTick.getValue(); i++) {
            Atomic.client.interactionManager.clickSlot(((HandledScreen) Atomic.client.currentScreen).getScreenHandler().syncId, slotToSpam.id, 0, SlotActionType.PICKUP, Atomic.client.player);
        }
    }

    @Override
    public void enable() {
        if (slotToSpam == null) {
            Notification.create(6000, "Slot spammer", "Please only enable via the inventory");
            this.setEnabled(false);
        }
    }

    @Override
    public void disable() {
        slotToSpam = null;
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

