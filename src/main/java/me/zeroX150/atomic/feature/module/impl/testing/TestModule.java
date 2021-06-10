package me.zeroX150.atomic.feature.module.impl.testing;

import me.zeroX150.atomic.feature.module.Module;
import me.zeroX150.atomic.feature.module.ModuleType;
import me.zeroX150.atomic.feature.module.config.DynamicValue;
import me.zeroX150.atomic.feature.module.config.MultiValue;
import me.zeroX150.atomic.feature.module.config.SliderValue;
import me.zeroX150.atomic.helper.Client;
import me.zeroX150.atomic.helper.Renderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;

public class TestModule extends Module {
    int delayPassed = 0;
    SliderValue poggers = this.config.create("delay", 3, 10, 40, 1);
    DynamicValue<String> pogers2 = this.config.create("text", "among");
    MultiValue mv = this.config.create("amoung2", "yeet", "yeet", "yeetus", "fetus", "deletus");


    public TestModule() {
        super("Test", "among", ModuleType.TEST);
    }

    @Override
    public void tick() {
        delayPassed++;
        if (delayPassed > poggers.getValue()) {
            delayPassed = 0;
            Client.notifyUser(pogers2.getValue());
        }
        /*if (delayPassed++>20) {
            this.setEnabled(false);
            delayPassed = 0;
            Atomic.client.openScreen(new ClickGUI());
        }*/
        //Client.notifyUser("yep, cock");
    }

    @Override
    public void enable() {
    }

    @Override
    public void disable() {

    }

    @Override
    public String getContext() {
        return "";
    }

    @Override
    public void onWorldRender(MatrixStack matrices) {
        Renderer.line(Renderer.getCrosshairVector(), new Vec3d(1, poggers.getValue(), 1), Client.getCurrentRGB(), matrices);

    }

    @Override
    public void onHudRender() {

    }
}
