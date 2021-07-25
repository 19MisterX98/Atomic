package me.zeroX150.atomic.feature.module.impl.world;

import me.zeroX150.atomic.Atomic;
import me.zeroX150.atomic.feature.gui.notifications.Notification;
import me.zeroX150.atomic.feature.module.Module;
import me.zeroX150.atomic.feature.module.ModuleType;
import me.zeroX150.atomic.feature.module.config.BooleanValue;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

import java.util.Arrays;

public class GodBridge extends Module {
    BooleanValue courseCorrect = (BooleanValue) this.config.create("Course correct", true).description("Prevent you from falling off the track by accident");
    float mOffset = 0.20f;
    Direction[] allowedSides = new Direction[]{
            Direction.NORTH,
            Direction.EAST,
            Direction.SOUTH,
            Direction.WEST
    };

    public GodBridge() {
        super("GodBridge", "YOOOO MF HAS SKILL!!!!", ModuleType.WORLD);
    }

    boolean isReady() {
        return Atomic.client.player.getPitch() > 82;
    }

    @Override
    public void tick() {

    }

    @Override
    public void enable() {
        Notification.create(5000, "GodBridge", "Look down, as you would normally while godbridging to start");
    }

    @Override
    public void disable() {

    }

    @Override
    public String getContext() {
        return isReady() ? "Ready" : "Not ready";
    }

    @Override
    public void onWorldRender(MatrixStack matrices) {

    }

    @Override
    public void onHudRender() {

    }

    @Override
    public void onFastTick() {
        if (!isReady()) return;
        Atomic.client.player.setYaw(Atomic.client.player.getMovementDirection().asRotation());
        if (Atomic.client.player.getPitch() > 83) Atomic.client.player.setPitch(82.5f);
        HitResult hr = Atomic.client.crosshairTarget;
        if (hr.getType() == HitResult.Type.BLOCK && hr instanceof BlockHitResult result) {
            if (Arrays.stream(allowedSides).anyMatch(direction -> direction == result.getSide())) {
                Atomic.client.player.swingHand(Hand.MAIN_HAND);
                Atomic.client.interactionManager.interactBlock(Atomic.client.player, Atomic.client.world, Hand.MAIN_HAND, result);
            }
        }
        if (!courseCorrect.getValue()) return;
        Vec3d ppos = Atomic.client.player.getPos();
        Vec3d isolated = new Vec3d(ppos.x - Math.floor(ppos.x), 0, ppos.z - Math.floor(ppos.z));
        double toCheck = 0;
        switch (Atomic.client.player.getMovementDirection()) {
            case NORTH, SOUTH -> toCheck = isolated.x;
            case EAST, WEST -> toCheck = isolated.z;
        }
        Atomic.client.options.keySneak.setPressed(toCheck > 0.5 + mOffset || toCheck < 0.5 - mOffset);
    }

}

