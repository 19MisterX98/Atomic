package me.zeroX150.atomic.feature.module.impl.movement;

import me.zeroX150.atomic.Atomic;
import me.zeroX150.atomic.feature.module.Module;
import me.zeroX150.atomic.feature.module.ModuleType;
import me.zeroX150.atomic.feature.module.config.MultiValue;
import me.zeroX150.atomic.feature.module.config.SliderValue;
import net.minecraft.client.util.math.MatrixStack;

public class Speed extends Module {
    MultiValue mode = this.config.create("Mode","Vanilla","Vanilla","BHop","Legit","Minihop");
    SliderValue newSpeed = this.config.create("Speed multiplier",1,0,5,1);
    SliderValue bhopDown = this.config.create("Down velocity",1,-0.7,3,2);

    public Speed() {
        super("Speed", "schbeed", ModuleType.MISC);
        bhopDown.showOnlyIf(() -> mode.getValue().equalsIgnoreCase("bhop"));
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
        return mode.getValue();
    }

    @Override
    public void onWorldRender(MatrixStack matrices) {

    }

    @Override
    public void onHudRender() {

    }
}

