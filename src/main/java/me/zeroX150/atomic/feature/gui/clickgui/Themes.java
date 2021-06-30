package me.zeroX150.atomic.feature.gui.clickgui;

import java.awt.*;

public class Themes {
    public enum Theme {
        ATOMIC(
                new Palette(
                        new Color(17, 17, 17, 220),
                        new Color(40, 40, 40, 220),
                        new Color(0, 194, 111, 220),
                        new Color(38, 38, 38, 255),
                        new Color(49, 49, 49, 255),
                        Color.WHITE, 4, 2, true)),
        SIGMA(
                new Palette(
                        new Color(0xfafafa),
                        new Color(0x28a6fc),
                        new Color(0x28a6fc),
                        new Color(0xf2f2f2),
                        new Color(0xf2f2f2),
                        new Color(10, 10, 10), 4, 0, false));
        Palette p;

        Theme(Palette palette) {
            this.p = palette;
        }

        public Palette getPalette() {
            return p;
        }
    }

    public static record Palette(Color inactive, Color active, Color l_highlight, Color h_ret, Color h_exp,
                                 Color fontColor, double h_margin, double h_paddingX, boolean centerText) {

    }
}
