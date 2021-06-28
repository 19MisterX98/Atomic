package me.zeroX150.atomic.feature.gui.particles;

import me.zeroX150.atomic.Atomic;
import me.zeroX150.atomic.helper.Renderer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ParticleManager {
    List<Particle> particles = new ArrayList<>();

    public ParticleManager(int amount) {
        int w = Atomic.client.getWindow().getScaledWidth();
        int h = Atomic.client.getWindow().getScaledHeight();
        Random r = new Random();
        for (int i = 0; i < amount; i++) {
            particles.add(new Particle(new Vec2f(r.nextInt(w - 2) + 1, r.nextInt(h - 2) + 1), new Vec2f((float) Math.random() - 0.5f, (float) Math.random() - 0.5f).multiply(3f), Color.WHITE));
        }
    }

    public void tick() {
        particles.forEach(Particle::move);
    }

    public void render() {
        int w = Atomic.client.getWindow().getScaledWidth();
        int h = Atomic.client.getWindow().getScaledHeight();
        for (Particle particle : particles) {
            Renderer.fill(Renderer.modify(particle.color, -1, -1, -1, (int) MathHelper.clamp(particle.brightness * 255, 0, 255)), particle.x - 0.5, particle.y - 0.5, particle.x + .5, particle.y + .5);
            for (Particle particle1 : particles) {
                Vec2f v = new Vec2f((float) particle1.x, (float) particle1.y);
                Vec2f v1 = new Vec2f((float) particle.x, (float) particle.y);
                double dist = v1.distanceSquared(v);
                double md = 3 * (w + h);
                if (dist < md) {
                    double dCalc = dist / md;
                    double dCalcR = Math.abs(1 - dCalc);
                    Renderer.gradientLineScreen(Renderer.modify(particle.color, -1, -1, -1, (int) MathHelper.clamp(particle.brightness * 255 * dCalcR, 0, 255)), Renderer.modify(particle1.color, -1, -1, -1, (int) MathHelper.clamp(particle1.brightness * 255 * dCalcR, 0, 255)), particle.x, particle.y, particle1.x, particle1.y);
                }
            }
        }
    }
}
