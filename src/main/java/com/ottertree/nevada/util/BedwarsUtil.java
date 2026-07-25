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
}
