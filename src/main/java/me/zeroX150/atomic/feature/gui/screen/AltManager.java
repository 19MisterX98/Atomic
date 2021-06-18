package me.zeroX150.atomic.feature.gui.screen;

import me.zeroX150.atomic.Atomic;
import me.zeroX150.atomic.helper.Client;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class AltManager extends Screen {
    TextFieldWidget username;
    TextFieldWidget password;
    ButtonWidget login;
    String feedback = "";

    public AltManager() {
        super(Text.of(""));
    }

    @Override
    protected void init() {
        super.init();
        username = new TextFieldWidget(Atomic.client.textRenderer, width / 2 - (150 / 2), height / 2 - (20 / 2) - 22, 150, 20, Text.of("SPECIAL:Username"));
        password = new TextFieldWidget(Atomic.client.textRenderer, width / 2 - (150 / 2), height / 2 - (20 / 2), 150, 20, Text.of("SPECIAL:Password"));
        login = new ButtonWidget(width / 2 - (150 / 2), height / 2 - (20 / 2) + 22, 150, 20, Text.of("Login"), button -> {
            boolean done = Client.auth(username.getText(), password.getText());
            feedback = done ? "§aLogged in!" : "§cFailed to login!";
        });
        addDrawableChild(login);
        addDrawableChild(username);
        addDrawableChild(password);
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
        renderBackground(matrices);

        drawCenteredText(matrices, textRenderer, feedback, width / 2, 50, 0xFFFFFF);
        super.render(matrices, mouseX, mouseY, delta);
    }
}
