package me.zeroX150.atomic.feature.gui.particles;

import me.zeroX150.atomic.Atomic;
import net.minecraft.util.math.Vec2f;

import java.awt.*;

public class Particle {
    double x;
    double y;
    Vec2f velocity;
    float brightness;
    Color color;

    public Particle(Vec2f initialPos, Vec2f vel, Color color) {
        x = initialPos.x;
        y = initialPos.y;
        this.color = color;
        brightness = 0f;
        velocity = vel;
    }

    public void move() {
        double nx = x + velocity.x;
        double ny = y + velocity.y;
        int w = Atomic.client.getWindow().getScaledWidth();
        int h = Atomic.client.getWindow().getScaledHeight();
        if (nx > w || nx < 0) {
            velocity = velocity.add(new Vec2f(-velocity.x * 2, 0));
        } else x = nx;
        if (ny > h || ny < 0) velocity = velocity.add(new Vec2f(0, -velocity.y * 2));
        else y = ny;
        if (nx > w) x = w - 1;
        if (nx < 0) x = 1;
        if (ny > h) y = h - 1;
        if (ny < 0) y = 1;
        brightness += (Math.random() - 0.5) / 8;
        brightness = brightness > 0.3f ? 0.3f : (brightness < 0 ? 0 : brightness);
    }
}
