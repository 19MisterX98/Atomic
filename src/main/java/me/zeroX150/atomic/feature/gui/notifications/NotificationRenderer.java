package me.zeroX150.atomic.feature.gui.notifications;

import me.zeroX150.atomic.Atomic;
import me.zeroX150.atomic.helper.Renderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class NotificationRenderer {
    public static List<Notification> notifications = new ArrayList<>();

    static long lastRender = System.currentTimeMillis();

    public static void render() {
        MatrixStack ms = new MatrixStack();
        double m = System.currentTimeMillis() - lastRender;
        if (m > 1) lastRender = System.currentTimeMillis();
        m /= 12;
        int currentYOffset = -20;
        int baseX = Atomic.client.getWindow().getScaledWidth() - 150;
        int baseY = Atomic.client.getWindow().getScaledHeight() - 50;
        long c = System.currentTimeMillis();
        for (Notification notification : notifications.toArray(new Notification[0])) {
            double timeRemaining = Math.abs(c - notification.creationDate - notification.duration) / (double) notification.duration;
            timeRemaining = MathHelper.clamp(timeRemaining, 0, 1);
            timeRemaining = Math.abs(1 - timeRemaining);
            boolean notificationExpired = notification.creationDate + notification.duration < c;
            int notifHeight = 2 + ((notification.contents.length + (notification.title.isEmpty() ? 0 : 1)) * 9);
            currentYOffset += notifHeight + 2;
            notification.posY = baseY - currentYOffset;
            if (!notificationExpired) {
                notification.posX = baseX;
            } else {
                notification.posX = baseX + 160;
                if (notification.renderPosX > baseX + 155) {
                    notifications.remove(notification);
                    continue;
                }
            }
            if (notification.renderPosY == 0) notification.renderPosY = notification.posY;
            if (notification.renderPosX == 0) notification.renderPosX = baseX + 151;
            float xDiff = (float) (notification.renderPosX - notification.posX);
            float yDiff = (float) (notification.renderPosY - notification.posY);
            double nxDiff = (xDiff / me.zeroX150.atomic.feature.module.impl.render.ClickGUI.smooth.getValue()) * m;
            double nyDiff = (yDiff / me.zeroX150.atomic.feature.module.impl.render.ClickGUI.smooth.getValue()) * m;
            if (Math.abs(nxDiff) < 0.02) nxDiff = xDiff;
            if (Math.abs(nyDiff) < 0.02) nyDiff = yDiff;
            notification.renderPosX -= nxDiff;
            notification.renderPosY -= nyDiff;

            //DrawableHelper.fill(ms,(int)notification.renderPosX,(int)notification.renderPosY,(int)notification.renderPosX+150,(int)notification.renderPosY+notifHeight, new Color(28, 28, 28, 170).getRGB());
            Renderer.fill(new Color(28, 28, 28, 170), notification.renderPosX, notification.renderPosY, notification.renderPosX + 150, notification.renderPosY + notifHeight);
            Renderer.fill(new Color(47, 47, 47, 211), notification.renderPosX, notification.renderPosY, notification.renderPosX + (150 * timeRemaining), notification.renderPosY + notifHeight);
            int currentYOffsetText = 2 + 9;
            Atomic.client.textRenderer.draw(ms, notification.title, (int) notification.renderPosX + 2, (int) notification.renderPosY + 2, 0xFFFFFF);
            for (String content : notification.contents) {
                Atomic.client.textRenderer.draw(ms, content, (int) notification.renderPosX + 2, (int) notification.renderPosY + currentYOffsetText, 0xFFFFFF);
                currentYOffsetText += 9;
            }
            //if (currentYOffset == 0) currentYOffset += notifHeight + 2;
        }
    }
}
