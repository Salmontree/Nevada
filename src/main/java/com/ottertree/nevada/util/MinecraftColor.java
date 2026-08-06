package com.ottertree.nevada.util;

public class MinecraftColor {
    private static final int[][] COLORS = {
        {0, 0, 0}, {0, 0, 170}, {0, 170, 0}, {0, 170, 170},
        {170, 0, 0}, {170, 0, 170}, {255, 170, 0}, {170, 170, 170},
        {85, 85, 85}, {85, 85, 255}, {85, 255, 85}, {85, 255, 255},
        {255, 85, 85}, {255, 85, 255}, {255, 255, 85}, {255, 255, 255}
    };

    private final int red, green, blue;

    private MinecraftColor(int red, int green, int blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public static MinecraftColor fromIndex(int index) {
        if (index < 0 || index >= COLORS.length) index = 15;
        int[] c = COLORS[index];
        return new MinecraftColor(c[0], c[1], c[2]);
    }

    public int getRed() { return red; }
    public int getGreen() { return green; }
    public int getBlue() { return blue; }
}
