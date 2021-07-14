package me.zeroX150.atomic.feature.gui.particles;

import me.zeroX150.atomic.Atomic;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;

import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

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

    public void move(List<Particle> rest) {
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
        brightness = brightness > 0.5f ? 0.5f : (brightness < 0 ? 0 : brightness);
        double perX = x / Atomic.client.getWindow().getScaledWidth();
        double perY = y / Atomic.client.getWindow().getScaledHeight();
        int r = (int) Math.floor(perX * 255);
        int g = Math.abs(255 - r);
        int b = (int) Math.floor(perY * 255);
        color = new Color(r, g, b);

        double velXO = 0;
        double velYO = 0;
        double maxDist = Math.sqrt((w + h) * 3);
        for (Particle particle : rest.stream().filter(v -> v != this).collect(Collectors.toList())) {
            Vec3d p = new Vec3d(particle.x, particle.y, 0);
            Vec3d p1 = new Vec3d(this.x, this.y, 0);
            double d = p1.distanceTo(p);
            if (d < maxDist) {
                double dInv = Math.abs(maxDist - d) / maxDist;
                double xDiff = (this.x - particle.x) / maxDist;
                double yDiff = (this.y - particle.y) / maxDist;
                velXO += xDiff * dInv;
                velYO += yDiff * dInv;
            }
        }
        velocity = velocity.add(new Vec2f((float) velXO / 2, (float) velYO / 2));
        velocity = new Vec2f(MathHelper.clamp(velocity.x, -3, 3), MathHelper.clamp(velocity.y, -3, 3));
        //velocity = new Vec2f(velocity.x/1.01f,velocity.y/1.01f);
    }
}
