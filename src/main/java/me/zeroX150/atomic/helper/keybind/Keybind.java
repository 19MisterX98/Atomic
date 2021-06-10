package me.zeroX150.atomic.helper.keybind;

import me.zeroX150.atomic.Atomic;
import net.minecraft.client.util.InputUtil;

public class Keybind {
    public int keycode;
    boolean pressedbefore = false;

    public Keybind(int kc) {
        this.keycode = kc;
    }

    public boolean isHeld() {
        if (keycode < 0)
            return false;
        return InputUtil.isKeyPressed(Atomic.client.getWindow().getHandle(), keycode)
                && Atomic.client.currentScreen == null;
    }

    public boolean isPressed() {
        if (Atomic.client.currentScreen != null)
            return false;
        if (keycode < 0)
            return false;
        boolean flag1 = InputUtil.isKeyPressed(Atomic.client.getWindow().getHandle(), keycode);
        if (flag1 && !pressedbefore) {
            pressedbefore = true;
            return true;
        }
        if (!flag1)
            pressedbefore = false;
        return false;
    }
}
