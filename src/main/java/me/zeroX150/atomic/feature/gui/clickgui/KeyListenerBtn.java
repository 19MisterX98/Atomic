package me.zeroX150.atomic.feature.gui.clickgui;

import me.zeroX150.atomic.Atomic;
import me.zeroX150.atomic.feature.module.Module;
import me.zeroX150.atomic.helper.keybind.KeybindManager;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class KeyListenerBtn extends ButtonWidget {
    Module parent;
    boolean listening = false;
    int kc;

    public KeyListenerBtn(int x, int y, int width, Module parent) {
        super(x, y, width, 12, Text.of(String.valueOf((char) Integer.parseInt(parent.config.get("Keybind").getValue() + "")).toUpperCase()), button -> {
        });
        kc = (int) parent.config.get("Keybind").getValue();
        this.parent = parent;
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        listening = true;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (!this.listening) return false;
        String v = ((char) (keyCode)) + "";
        if (keyCode == 47 || v.charAt(0) == '-') {
            listening = false;
            kc = -1;
            parent.config.get("Keybind").setValue(kc);
            KeybindManager.reload();
            return false;
        }
        v = v.toUpperCase();
        char bruh = v.charAt(0);
        kc = bruh;
        listening = false;
        parent.config.get("Keybind").setValue(kc);
        KeybindManager.reload();
        this.setMessage(Text.of(String.valueOf(bruh)));
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        if (listening) this.setMessage(Text.of("... (- to cancel)"));
        else this.setMessage(Text.of(kc != -1 ? String.valueOf((char) kc) : "None"));
        fill(matrices, x, y, x + width, y + height, this.isHovered() ? ClickGUI.ACTIVE.getRGB() : ClickGUI.INACTIVE.getRGB());
        DrawableHelper.drawCenteredText(matrices, Atomic.client.textRenderer, this.getMessage(), x + (width / 2), y + (height / 2 - 9 / 2), 0xFFFFFF);
    }
}
