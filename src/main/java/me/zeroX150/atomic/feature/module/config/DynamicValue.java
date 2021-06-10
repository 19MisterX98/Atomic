package me.zeroX150.atomic.feature.module.config;

public class DynamicValue<T> {
    private final String key;
    protected T value;

    public DynamicValue(String key, T value) {
        this.key = key;
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public void setValue(Object value) {
        if (value.getClass() != this.getType()) return;
        this.value = (T) value;
    }

    public String getKey() {
        return key;
    }

    public Class<?> getType() {
        return value.getClass();
    }
}
