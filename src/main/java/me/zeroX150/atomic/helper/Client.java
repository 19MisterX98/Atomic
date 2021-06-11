package me.zeroX150.atomic.helper;

import me.zeroX150.atomic.Atomic;
import net.minecraft.text.Text;

import java.awt.*;

public class Client {
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
}
