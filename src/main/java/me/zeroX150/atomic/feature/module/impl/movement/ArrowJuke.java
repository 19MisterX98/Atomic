package me.zeroX150.atomic.feature.module.impl.movement;

import me.zeroX150.atomic.Atomic;
import me.zeroX150.atomic.feature.module.Module;
import me.zeroX150.atomic.feature.module.ModuleType;
import me.zeroX150.atomic.feature.module.config.SliderValue;
import me.zeroX150.atomic.helper.Renderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ArrowJuke extends Module {
    SliderValue predict = this.config.create("Prediction dist", 10, 5, 20, 1);
    SliderValue accuracy = this.config.create("Laziness", 0.6, 0.1, 2, 2);
    SliderValue expand = this.config.create("Hitbox expand", 0.3, 0, 1, 3);

    List<Vec3d> bruhMoments = new ArrayList<>();

    public ArrowJuke() {
        super("ArrowAvoid", "fuck skeletons", ModuleType.MOVEMENT);
    }

    @Override
    public void tick() {
        if (Atomic.client.player == null || Atomic.client.world == null) return;
        bruhMoments.clear();
        for (Entity e : Atomic.client.world.getEntities()) {
            if (e instanceof ArrowEntity) {
                if (e.isOnGround()) continue;
                Vec3d arrowRot = e.getVelocity();
                for (double i = 0; i < predict.getValue(); i += accuracy.getValue()) {
                    Vec3d arrowPos = e.getPos().add(arrowRot.multiply(i));
                    bruhMoments.add(arrowPos);
                    if (Atomic.client.player.getBoundingBox().expand(expand.getValue()).contains(arrowPos)) {
                        Vec3d goTo = null;
                        for (int x = -2; x < 3; x++) {
                            if (goTo != null) break;
                            for (int z = -2; z < 3; z++) {
                                if (!Atomic.client.player.getBoundingBox().expand(expand.getValue()).offset(x, 0, z).contains(arrowPos)) {
                                    Vec3d cp = new Vec3d(x, 0, z);
                                    BlockPos np = Atomic.client.player.getBlockPos().add(cp.x, cp.y, cp.z);
                                    if (Atomic.client.world.getBlockState(np).getMaterial().isReplaceable() && Atomic.client.world.getBlockState(np.add(0, 1, 0)).getMaterial().isReplaceable()) {
                                        goTo = cp;
                                        break;
                                    }
                                }
                            }
                        }
                        if (goTo != null) {
                            Vec3d np = Atomic.client.player.getPos().add(goTo);
                            //Atomic.client.player.updatePosition(np.x, np.y, np.z);
                            Atomic.client.player.addVelocity(np.x, np.y, np.z);
                        }
                    }
                }
            }
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
        for (Vec3d bruhMoment : bruhMoments) {
            Renderer.renderOutline(bruhMoment, new Vec3d(0.1, 0.1, 0.1), Color.RED, matrices);
        }
    }

    @Override
    public void onHudRender() {

    }
}
