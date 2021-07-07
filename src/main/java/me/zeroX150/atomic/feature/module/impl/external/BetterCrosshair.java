package me.zeroX150.atomic.feature.module.impl.external;

import me.zeroX150.atomic.Atomic;
import me.zeroX150.atomic.feature.module.Module;
import me.zeroX150.atomic.feature.module.ModuleType;
import me.zeroX150.atomic.feature.module.config.MultiValue;
import me.zeroX150.atomic.feature.module.config.SliderValue;
import me.zeroX150.atomic.helper.Client;
import me.zeroX150.atomic.helper.Renderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;

public class BetterCrosshair extends Module {
    static MultiValue mode;
    static SliderValue speenSpeed;
    public BetterCrosshair() {
        super("BetterCrosshair", "a better crosshair", ModuleType.RENDER);
        mode = (MultiValue) this.config.create("Mode","Cube PY","Cube PY", "Cube SPEEN").description("What to render");
        speenSpeed = (SliderValue) this.config.create("Speen duration",2000,100,10000,0).description("SPEEN");
        speenSpeed.showOnlyIf(() -> mode.getValue().equalsIgnoreCase("cube speen"));
    }

    public static void render() {
        float pitch = Atomic.client.player.getPitch();
        float yaw = Atomic.client.player.getYaw();
        MatrixStack s = new MatrixStack();
        int w = Atomic.client.getWindow().getScaledWidth();
        int h = Atomic.client.getWindow().getScaledHeight();
        s.translate(w/2d,h/2d,0);
        if(mode.getValue().equalsIgnoreCase("cube speen")) {
            int speed = (int) (speenSpeed.getValue()+0d);
            s.multiply(new Quaternion(new Vec3f(0,1,0),(System.currentTimeMillis()%speed)/(float)speed*360f,true));
            s.multiply(new Quaternion(new Vec3f(0,0,1),((System.currentTimeMillis())%speed)/(float)speed*360f,true));
        } else if (mode.getValue().equalsIgnoreCase("cube py")) {
            s.multiply(new Quaternion(new Vec3f(0,-1,0),yaw+90,true));
            s.multiply(new Quaternion(new Vec3f(0,0,-1),pitch,true));
        }

        Renderer.renderOutlineNoTransform(new Vec3d(-5,-5,-5),new Vec3d(10,10,10), Color.WHITE,s);
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

