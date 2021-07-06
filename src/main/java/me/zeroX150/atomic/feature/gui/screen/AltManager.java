package me.zeroX150.atomic.feature.gui.screen;

import me.zeroX150.atomic.Atomic;
import me.zeroX150.atomic.feature.gui.clickgui.Themes;
import me.zeroX150.atomic.feature.gui.widget.AltEntryWidget;
import me.zeroX150.atomic.feature.module.impl.external.Alts;
import me.zeroX150.atomic.helper.Client;
import me.zeroX150.atomic.helper.Renderer;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AltManager extends Screen {
    TextFieldWidget username;
    TextFieldWidget password;
    ButtonWidget login;
    ButtonWidget save;
    String feedback = "";
    String status = "";

    List<Runnable> r = new ArrayList<>();

    Thread updater = null;

    public AltManager() {
        super(Text.of(""));
    }

    @Override
    protected void init() {
        super.init();
        if (updater != null) {
            updater.interrupt();
            updater = null;
        }
        clearChildren();
        int maxW = 240;
        int midpoint = (int) (width - maxW / 2 - 2.5);
        int widgetW = maxW - 10;
        this.client.keyboard.setRepeatEvents(true);
        username = new TextFieldWidget(Atomic.client.textRenderer, midpoint - widgetW / 2, 30, widgetW - 10, 20, Text.of("SPECIAL:Username"));
        username.setMaxLength(65535);
        password = new TextFieldWidget(Atomic.client.textRenderer, midpoint - widgetW / 2, 55, widgetW - 10, 20, Text.of("SPECIAL:Password"));
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
        login = new ButtonWidget(midpoint - widgetW / 2, 80, widgetW / 2 - 10, 20, Text.of("Login"), button -> {
            status = "Logging into " + username.getText() + "...";
            boolean done = Client.auth(username.getText(), password.getText());
            feedback = done ? "§aLogged in!" : "§cFailed to login!";
            //status = "";
        });
        save = new ButtonWidget(midpoint, 80, widgetW / 2 - 10, 20, Text.of("Save alt"), button -> {
            if (username.getText().isEmpty()) return;
            String pair = username.getText() + "\002" + (password.getText().isEmpty() ? "\003" : password.getText());
            Alts.alts.setValue(Alts.alts.getValue() + "\n" + pair);
            Atomic.client.openScreen(this);
        });
        addDrawableChild(login);
        addDrawableChild(username);
        addDrawableChild(password);
        addDrawableChild(save);

        updater = new Thread(() -> {
            status = "Loading alts...";
            int yOffset = 5;
            List<AltEntryWidget> entries = new ArrayList<>();
            int index = 0;
            String[] l = Arrays.stream(Alts.alts.getValue().split("\n")).filter(s -> s.split("\002").length == 2).toArray(String[]::new);
            for (String s : l) {
                String[] authPair = s.split("\002");
                if (authPair.length != 2) continue;
                String un = authPair[0];
                String pw = authPair[1];
                index++;
                status = "Logging into alt " + index + " / " + l.length;
                AltEntryWidget w = new AltEntryWidget(3, yOffset, 160, 30, un, pw) {
                    @Override
                    public void event_mouseClicked() {
                        AltManager.this.username.setText(this.mail);
                        String pw = this.pw.equals("\003") ? "" : this.pw;
                        AltManager.this.password.setText(pw);
                    }
                };
                entries.add(w);
                yOffset += 35;
            }
            status = "";
            for (AltEntryWidget entry : entries) {
                run(() -> addDrawableChild(entry));
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    break;
                }
            }
        });
        updater.start();
    }

    void run(Runnable r) {
        this.r.add(r);
    }

    public void fastTick() {
        for (Element child : children()) {
            if (child instanceof AltEntryWidget e) {
                e.tick();
            }
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

        Renderer.fill(Renderer.modify(Themes.Theme.ATOMIC.getPalette().h_exp(), -1, -1, -1, 100), width - 250, 5, width - 5, height - 5);

        Atomic.fontRenderer.drawCenteredString(matrices, status, width - (width / 3.5 / 2 - 2.5d), 10, 0xAAFFFFFF);

        Atomic.fontRenderer.drawCenteredString(matrices, feedback, width - (width / 3.5 / 2 - 2.5d), status.isEmpty() ? 10 : 20, 0xFFFFFF);


        super.render(matrices, mouseX, mouseY, delta);

        if (r.size() != 0) {
            for (Runnable runnable : r) {
                runnable.run();
            }
            r.clear();
        }
    }
}
