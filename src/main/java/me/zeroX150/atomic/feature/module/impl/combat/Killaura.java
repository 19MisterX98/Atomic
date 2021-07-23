package me.zeroX150.atomic.feature.module.impl.combat;

import com.google.common.util.concurrent.AtomicDouble;
import me.zeroX150.atomic.Atomic;
import me.zeroX150.atomic.feature.module.Module;
import me.zeroX150.atomic.feature.module.ModuleType;
import me.zeroX150.atomic.feature.module.config.BooleanValue;
import me.zeroX150.atomic.feature.module.config.MultiValue;
import me.zeroX150.atomic.feature.module.config.SliderValue;
import me.zeroX150.atomic.helper.Packets;
import me.zeroX150.atomic.helper.Rotations;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;

import java.util.ArrayList;
import java.util.List;

public class Killaura extends Module {
    BooleanValue capRangeAtMax = (BooleanValue) this.config.create("Max range", true).description("Whether or not to set the range to the max");
    SliderValue range = (SliderValue) this.config.create("Range", 3.2, 0.1, 7, 2).description("The range to select entities by");
    SliderValue delay = (SliderValue) this.config.create("Delay", 0, 0, 20, 0).description("The delay before attacking");
    BooleanValue automaticalDelay = (BooleanValue) this.config.create("Auto delay", true).description("Whether or not to automatically pick a delay based on your weapon");

    MultiValue mode = (MultiValue) this.config.create("Mode", "Single", "Single", "Multi").description("The mode");

    BooleanValue attackMobs = (BooleanValue) this.config.create("Attack mobs", true).description("Whether or not to attack mobs");
    BooleanValue attackPlayers = (BooleanValue) this.config.create("Attack players", true).description("Whether or not to attack players");

    int delayPassed = 0;

    public Killaura() {
        super("Killaura", "anime", ModuleType.COMBAT);
        range.showOnlyIf(() -> !capRangeAtMax.getValue());
        delay.showOnlyIf(() -> !automaticalDelay.getValue());
    }

    int getDelay() {
        if (Atomic.client.player == null) return 0;
        if (!automaticalDelay.getValue()) return (int) (delay.getValue() + 0);
        else {
            ItemStack hand = Atomic.client.player.getMainHandStack();
            if (hand == null) hand = Atomic.client.player.getOffHandStack();
            if (hand == null) return 10;
            hand.getTooltip(Atomic.client.player, TooltipContext.Default.ADVANCED);
            AtomicDouble speed = new AtomicDouble(Atomic.client.player.getAttributeBaseValue(EntityAttributes.GENERIC_ATTACK_SPEED));
            hand.getAttributeModifiers(EquipmentSlot.MAINHAND).forEach((entityAttribute, entityAttributeModifier) -> {
                if (entityAttribute == EntityAttributes.GENERIC_ATTACK_SPEED) {
                    speed.addAndGet(entityAttributeModifier.getValue());
                }
            });
            return (int) (20d / speed.get());
        }
    }

    double getRange() {
        if (Atomic.client.interactionManager == null) return 0;
        if (capRangeAtMax.getValue()) return Atomic.client.interactionManager.getReachDistance();
        else return range.getValue();
    }

    @Override
    public void tick() {
        if (Atomic.client.world == null || Atomic.client.player == null || Atomic.client.interactionManager == null)
            return;
        boolean delayHasPassed = true;
        if (delayPassed < getDelay()) {
            delayPassed++;
            delayHasPassed = false;
        } else delayPassed = 0;
        List<Entity> attacks = new ArrayList<>();
        for (Entity entity : Atomic.client.world.getEntities()) {
            if (!entity.isAttackable()) continue;
            if (entity.getUuid() == Atomic.client.player.getUuid()) continue;
            if (!entity.isAlive()) continue;
            if (entity.getPos().distanceTo(Atomic.client.player.getPos()) > getRange()) continue;
            if (entity instanceof PlayerEntity && attackPlayers.getValue()) attacks.add(entity);
            else if (entity instanceof MobEntity && attackMobs.getValue()) attacks.add(entity);
        }
        for (Entity attack : attacks) {
            Packets.sendServerSideLook(attack.getEyePos());
            Rotations.lookAtV3(attack.getPos().add(0, attack.getHeight() / 2, 0));
            if (delayHasPassed) {
                Atomic.client.player.swingHand(Hand.MAIN_HAND);
                Atomic.client.interactionManager.attackEntity(Atomic.client.player, attack);
            }
            if (mode.getValue().equalsIgnoreCase("single")) break;
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

