package me.zeroX150.atomic.feature.gui.clickgui;

import me.zeroX150.atomic.Atomic;
import me.zeroX150.atomic.feature.module.Module;
import me.zeroX150.atomic.helper.keybind.KeybindManager;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class KeyListenerBtn extends ButtonWidget {
    public boolean listening = false;
    public long stoppedScanning = 0;
    Module parent;
    int kc;
    int sc;

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
        stoppedScanning = System.currentTimeMillis();
        if (keyCode == 47 || ((char) keyCode) == '-') {
            listening = false;
            kc = -1;
            parent.config.get("Keybind").setValue(kc);
            KeybindManager.reload();
            return true;
        }
        kc = keyCode;
        sc = scanCode;
        listening = false;
        parent.config.get("Keybind").setValue(kc);
        KeybindManager.reload();
        //this.setMessage(InputUtil.fromKeyCode(keyCode, scanCode).getLocalizedText());
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        if (listening) this.setMessage(Text.of("... (- to cancel)"));
        else this.setMessage(kc != -1 ? Text.of(String.valueOf((char) kc)) : Text.of("None"));
        fill(matrices, x, y, x + width, y + height, (this.isHovered() ? ClickGUI.currentActiveTheme.active() : ClickGUI.currentActiveTheme.inactive()).getRGB());
        Atomic.fontRenderer.drawCenteredString(matrices, this.getMessage().asString(), x + width / 2f, y + height / 2f - 8 / 2f, ClickGUI.currentActiveTheme.fontColor().getRGB());
        //DrawableHelper.drawCenteredText(matrices, Atomic.client.textRenderer, this.getMessage(), x + (width / 2), y + (height / 2 - 9 / 2), 0xFFFFFF);
    }
}
