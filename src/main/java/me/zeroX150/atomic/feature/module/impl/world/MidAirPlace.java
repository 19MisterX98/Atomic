package me.zeroX150.atomic.feature.module.impl.world;

import me.zeroX150.atomic.Atomic;
import me.zeroX150.atomic.feature.module.Module;
import me.zeroX150.atomic.feature.module.ModuleType;
import me.zeroX150.atomic.helper.Client;
import me.zeroX150.atomic.helper.Renderer;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class MidAirPlace extends Module {
    public MidAirPlace() {
        super("MidAirPlace", "magic", ModuleType.WORLD);
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
        HitResult hr = Atomic.client.crosshairTarget;
        if (!(hr instanceof BlockHitResult hitresult)) return;
        BlockPos bp = hitresult.getBlockPos();
        BlockState bs = Atomic.client.world.getBlockState(bp);
        if (bs.isAir()) {
            Renderer.renderFilled(new Vec3d(bp.getX(), bp.getY(), bp.getZ()), new Vec3d(1, 1, 1), Client.getCurrentRGB(), matrices);
            if (Atomic.client.options.keyUse.wasPressed()) {
                Atomic.client.interactionManager.interactBlock(Atomic.client.player, Atomic.client.world, Hand.MAIN_HAND, hitresult);
            }
        }
    }

    @Override
    public void onHudRender() {

    }
}

