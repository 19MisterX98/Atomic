package me.zeroX150.atomic.feature.module.impl.misc;

import me.zeroX150.atomic.Atomic;
import me.zeroX150.atomic.feature.module.Module;
import me.zeroX150.atomic.feature.module.ModuleType;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.util.Hand;

import java.lang.reflect.Field;

public class AutoFish extends Module {
    public AutoFish() {
        super("AutoFish", "fishes", ModuleType.MISC);
    }

    @Override
    public void tick() {
        FishingBobberEntity fishingBobberEntity = Atomic.client.player.fishHook;
        if (fishingBobberEntity == null)
            return;
        try {
            Field f = fishingBobberEntity.getClass().getDeclaredField("caughtFish");
            f.setAccessible(true);
            boolean caughtFish = f.getBoolean(fishingBobberEntity);
            if (caughtFish) {
                Atomic.client.interactionManager.interactItem(Atomic.client.player, Atomic.client.world,
                        Hand.MAIN_HAND);
                fishingBobberEntity.remove(Entity.RemovalReason.DISCARDED);
                new Thread(() -> {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Atomic.client.interactionManager.interactItem(Atomic.client.player, Atomic.client.world,
                            Hand.MAIN_HAND);
                }).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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

