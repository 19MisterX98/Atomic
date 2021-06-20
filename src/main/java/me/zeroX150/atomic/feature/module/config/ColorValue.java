package me.zeroX150.atomic.feature.module.config;

import me.zeroX150.atomic.helper.Client;

import java.awt.*;

public class ColorValue extends DynamicValue<String> {
    boolean isRGB = false;

    public ColorValue(String key, Color value) {
        super(key, value.getRGB() + "");
    }

    public Color getColor() {
        int v = Integer.parseInt(this.getValue().replaceAll(";", ""));
        return new Color(!isRGB ? v : Client.getCurrentRGB().getRGB());
    }

    public boolean isRGB() {
        return isRGB;
    }

    public void setRGB(boolean RGB) {
        isRGB = RGB;
    }

    @Override
    public String getValue() {
        return super.getValue() + (isRGB ? ";" : "");
    }

    @Override
    public void setValue(Object value) {

        if (!(value instanceof String v)) return;
        boolean isRGB = v.endsWith(";");
        if (isRGB) v = v.replaceAll(";", "");
        Color parsed;
        try {
            parsed = new Color(Integer.parseInt(v));
        } catch (Exception ignored) {
            return;
        }
        this.isRGB = isRGB;
        this.value = parsed.getRGB() + "";
    }
}
