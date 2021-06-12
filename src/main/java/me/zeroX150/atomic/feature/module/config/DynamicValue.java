package me.zeroX150.atomic.feature.module.config;

import java.util.ArrayList;
import java.util.List;

public class DynamicValue<T> {
    private final String key;
    protected T value;
    List<SelectorRunnable> selectors = new ArrayList<>();

    public DynamicValue(String key, T value) {
        this.key = key;
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    @SuppressWarnings("unchecked") // fucking monkey shut up
    public void setValue(Object value) {
        if (getType() != value.getClass()) return;
        this.value = (T) value;
    }

    public String getKey() {
        return key;
    }

    public Class<?> getType() {
        return value.getClass();
    }

    public boolean shouldShow() {
        for (SelectorRunnable selector : selectors) {
            if (!selector.shouldShow()) return false;
        }
        return true;
    }

    public void showOnlyIf(SelectorRunnable runnable) {
        selectors.add(runnable);
    }

    public interface SelectorRunnable {
        boolean shouldShow();
    }

}
