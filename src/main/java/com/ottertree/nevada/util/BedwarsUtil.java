package com.ottertree.nevada.util;

import net.minecraft.client.Minecraft;

public class BedwarsUtil {
    public static boolean inBedwars() {
        return Minecraft.getMinecraft().theWorld.getScoreboard().getObjectiveInDisplaySlot(1).getDisplayName().replaceAll("§.", "").equals("BED WARS");
    }

    public static String formatStars(int level) {
        return "§r§7[" + level + "✫]";
    }

    public static String colorFKDR(float fkdr) {
        String code = "§7";
        if (fkdr >= 1) code = "§f";
        if (fkdr >= 3) code = "§a";
        if (fkdr >= 5) code = "§2";
        if (fkdr >= 7) code = "§e";
        if (fkdr >= 10) code = "§6";
        if (fkdr >= 20) code = "§c";
        if (fkdr >= 30) code = "§4";
        if (fkdr >= 50) code = "§d";
        if (fkdr >= 100) code = "§5";
        return code;
    }
    public static String colorFinals(int finals) {
        String code = "§7";
        if (finals >= 500) code = "§f";
        if (finals >= 1000) code = "§a";
        if (finals >= 2500) code = "§2";
        if (finals >= 5000) code = "§e";
        if (finals >= 7500) code = "§6";
        if (finals >= 15000) code = "§c";
        if (finals >= 25000) code = "§4";
        if (finals >= 50000) code = "§d";
        if (finals >= 100000) code = "§5";
        return code;
    }
    public static String colorWLR(float wlr) {
        String code = "§7";
        if (wlr >= 0.3) code = "§f";
        if (wlr >= 0.9) code = "§a";
        if (wlr >= 1.5) code = "§2";
        if (wlr >= 2.1) code = "§e";
        if (wlr >= 3) code = "§6";
        if (wlr >= 6) code = "§c";
        if (wlr >= 9) code = "§4";
        if (wlr >= 15) code = "§d";
        if (wlr >= 30) code = "§5";
        return code;
    }
    public static String colorWins(int wins) {
        String code = "§7";
        if (wins >= 150) code = "§f";
        if (wins >= 300) code = "§a";
        if (wins >= 450) code = "§2";
        if (wins >= 1500) code = "§e";
        if (wins >= 2250) code = "§6";
        if (wins >= 4500) code = "§c";
        if (wins >= 7500) code = "§4";
        if (wins >= 15000) code = "§d";
        if (wins >= 30000) code = "§5";
        return code;
    }
}
