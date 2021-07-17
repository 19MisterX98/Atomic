package me.zeroX150.atomic.feature.gui.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import me.zeroX150.atomic.Atomic;
import me.zeroX150.atomic.feature.gui.clickgui.Themes;
import me.zeroX150.atomic.feature.gui.widget.AltEntryWidget;
import me.zeroX150.atomic.feature.gui.widget.PasswordFieldWidget;
import me.zeroX150.atomic.feature.module.impl.external.Alts;
import me.zeroX150.atomic.helper.Client;
import me.zeroX150.atomic.helper.Renderer;
import me.zeroX150.atomic.helper.Transitions;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AltManager extends Screen {
    public static Identifier EYE = new Identifier("atomic", "eye.png");
    public static Identifier EYEINVIS = new Identifier("atomic", "eyeinvis.png");
    TextFieldWidget username;
    PasswordFieldWidget password;
    ButtonWidget login;
    ButtonWidget save;
    ButtonWidget paste;
    ButtonWidget hidepass;
    String feedback = "";
    double savedHeight = 0;
    double scroll = 0;
    double renderScroll = 0;
    boolean alreadyInitialized = false;
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
        savedHeight = 0;
        int maxW = 240;
        int midpoint = (int) (width - maxW / 2 - 2.5);
        int widgetW = maxW - 10;
        this.client.keyboard.setRepeatEvents(true);
        username = new TextFieldWidget(Atomic.client.textRenderer, midpoint - widgetW / 2, 60, widgetW - 10, 20, Text.of("SPECIAL:Username"));
        username.setMaxLength(65535);
        password = new PasswordFieldWidget(Atomic.client.textRenderer, midpoint - widgetW / 2, 85, widgetW - 30, 20, Text.of("SPECIAL:Password"));
        password.setMaxLength(65535);
        password.setShowText(true);
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
        login = new ButtonWidget(midpoint - widgetW / 2, 85 + 25, widgetW / 2 - 10, 20, Text.of("Login"), button -> {
            feedback = "Logging into " + username.getText() + "...";
            boolean done = Client.auth(username.getText(), password.getText());
            feedback = done ? "Logged in!" : "Failed to login!";
            //status = "";
        });
        save = new ButtonWidget(midpoint, 85 + 25, widgetW / 2 - 10, 20, Text.of("Save alt"), button -> {
            if (username.getText().isEmpty()) return;
            String pair = username.getText() + "\002" + (password.getText().isEmpty() ? "\003" : password.getText());
            Alts.alts.setValue(Alts.alts.getValue() + "\n" + pair);
            Atomic.client.openScreen(this);
        });
        paste = new ButtonWidget(midpoint - widgetW / 2, 85 + 50, (widgetW / 2 - 5) * 2, 20, Text.of("Paste"), button -> {
            String clipboard = MinecraftClient.getInstance().keyboard.getClipboard();
            if (clipboard.contains(":")) {
                String[] pair = clipboard.split(":");
                String un = "";
                String pw = "";
                if (pair.length == 1) {
                    un = pair[0];
                    pw = "";
                } else if (pair.length >= 2) {
                    un = pair[0];
                    pw = pair[1];
                }
                password.setText(pw);
                username.setText(un);
                if (un.isEmpty()) {
                    username.setTextFieldFocused(true);
                    password.setTextFieldFocused(false);
                } else if (pw.isEmpty()) {
                    password.setTextFieldFocused(true);
                    username.setTextFieldFocused(false);
                }

            }
        });
        hidepass = new ButtonWidget(midpoint + 85, 85, 20, 20, Text.of(""), button -> password.setShowText(!password.isShowText()));
        ButtonWidget quit = new ButtonWidget(width - 10 - 100, height - 30, 100, 20, Text.of("Quit"), button -> Atomic.client.openScreen(null));

        addDrawableChild(login);
        addDrawableChild(username);
        addDrawableChild(password);
        addDrawableChild(save);
        addDrawableChild(paste);
        addDrawableChild(hidepass);
        addDrawableChild(quit);

        updater = new Thread(() -> {
            feedback = "Loading alts...";
            int yOffset = 50;
            List<AltEntryWidget> entries = new ArrayList<>();
            int index = 0;
            String[] l = Arrays.stream(Alts.alts.getValue().split("\n")).filter(s -> s.split("\002").length == 2).toArray(String[]::new);
            List<String> seenEmails = new ArrayList<>();
            for (String s : l) {
                String[] authPair = s.split("\002");
                if (authPair.length != 2) continue;
                String un = authPair[0];
                String pw = authPair[1];
                if (seenEmails.contains(un)) continue;
                index++;
                feedback = "Logging into alt " + index + " / " + l.length;
                AltEntryWidget w = new AltEntryWidget(5, yOffset, width - maxW - 20, 40, un, pw) {
                    @Override
                    public void event_mouseClicked() {
                        AltManager.this.username.setText(this.mail);
                        String pw = this.pw.equals("\003") ? "" : this.pw;
                        AltManager.this.password.setText(pw);
                    }

                    @Override
                    public void event_altDeleted() {
                        List<String> keep = new ArrayList<>();
                        for (String s : Alts.alts.getValue().split("\n")) {
                            String[] authPair = s.split("\002");
                            if (authPair.length != 2) continue;
                            String un = authPair[0];
                            String pw = authPair[1];
                            if (!un.equals(this.mail) || !pw.equals(this.pw)) {
                                keep.add(s);
                            }
                        }
                        Alts.alts.setValue(String.join("\n", keep));
                        Atomic.client.openScreen(AltManager.this);
                    }
                };
                entries.add(w);
                yOffset += 45;
                seenEmails.add(un);
            }
            feedback = "";
            for (AltEntryWidget entry : entries) {
                if (alreadyInitialized) {
                    entry.renderX = entry.x;
                }
                run(() -> addDrawableChild(entry));
                savedHeight += 40 + 5;
                try {
                    Thread.sleep(alreadyInitialized ? 0 : 50);
                } catch (InterruptedException e) {
                    break;
                }
            }
            alreadyInitialized = true;
        });
        updater.start();
        if (alreadyInitialized) {
            try {
                updater.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
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
        renderScroll = Transitions.transition(renderScroll, scroll, 20);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        username.mouseClicked(0, 0, 0);
        password.mouseClicked(0, 0, 0);
        if (mouseX < width-250) {
            mouseY += renderScroll;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        if (r.size() != 0) {
            for (Runnable runnable : r) {
                runnable.run();
            }
            r.clear();
        }
        if (savedHeight - scroll < height - 50) {
            mouseScrolled(mouseX, mouseY, 0);
        }
        this.client.keyboard.setRepeatEvents(true);
        renderBackground(matrices);

        Renderer.fill(Renderer.modify(Themes.Theme.ATOMIC.getPalette().h_exp().brighter(), -1, -1, -1, 100), width - 250, 50, width - 5, height - 5);

        Atomic.fontRenderer.drawString(matrices, "Alt manager    Current account: " + Atomic.client.getSession().getUsername() + " | " + Atomic.client.getSession().getUuid(), 5, 5, 0xFFFFFF);
        //Atomic.fontRenderer.drawCenteredString(matrices, status, width - (width / 3.5 / 2 - 2.5d), 10, 0xAAFFFFFF);
        Atomic.fontRenderer.drawString(matrices, feedback, 5, 15, 0xFFFFFF);

        MatrixStack defaultStack = new MatrixStack();
        matrices.translate(0, -renderScroll, 0);
        RenderSystem.enableScissor(10, 10, (int) (width * Atomic.client.getWindow().getScaleFactor()), (int) (height * Atomic.client.getWindow().getScaleFactor() - 110));
        for (Element child : this.children()) {
            if (child instanceof Drawable d) {
                if (d instanceof AltEntryWidget) {
                    d.render(matrices, mouseX, mouseY, delta);
                }
            }
        }
        RenderSystem.disableScissor();
        matrices.translate(0, renderScroll, 0);

        for (Element child : this.children()) {
            if (!(child instanceof AltEntryWidget) && child instanceof Drawable d) {
                d.render(defaultStack, mouseX, mouseY, delta);
            }

        }

        RenderSystem.setShaderTexture(0, password.isShowText() ? EYEINVIS : EYE);
        RenderSystem.enableBlend();
        RenderSystem.blendEquation(32774);
        RenderSystem.blendFunc(770, 1);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1F, 1F);
        drawTexture(new MatrixStack(), (int) ((width - 240 / 2 - 2.5) + 87), 87, 16, 16, 0, 0, 32, 32, 32, 32);
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableBlend();

    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        scroll -= amount * 50;
        while (savedHeight - scroll < height - 50) {
            scroll -= 1;
        }
        if (scroll < 0) scroll = 0;
        return super.mouseScrolled(mouseX, mouseY, amount);
    }
}
