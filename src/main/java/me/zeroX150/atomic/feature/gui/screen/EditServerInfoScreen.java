package me.zeroX150.atomic.feature.gui.screen;

import me.zeroX150.atomic.Atomic;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class EditServerInfoScreen extends Screen {
    ServerInfo si;
    TextFieldWidget motd;
    Screen parent;

    public EditServerInfoScreen(ServerInfo serverToEdit, Screen parent) {
        super(Text.of(""));
        this.si = serverToEdit;
        this.parent = parent;
    }

    @Override
    protected void init() {
        int fw = 200;
        motd = new TextFieldWidget(textRenderer, width / 2 - fw / 2, height / 2 - 23, fw, 20, Text.of("SPECIAL:Server MOTD"));
        motd.setMaxLength(65535);
        motd.setText(si.label.asString().replaceAll("§", "&").replaceAll("\n", "\\\\n"));
        ButtonWidget cancel = new ButtonWidget(width / 2 - fw / 2, height / 2 + 23, fw, 20, Text.of("Cancel"), button -> {
            Atomic.client.openScreen(parent);
        });
        ButtonWidget save = new ButtonWidget(width / 2 - fw / 2, height / 2, fw, 20, Text.of("Save"), button -> {
            si.label = Text.of(motd.getText().replaceAll("&", "§").replaceAll("\\\\n", "\n"));
            Atomic.client.openScreen(parent);
        });
        addDrawableChild(save);
        addDrawableChild(motd);
        addDrawableChild(cancel);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        renderBackground(matrices);
        Atomic.fontRenderer.drawString(matrices, "MOTD Editor", 5, 5, 0xFFFFFF);
        List<String> texts = new ArrayList<>();
        texts.add("How to use");
        texts.add("Use & color codes for styling");
        texts.add("Use \"\\n\"s for newlines");
        texts.add("Click \"Save\" to apply changes");
        texts.add("Click \"Cancel\" to abort");
        texts.add("Client side only :sadge:");
        int yOff = 15;
        for (String text : texts) {
            Atomic.monoFontRenderer.drawString(matrices, text, 5, yOff, 0xFFFFFF);
            yOff += 10;
        }
        Atomic.fontRenderer.drawString(matrices, motd.getText().isEmpty() ? "" : "Server MOTD", width / 2f - 100, height / 2f - 33, 0xFFFFFF);
        super.render(matrices, mouseX, mouseY, delta);
    }
}
