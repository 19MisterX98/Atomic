package me.zeroX150.atomic.feature.gui.screen;

import me.zeroX150.atomic.Atomic;
import me.zeroX150.atomic.feature.module.impl.external.Alts;
import me.zeroX150.atomic.helper.Client;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.CommandBlockScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.util.Arrays;

public class AltManager extends Screen {
    TextFieldWidget username;
    TextFieldWidget password;
    ButtonWidget login;
    ButtonWidget save;
    String feedback = "";

    public AltManager() {
        super(Text.of(""));
    }

    @Override
    protected void init() {
        super.init();
        this.client.keyboard.setRepeatEvents(true);
        username = new TextFieldWidget(Atomic.client.textRenderer, width / 2 - (200 / 2), height / 2 - (20 / 2) - 22, 200, 20, Text.of("SPECIAL:Username"));
        username.setMaxLength(65535);
        password = new TextFieldWidget(Atomic.client.textRenderer, width / 2 - (200 / 2), height / 2 - (20 / 2), 200, 20, Text.of("SPECIAL:Password"));
        password.setMaxLength(65535);
        username.setChangedListener(v -> {
            String s = username.getText();
            if (s.contains(":")) {
                String[] pair = s.split(":");
                if (pair.length < 2) return;
                String uname = pair[0];
                String pw = pair[1];
                username.setText(uname);
                password.setText(pw);
            }
        });
        password.setChangedListener(v -> {
            String s = password.getText();
            if (s.contains(":")) {
                String[] pair = s.split(":");
                if (pair.length < 2) return;
                String uname = pair[0];
                String pw = pair[1];
                username.setText(uname);
                password.setText(pw);
            }
        });
        login = new ButtonWidget(width / 2 - (150 / 2), height / 2 - (20 / 2) + 22, 150, 20, Text.of("Login"), button -> {
            boolean done = Client.auth(username.getText(), password.getText());
            feedback = done ? "§aLogged in!" : "§cFailed to login!";
        });
        save = new ButtonWidget(width / 2 - (150 / 2), height / 2 - (20 / 2) + 22 + 22, 150, 20, Text.of("Save alt"), button -> {
            if (username.getText().isEmpty()) return;
            String pair = username.getText() + "\002" + (password.getText().isEmpty() ? "\003" : password.getText());
            Alts.alts.setValue(Alts.alts.getValue() + "\n" + pair);
            Atomic.client.openScreen(this);
        });
        addDrawableChild(login);
        addDrawableChild(username);
        addDrawableChild(password);
        addDrawableChild(save);

        int yOffset = 5;
        for (String s : Alts.alts.getValue().split("\n")) {
            String[] authPair = s.split("\002");
            if (authPair.length != 2) continue;
            String un = authPair[0];
            String pw = authPair[1];
            ButtonWidget alt = new ButtonWidget(width - 156, yOffset, 130, 20, Text.of(un), button -> {
                username.setText(un);
                password.setText(pw.equals("\003") ? "" : pw);
            });
            ButtonWidget altDelete = new ButtonWidget(width - 22, yOffset, 20, 20, Text.of("X"), button -> {
                String[] bruh = Arrays.stream(Alts.alts.getValue().split("\n")).filter(s1 -> s1.split("\002").length == 2 && !s1.split("\002")[0].equals(un) && !s1.split("\002")[1].equals(pw)).toArray(String[]::new);
                Alts.alts.setValue(String.join("\n", bruh));
                Atomic.client.openScreen(this);
            });
            addDrawableChild(alt);
            addDrawableChild(altDelete);
            yOffset += 25;
        }
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        username.mouseClicked(0, 0, 0);
        password.mouseClicked(0, 0, 0);
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.client.keyboard.setRepeatEvents(true);
        renderBackground(matrices);

        drawCenteredText(matrices, textRenderer, feedback, width / 2, 50, 0xFFFFFF);
        super.render(matrices, mouseX, mouseY, delta);
    }
}
