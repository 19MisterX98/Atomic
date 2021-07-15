package me.zeroX150.atomic.helper;

import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import com.google.common.io.Files;
import com.mojang.authlib.Agent;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import me.zeroX150.atomic.Atomic;
import me.zeroX150.atomic.mixin.game.IMinecraftClientAccessor;
import net.minecraft.client.input.Input;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.client.util.Session;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.apache.logging.log4j.Level;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Proxy;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.UUID;

public class Client {
    private static final Input INPUT_BLOCK = new Input() {
        @Override
        public void tick(boolean slowDown) {
            this.movementSideways = 0f;
            this.movementForward = 0f;
        }
    };
    public static ServerInfo latestServerInfo;
    private static Input INPUT_NORMAL = null;

    public static void drop(int index) {
        int translatedSlotId;
        if (index >= 0 && index < 9) translatedSlotId = 36 + index;
        else translatedSlotId = index;
        //ItemStack stack = Atomic.client.player.getInventory().getStack(index);
        Atomic.client.interactionManager.clickSlot(Atomic.client.player.currentScreenHandler.syncId, translatedSlotId, 1, SlotActionType.THROW, Atomic.client.player);
    }

    public static boolean isPlayerNameValid(String name) {
        String valid = "abcdefghijklmnopqrstuvwxyz0123456789_";
        boolean isValidEntityName = true;
        for (char c : name.toLowerCase().toCharArray()) {
            if (!valid.contains(c + "")) {
                isValidEntityName = false;
                break;
            }
        }
        return isValidEntityName;
    }

    public static void downloadFile(String urlStr, String file) throws IOException {
        URL url = new URL(urlStr);
        ReadableByteChannel rbc = Channels.newChannel(url.openStream());
        FileOutputStream fos = new FileOutputStream(file);
        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        fos.close();
        rbc.close();
    }

    public static void isClientOutdated(OutdatedCheckCallback callback) throws Exception {
        callback.log("Getting current mod file");
        File modFile = new File(Client.class.getProtectionDomain().getCodeSource().getLocation().toURI());
        if (modFile.isDirectory()) {
            callback.log("Detected development environment. Wont check for updates");
            Thread.sleep(500);
            callback.callback(false);
            return;
        }
        File parent = new File(modFile.getParentFile().getParentFile().getAbsolutePath() + "/atomicTmp");
        if (!parent.isDirectory()) {
            parent.delete();
        }
        if (!parent.exists()) {
            parent.mkdir();
        }
        parent = new File(parent.getAbsolutePath() + "/atomicLatest.jar");
        if (parent.exists()) {
            boolean deleted = parent.delete();
            callback.log(deleted ? "Deleted old file" : "Failed to delete old file!");
        }
        callback.log("Downloading latest client jar");
        downloadFile("https://github.com/cornos/Atomic/raw/master/builds/latest.jar", parent.getAbsolutePath());
        callback.log("Downloaded!");
        HashCode hc = Files.asByteSource(modFile).hash(Hashing.crc32());
        callback.log("Hash of current jarfile: " + hc);
        HashCode hc1 = Files.asByteSource(parent).hash(Hashing.crc32());
        callback.log("Hash of latest jarfile: " + hc1);
        if (!hc.equals(hc1)) callback.log("Hash mismatch!");
        callback.callback(!hc.equals(hc1));
    }

    public static void startBlockingMovement() {
        INPUT_NORMAL = Atomic.client.player.input;
        Atomic.client.player.input = INPUT_BLOCK;
    }

    public static void stopBlockingMovement() {
        if (INPUT_NORMAL != null) Atomic.client.player.input = INPUT_NORMAL;
    }

    public static void notifyUser(String n) {
        if (Atomic.client.player == null) return;
        Atomic.client.player.sendMessage(Text.of("[§9A§r] " + n), false);
    }

    public static Color getCurrentRGB() {
        return new Color(Color.HSBtoRGB((System.currentTimeMillis() % 4750) / 4750f, 0.7f, 1));
    }

    public static double roundToN(double x, int n) {
        if (n == 0) return Math.floor(x);
        double factor = Math.pow(10, n);
        return Math.round(x * factor) / factor;
    }

    public static int tryParseInt(String input, int defaultValue) {
        try {
            return Integer.parseInt(input);
        } catch (Exception ignored) {
            return defaultValue;
        }
    }

    public static boolean auth(String username, String password) {
        if (password.isEmpty()) {
            Session crackedSession = new Session(username, UUID.randomUUID().toString(), "CornosOnTOP", "mojang");
            ((IMinecraftClientAccessor) Atomic.client).setSession(crackedSession);
            return true;
        }
        YggdrasilUserAuthentication auth = (YggdrasilUserAuthentication) new YggdrasilAuthenticationService(
                Proxy.NO_PROXY, "").createUserAuthentication(Agent.MINECRAFT);
        auth.setPassword(password);
        auth.setUsername(username);
        try {
            auth.logIn();
            Session ns = new Session(auth.getSelectedProfile().getName(), auth.getSelectedProfile().getId().toString(),
                    auth.getAuthenticatedToken(), "mojang");
            ((IMinecraftClientAccessor) Atomic.client).setSession(ns);
            return true;
        } catch (Exception ec) {
            Atomic.log(Level.ERROR, "Failed to log in: ");
            ec.printStackTrace();
            return false;
        }
    }

    public static double getMouseX() {
        return Atomic.client.mouse.getX() / Atomic.client.getWindow().getScaleFactor();
    }

    public static double getMouseY() {
        return Atomic.client.mouse.getY() / Atomic.client.getWindow().getScaleFactor();
    }

    public static void lookAt(Vec3d target1) {
        double vec = 57.2957763671875;
        Vec3d target = target1.subtract(Atomic.client.player.getEyePos());
        double square = Math.sqrt(target.x * target.x + target.z * target.z);
        float pitch = MathHelper.wrapDegrees((float) (-(MathHelper.atan2(target.y, square) * vec)));
        float yaw = MathHelper.wrapDegrees((float) (MathHelper.atan2(target.z, target.x) * vec) - 90.0F);

        Atomic.client.player.setYaw(yaw);
        Atomic.client.player.setPitch(pitch);
    }

    public interface OutdatedCheckCallback {
        void callback(boolean isOutdated);

        void log(String message);
    }
}
