package me.zeroX150.atomic.feature.gui.widget;

import com.mojang.authlib.Agent;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import com.mojang.blaze3d.systems.RenderSystem;
import me.zeroX150.atomic.Atomic;
import me.zeroX150.atomic.feature.gui.clickgui.Themes;
import me.zeroX150.atomic.helper.Renderer;
import me.zeroX150.atomic.helper.Transitions;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.DefaultSkinHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.awt.*;
import java.net.Proxy;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AltEntryWidget extends ClickableWidget implements Drawable, Element {
    static Map<UUID, Identifier> skins = new HashMap<>();
    static Map<String, PlayerEntry> cache = new HashMap<>();
    public String uname;
    public UUID uuid;
    protected String mail;
    protected String pw;
    Identifier skin = DefaultSkinHelper.getTexture();
    double renderX;
    boolean failedLogIn = false;

    public AltEntryWidget(int x, int y, int width, int height, String email, String password) {
        super(x, y, width, height, Text.of(""));
        renderX = -width - 1;
        this.mail = email;
        this.pw = password;
        if (!pw.equals("\003")) {
            boolean shouldContinue = true;
            if (cache.containsKey(mail)) {
                shouldContinue = false;
                PlayerEntry pe = cache.get(mail);
                if (System.currentTimeMillis() - pe.addTime > 300000) { // invalidate cached alt after 5 mins
                    shouldContinue = true;
                    cache.remove(mail);
                } else {
                    uname = cache.get(mail).username;
                    uuid = cache.get(mail).uuid;
                    failedLogIn = cache.get(mail).loginIssue;
                }
            }
            if (shouldContinue) {
                YggdrasilUserAuthentication auth = (YggdrasilUserAuthentication) new YggdrasilAuthenticationService(
                        Proxy.NO_PROXY, "").createUserAuthentication(Agent.MINECRAFT);
                auth.setUsername(mail);
                auth.setPassword(pw);
                try {
                    if (!auth.canLogIn()) throw new Exception("cant log in");
                    auth.logIn();
                    GameProfile gp = auth.getSelectedProfile();
                    uname = gp.getName();
                    uuid = gp.getId();
                } catch (Exception ignored) {
                    uname = "Unknown username (" + mail + ")";
                    uuid = UUID.randomUUID();
                    failedLogIn = true;
                }
                cache.put(mail, new PlayerEntry(uname, uuid, System.currentTimeMillis(), failedLogIn));
            }
        } else {
            uname = mail;
            uuid = UUID.randomUUID();
        }
        if (!isCracked()) {
            getSkinTexture(uuid, uname, skin1 -> this.skin = skin1);
        }
    }

    public static void getSkinTexture(UUID uuid, String uname, SkinAvailabilityCallback callback) {
        System.out.println("Requesting skin for " + uuid.toString() + " - " + uname);
        if (skins.containsKey(uuid)) {
            callback.run(skins.get(uuid));
        } else {
            MinecraftClient.getInstance().getSkinProvider().loadSkin(new GameProfile(uuid, uname), (type, id, texture) -> {
                if (type == MinecraftProfileTexture.Type.SKIN) {
                    skins.put(uuid, id);
                    callback.run(id);
                }
            }, true);
        }
    }

    public boolean isCracked() {
        return failedLogIn || pw.equals("\003");
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        boolean done = false;
        if (this.active && this.visible && button == 0 && this.clicked(mouseX, mouseY)) {
            done = true;
            event_mouseClicked();
        }
        return done;
    }

    public void event_mouseClicked() {

    }

    public void tick() {
        renderX = Transitions.transition(renderX, x, 20);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        Color fontColor = Color.WHITE;
        if (failedLogIn) fontColor = new Color(255, 255, 255, 100);
        Color c = Renderer.modify(Themes.Theme.ATOMIC.getPalette().h_exp().brighter(), -1, -1, -1, 100);
        float mid = y + (height / 2f - (9 / 2f));
        Renderer.fill(matrices, c, renderX, y, renderX + width, y + height);
        String uid = uuid.toString();
        if (uid.length() > 23) {
            uid = uid.substring(0, 20) + "...";
        }
        RenderSystem.setShaderTexture(0, skin);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1F, 1F);
        int d = Math.min(width - 10, height - 10);
        drawTexture(matrices, (int) (renderX + 5), y + 5, d, d, 8.0F, 8, 8, 8, 64, 64);
        //drawTexture(matrices, (int) (renderX+5), y+5, 0, 0, 0, d,d,d,d);
        RenderSystem.defaultBlendFunc();
        float w = Atomic.fontRenderer.getStringWidth(uname);
        float w2 = Atomic.fontRenderer.getStringWidth(uid);
        Atomic.fontRenderer.drawString(matrices, uname, renderX + 5 + d + 2, mid - 5, fontColor.getRGB());
        Atomic.fontRenderer.drawString(matrices, uid, renderX + 5 + d + 2, mid + 5, fontColor.getRGB());
    }

    @Override
    public void appendNarrations(NarrationMessageBuilder builder) {

    }

    interface SkinAvailabilityCallback {
        void run(Identifier skin);
    }

    record PlayerEntry(String username, UUID uuid, long addTime, boolean loginIssue) {
    }
}
