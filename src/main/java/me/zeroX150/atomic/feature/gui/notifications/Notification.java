package me.zeroX150.atomic.feature.gui.notifications;

import me.zeroX150.atomic.Atomic;

import java.util.ArrayList;
import java.util.List;

public class Notification {
    public double posX;
    public double posY;
    public double renderPosX = -1;
    public double renderPosY = -1;
    public String title;
    public String[] contents;
    public long creationDate;
    public long duration;

    public Notification(long duration, String title, String... contents) {
        this.duration = duration;
        this.creationDate = System.currentTimeMillis();
        this.contents = contents;
        this.title = title;
    }

    public static Notification create(long duration, String title, String... contents) {
        Notification n = new Notification(duration, title, contents);
        NotificationRenderer.notifications.add(0, n);
        return n;
    }

    public static Notification create(long duration, String title, String split) {
        List<String> splitContent = new ArrayList<>();
        StringBuilder line = new StringBuilder();
        for (String c : split.split(" +")) {
            if (Atomic.client.textRenderer.getWidth(line + " " + c) >= 145) {
                splitContent.add(line.toString());
                line = new StringBuilder();
            }
            line.append(c).append(" ");
        }
        splitContent.add(line.toString());
        return create(duration, title, splitContent.toArray(new String[0]));
    }
}
