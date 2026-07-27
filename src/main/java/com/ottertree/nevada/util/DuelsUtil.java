package com.ottertree.nevada.util;

import net.minecraft.client.Minecraft;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;

public class DuelsUtil {
    public static boolean inDuels() {
        try {
            return Minecraft.getMinecraft().theWorld.getScoreboard().getObjectiveInDisplaySlot(1).getDisplayName().replaceAll("§.", "").equals("DUELS");
        }
        catch (Exception e) {
            return false;
        }
    }

    public static boolean inLobby() {
        try {
            Scoreboard scoreboard = Minecraft.getMinecraft().theWorld.getScoreboard();
            for (Score score : scoreboard.getSortedScores(scoreboard.getObjectiveInDisplaySlot(1))) {
                if (inDuels() && ScorePlayerTeam.formatPlayerName(scoreboard.getPlayersTeam(score.getPlayerName()), score.getPlayerName()).contains("Duel other players with:"))
                    return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    public static boolean inGame() {
        return inDuels() && !inLobby();
    }

    public static boolean inPregame() {
        try {
            Scoreboard scoreboard = Minecraft.getMinecraft().theWorld.getScoreboard();
            for (Score score : scoreboard.getSortedScores(scoreboard.getObjectiveInDisplaySlot(1))) {
                if (inDuels() && ScorePlayerTeam.formatPlayerName(scoreboard.getPlayersTeam(score.getPlayerName()), score.getPlayerName()).contains("Map:"))
                    return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    public static boolean inActiveGame() {
        return inDuels() && inGame() && !inPregame();
    }

    public static String colorWLR(float rawWLR) {
        String val = String.format("%.2f", rawWLR);
        String code = "§7" + val;
        if (rawWLR >= 1) code = "§f" + val;
        if (rawWLR >= 2) code = "§a" + val;
        if (rawWLR >= 3.5) code = "§2" + val;
        if (rawWLR >= 7) code = "§e" + val;
        if (rawWLR >= 10) code = "§6" + val;
        if (rawWLR >= 20) code = "§c" + val;
        if (rawWLR >= 35) code = "§4" + val;
        if (rawWLR >= 50) code = "§d" + val;
        if (rawWLR >= 100) code = "§5" + val;
        return code;
    }
    public static String colorWins(int wins) {
        String code = "§7" + wins;
        if (wins >= 150) code = "§f" + wins;
        if (wins >= 300) code = "§a" + wins;
        if (wins >= 450) code = "§2" + wins;
        if (wins >= 1500) code = "§e" + wins;
        if (wins >= 3000) code = "§6" + wins;
        if (wins >= 5000) code = "§c" + wins;
        if (wins >= 7500) code = "§4" + wins;
        if (wins >= 15000) code = "§d" + wins;
        if (wins >= 30000) code = "§5" + wins;
        return code;
    }
    public static String colorWS(int ws) {
        String code = "§7" + ws;
        if (ws >= 5) code = "§f" + ws;
        if (ws >= 10) code = "§a" + ws;
        if (ws >= 20) code = "§2" + ws;
        if (ws >= 50) code = "§e" + ws;
        if (ws >= 68) code = "§6" + ws;
        if (ws >= 80) code = "§c" + ws;
        if (ws >= 100) code = "§4" + ws;
        if (ws >= 120) code = "§d" + ws;
        if (ws >= 150) code = "§5" + ws;
        return code;
    }
    public static String colorBWS(int bws) {
        String code = "§7" + bws;
        if (bws >= 5) code = "§f" + bws;
        if (bws >= 10) code = "§a" + bws;
        if (bws >= 20) code = "§2" + bws;
        if (bws >= 50) code = "§e" + bws;
        if (bws >= 71) code = "§6" + bws;
        if (bws >= 90) code = "§c" + bws;
        if (bws >= 180) code = "§4" + bws;
        if (bws >= 350) code = "§d" + bws;
        if (bws >= 500) code = "§5" + bws;
        return code;
    }
}
