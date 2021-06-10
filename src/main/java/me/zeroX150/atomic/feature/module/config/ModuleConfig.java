package me.zeroX150.atomic.feature.module.config;

import me.zeroX150.atomic.Atomic;
import net.minecraft.util.math.MathHelper;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ModuleConfig {
    List<DynamicValue<?>> config = new ArrayList<>();

    public <T> DynamicValue<T> create(String key, T value) {
        DynamicValue<T> nv = new DynamicValue<>(key, value);
        config.add(nv);
        return nv;
    }

    public SliderValue create(String key, double value, double min, double max, int prc) {
        SliderValue sv = new SliderValue(key, MathHelper.clamp(value, min, max), min, max, prc);
        config.add(sv);
        return sv;
    }

    public MultiValue create(String key, String value, String... possible) {
        MultiValue ev = new MultiValue(key, value, possible);
        config.add(ev);
        return ev;
    }

    public DynamicValue<?> get(String key) {
        for (DynamicValue<?> dynamicValue : config) {
            if (dynamicValue.getKey().equalsIgnoreCase(key)) return dynamicValue;
        }
        return null;
    }

    public List<DynamicValue<?>> getAll() {
        config.sort(Comparator.comparingInt(value -> Atomic.client.textRenderer.getWidth(value.getKey())));
        return config;
    }
}
