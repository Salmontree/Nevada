package com.ottertree.nevada.util;

import net.minecraft.client.Minecraft;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;

public class BedwarsUtil {
    public static boolean inBedwars() {
        try {
            return Minecraft.getMinecraft().theWorld.getScoreboard().getObjectiveInDisplaySlot(1).getDisplayName().replaceAll("§.", "").equals("BED WARS");
        }
        catch (Exception e) {
            return false;
        }
    }

    public static boolean inLobby() {
        try {
            Scoreboard scoreboard = Minecraft.getMinecraft().theWorld.getScoreboard();
            for (Score score : scoreboard.getSortedScores(scoreboard.getObjectiveInDisplaySlot(1))) {
                if (inBedwars() && ScorePlayerTeam.formatPlayerName(scoreboard.getPlayersTeam(score.getPlayerName()), score.getPlayerName()).contains("Level:"))
                    return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    public static boolean inGame() {
        return inBedwars() && !inLobby();
    }

    public static boolean inPregame() {
        try {
            Scoreboard scoreboard = Minecraft.getMinecraft().theWorld.getScoreboard();
            for (Score score : scoreboard.getSortedScores(scoreboard.getObjectiveInDisplaySlot(1))) {
                if (inBedwars() && ScorePlayerTeam.formatPlayerName(scoreboard.getPlayersTeam(score.getPlayerName()), score.getPlayerName()).contains("Map:"))
                    return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    public static boolean inActiveGame() {
        return inBedwars() && inGame() && !inPregame();
    }
    
    public static String formatLevel(int level) {
        return "§7[" + level + "✫]";
    }

    public static String colorFKDR(float rawFkdr) {
        String fkdr = String.format("%.2f", rawFkdr);
        String code = "§7" + fkdr;
        if (rawFkdr >= 1) code = "§f" + fkdr;
        if (rawFkdr >= 3) code = "§a" + fkdr;
        if (rawFkdr >= 5) code = "§2" + fkdr;
        if (rawFkdr >= 7) code = "§e" + fkdr;
        if (rawFkdr >= 10) code = "§6" + fkdr;
        if (rawFkdr >= 20) code = "§c" + fkdr;
        if (rawFkdr >= 30) code = "§4" + fkdr;
        if (rawFkdr >= 50) code = "§d" + fkdr;
        if (rawFkdr >= 100) code = "§5" + fkdr;
        return code;
    }
    public static String colorFinals(int finals) {
        String code = "§7" + finals;
        if (finals >= 500) code = "§f" + finals;
        if (finals >= 1000) code = "§a" + finals;
        if (finals >= 2500) code = "§2" + finals;
        if (finals >= 5000) code = "§e" + finals;
        if (finals >= 7500) code = "§6" + finals;
        if (finals >= 15000) code = "§c" + finals;
        if (finals >= 25000) code = "§4" + finals;
        if (finals >= 50000) code = "§d" + finals;
        if (finals >= 100000) code = "§5" + finals;
        return code;
    }
    public static String colorWLR(float rawWlr) {
        String wlr = String.format("%.2f", rawWlr);
        String code = "§7" + wlr;
        if (rawWlr >= 0.3) code = "§f" + wlr;
        if (rawWlr >= 0.9) code = "§a" + wlr;
        if (rawWlr >= 1.5) code = "§2" + wlr;
        if (rawWlr >= 2.1) code = "§e" + wlr;
        if (rawWlr >= 3) code = "§6" + wlr;
        if (rawWlr >= 6) code = "§c" + wlr;
        if (rawWlr >= 9) code = "§4" + wlr;
        if (rawWlr >= 15) code = "§d" + wlr;
        if (rawWlr >= 30) code = "§5" + wlr;
        return code;
    }
    public static String colorWins(int wins) {
        String code = "§7" + wins;
        if (wins >= 150) code = "§f" + wins;
        if (wins >= 300) code = "§a" + wins;
        if (wins >= 450) code = "§2" + wins;
        if (wins >= 1500) code = "§e" + wins;
        if (wins >= 2250) code = "§6" + wins;
        if (wins >= 4500) code = "§c" + wins;
        if (wins >= 7500) code = "§4" + wins;
        if (wins >= 15000) code = "§d" + wins;
        if (wins >= 30000) code = "§5" + wins;
        return code;
    }
    public static String colorBBLR(float rawBblr) {
        String bblr = String.format("%.2f", rawBblr);
        String code = "§7" + bblr;
        if (rawBblr >= 0.2) code = "§f" + bblr;
        if (rawBblr >= 0.6) code = "§a" + bblr;
        if (rawBblr >= 1) code = "§2" + bblr;
        if (rawBblr >= 1.4) code = "§e" + bblr;
        if (rawBblr >= 2) code = "§6" + bblr;
        if (rawBblr >= 4) code = "§c" + bblr;
        if (rawBblr >= 6) code = "§4" + bblr;
        if (rawBblr >= 10) code = "§d" + bblr;
        if (rawBblr >= 20) code = "§5" + bblr;
        return code;
    }
    public static String colorBedsBroken(int beds) {
        String code = "§7" + beds;
        if (beds >= 250) code = "§f" + beds;
        if (beds >= 500) code = "§a" + beds;
        if (beds >= 1250) code = "§2" + beds;
        if (beds >= 2500) code = "§e" + beds;
        if (beds >= 3750) code = "§6" + beds;
        if (beds >= 7500) code = "§c" + beds;
        if (beds >= 12500) code = "§4" + beds;
        if (beds >= 25000) code = "§d" + beds;
        if (beds >= 50000) code = "§5" + beds;
        return code;
    }
}
