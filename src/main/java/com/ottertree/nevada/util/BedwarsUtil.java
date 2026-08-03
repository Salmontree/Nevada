package com.ottertree.nevada.util;

import net.minecraft.client.Minecraft;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import scala.collection.mutable.StringBuilder;

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
    

    private static String dynamicLevelColor(String level, String[] colors) {
        StringBuilder result = new StringBuilder();
        
        int i = 0;
        for (char c : level.toCharArray()) {
            result.append(colors[i % colors.length]);
            result.append(c);
            i++;
        }

        return result.toString();
    }
    public static String formatLevel(int level) {
        String formatted;
        if (level >= 5000) formatted = dynamicLevelColor("[" + level + "✥]", new String[]{"§8","§5","§9","§9","§8","§0"});
        else if (level >= 4900) formatted = dynamicLevelColor("[" + level + "✥]", new String[]{"§a","§f","§f","§a","§a","§8"});
        else if (level >= 4800) formatted = dynamicLevelColor("[" + level + "✥]", new String[]{"§7","§c","§e","§e","§b","§3"});
        else if (level >= 4700) formatted = dynamicLevelColor("[" + level + "✥]", new String[]{"§9","§8","§9"}); // Spinel
        else if (level >= 4600) formatted = dynamicLevelColor("[" + level + "✥]", new String[]{"§b","§e","§e","§e","§5","§7"});
        else if (level >= 4500) formatted = dynamicLevelColor("[" + level + "✥]", new String[]{"§f","§b","§3","§3","§3","§3","§3"});
        else if (level >= 4400) formatted = dynamicLevelColor("[" + level + "✥]", new String[]{"§a","§e","§e","§5","§d"});
        else if (level >= 4300) formatted = dynamicLevelColor("[" + level + "✥]", new String[]{"§7","§8","§5","§5","§3"});
        else if (level >= 4200) formatted = dynamicLevelColor("[" + level + "✥]", new String[]{"§9","§3","§b","§f","§7","§7"});
        else if (level >= 4100) formatted = dynamicLevelColor("[" + level + "✥]", new String[]{"§e","§6","§c","§d","§c","§7"});
        else if (level >= 4000) formatted = dynamicLevelColor("[" + level + "✥]", new String[]{"§7","§c","§c","§6","§6","§e"});
        else if (level >= 3900) formatted = dynamicLevelColor("[" + level + "✥]", new String[]{"§c","§a","§a","§3","§9","§7"});
        else if (level >= 3800) formatted = dynamicLevelColor("[" + level + "✥]", new String[]{"§8","§9","§5","§5","§c","§1"});
        else if (level >= 3700) formatted = dynamicLevelColor("[" + level + "✥]", new String[]{"§8","§c","§b","§3","§3"});
        else if (level >= 3600) formatted = dynamicLevelColor("[" + level + "✥]", new String[]{"§a","§a","§b","§9","§9","§8","§8"});
        else if (level >= 3500) formatted = dynamicLevelColor("[" + level + "✥]", new String[]{"§c","§4","§8","§a","§a"});
        else if (level >= 3400) formatted = dynamicLevelColor("[" + level + "✥]", new String[]{"§7","§d","§5","§5","§8","§8","§8"});
        else if (level >= 3300) formatted = dynamicLevelColor("[" + level + "✥]", new String[]{"§9","§9","§d","§c","§c","§8"});
        else if (level >= 3200) formatted = dynamicLevelColor("[" + level + "✥]", new String[]{"§c","§7","§7","§4","§c","§c","§c"});
        else if (level >= 3100) formatted = dynamicLevelColor("[" + level + "✥]", new String[]{"§9","§3","§6","§6","§e"});
        else if (level >= 3000) formatted = dynamicLevelColor("[" + level + "⚝]", new String[]{"§e","§e","§e","§c","§c"});
        else if (level >= 2900) formatted = dynamicLevelColor("[" + level + "⚝]", new String[]{"§b","§7","§7","§9","§9"});
        else if (level >= 2800) formatted = dynamicLevelColor("[" + level + "⚝]", new String[]{"§7","§8","§8","§e","§e"});
        else if (level >= 2700) formatted = dynamicLevelColor("[" + level + "⚝]", new String[]{"§e","§f","§8","§7","§8"});
        else if (level >= 2600) formatted = dynamicLevelColor("[" + level + "⚝]", new String[]{"§c","§c","§c","§d","§d"});
        else if (level >= 2500) formatted = dynamicLevelColor("[" + level + "⚝]", new String[]{"§f","§a","§7","§8","§8"});
        else if (level >= 2400) formatted = dynamicLevelColor("[" + level + "⚝]", new String[]{"§b","§f","§f","§7","§7","§8"});
        else if (level >= 2300) formatted = dynamicLevelColor("[" + level + "⚝]", new String[]{"§5","§d","§d"}); // Dusk
        else if (level >= 2200) formatted = dynamicLevelColor("[" + level + "⚝]", new String[]{"§e","§f","§f","§3","§7"});
        else if (level >= 2100) formatted = dynamicLevelColor("[" + level + "⚝]", new String[]{"§f","§e","§e","§e","§e"});
        else if (level >= 2000) formatted = dynamicLevelColor("[" + level + "✪]", new String[]{"§8","§f","§f","§7","§8","§8"});
        else if (level >= 1900) formatted = "§7[§5" + level + "§✪✫§7]";
        else if (level >= 1800) formatted = "§7[§9" + level + "§1✪§7]";
        else if (level >= 1700) formatted = "§7[§d" + level + "§5✪§7]";
        else if (level >= 1600) formatted = "§7[§c" + level + "§4✪§7]";
        else if (level >= 1500) formatted = "§7[§3" + level + "§9✪§7]";
        else if (level >= 1400) formatted = "§7[§a" + level + "§2✪§7]";
        else if (level >= 1300) formatted = "§7[§b" + level + "§3✪§7]";
        else if (level >= 1200) formatted = "§7[§e" + level + "§6✪§7]";
        else if (level >= 1100) formatted = "§7[§f" + level + "§7✪]";
        else if (level >= 1000) formatted = dynamicLevelColor("[" + level + "✫]", new String[]{"§c","§6","§e","§a","§b","§d","§5"});
        else if (level >= 800) formatted = "§9[" + level + "✫]";
        else if (level >= 700) formatted = "§d[" + level + "✫]";
        else if (level >= 600) formatted = "§4[" + level + "✫]";
        else if (level >= 500) formatted = "§3[" + level + "✫]";
        else if (level >= 400) formatted = "§2[" + level + "✫]";
        else if (level >= 300) formatted = "§b[" + level + "✫]";
        else if (level >= 200) formatted = "§6[" + level + "✫]";
        else if (level >= 100) formatted = "§f[" + level + "✫]";
        else formatted = "§7[" + level + "✫]";
        return formatted;
    }

    public static String colorFKDR(float rawFkdr) {
        String fkdr = String.format("%.2f", rawFkdr);
        String code;
        if (rawFkdr >= 100) code = "§5" + fkdr;
        else if (rawFkdr >= 50) code = "§d" + fkdr;
        else if (rawFkdr >= 30) code = "§4" + fkdr;
        else if (rawFkdr >= 20) code = "§c" + fkdr;
        else if (rawFkdr >= 10) code = "§6" + fkdr;
        else if (rawFkdr >= 7) code = "§e" + fkdr;
        else if (rawFkdr >= 5) code = "§2" + fkdr;
        else if (rawFkdr >= 3) code = "§a" + fkdr;
        else code = "§f" + fkdr;
        return code;
    }
    public static String colorFinals(int finals) {
        String code;
        if (finals >= 100000) code = "§5" + finals;
        else if (finals >= 50000) code = "§d" + finals;
        else if (finals >= 25000) code = "§4" + finals;
        else if (finals >= 15000) code = "§c" + finals;
        else if (finals >= 7500) code = "§6" + finals;
        else if (finals >= 5000) code = "§e" + finals;
        else if (finals >= 2500) code = "§2" + finals;
        else if (finals >= 1000) code = "§a" + finals;
        else code = "§f" + finals;
        return code;
    }
    public static String colorWLR(float rawWlr) {
        String wlr = String.format("%.2f", rawWlr);
        String code;
        if (rawWlr >= 30) code = "§5" + wlr;
        else if (rawWlr >= 15) code = "§d" + wlr;
        else if (rawWlr >= 9) code = "§4" + wlr;
        else if (rawWlr >= 6) code = "§c" + wlr;
        else if (rawWlr >= 3) code = "§6" + wlr;
        else if (rawWlr >= 2.1) code = "§e" + wlr;
        else if (rawWlr >= 1.5) code = "§2" + wlr;
        else if (rawWlr >= 0.9) code = "§a" + wlr;
        else code = "§f" + wlr;
        return code;
    }
    public static String colorWins(int wins) {
        String code;
        if (wins >= 30000) code = "§5" + wins;
        else if (wins >= 15000) code = "§d" + wins;
        else if (wins >= 7500) code = "§4" + wins;
        else if (wins >= 4500) code = "§c" + wins;
        else if (wins >= 2250) code = "§6" + wins;
        else if (wins >= 1500) code = "§e" + wins;
        else if (wins >= 450) code = "§2" + wins;
        else if (wins >= 300) code = "§a" + wins;
        else code = "§f" + wins;
        return code;
    }
    public static String colorBBLR(float rawBblr) {
        String bblr = String.format("%.2f", rawBblr);
        String code;
        if (rawBblr >= 20) code = "§5" + bblr;
        else if (rawBblr >= 10) code = "§d" + bblr;
        else if (rawBblr >= 6) code = "§4" + bblr;
        else if (rawBblr >= 4) code = "§c" + bblr;
        else if (rawBblr >= 2) code = "§6" + bblr;
        else if (rawBblr >= 1.4) code = "§e" + bblr;
        else if (rawBblr >= 1) code = "§2" + bblr;
        else if (rawBblr >= 0.6) code = "§a" + bblr;
        else code = "§f" + bblr;
        return code;
    }
    public static String colorBedsBroken(int beds) {
        String code;
        if (beds >= 50000) code = "§5" + beds;
        else if (beds >= 25000) code = "§d" + beds;
        else if (beds >= 12500) code = "§4" + beds;
        else if (beds >= 7500) code = "§c" + beds;
        else if (beds >= 3750) code = "§6" + beds;
        else if (beds >= 2500) code = "§e" + beds;
        else if (beds >= 1250) code = "§2" + beds;
        else if (beds >= 500) code = "§a" + beds;
        else code = "§f" + beds;
        return code;
    }
}
