package me.zeroX150.atomic.feature.module.config;

import com.google.common.collect.Lists;

import java.util.List;

public class MultiValue extends DynamicValue<String> {
    List<String> possible;

    public MultiValue(String key, String value, String... values) {
        super(key, value);
        this.possible = Lists.newArrayList(values);
    }

    public int getIndex() {
        return possible.indexOf(this.value);
    }

    @Override
    public void setValue(Object value) {
        if (!(value instanceof String)) return;
        if (!possible.contains(value)) return;
        this.value = (String) value;
    }

    public void cycle() {
        int next = getIndex() + 1;
        if (next >= possible.size()) next = 0;
        this.setValue(possible.get(next));
    }

    @SuppressWarnings("unused")
    public List<String> getPossible() {
        return possible;
    }
}
