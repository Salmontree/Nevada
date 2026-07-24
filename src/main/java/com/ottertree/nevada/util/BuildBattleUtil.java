package com.ottertree.nevada.util;

public class BuildBattleUtil {
    public static String formatScore(int score) {
        String result = "§fRookie";
        if (score >= 100) result = "§7Untrained";
        if (score >= 250) result = "§8Amateur";
        if (score >= 500) result = "§aProspect";
        if (score >= 1000) result = "§2Apprentice";
        if (score >= 2000) result = "§bExperienced";
        if (score >= 3500) result = "§3Seasoned";
        if (score >= 5000) result = "§9Trained";
        if (score >= 7500) result = "§1Skilled";
        if (score >= 10000) result = "§5Talented";
        if (score >= 15000) result = "§dProfessional";
        if (score >= 20000) result = "§cArtisan";
        if (score >= 30000) result = "§4Expert";
        if (score >= 50000) result = "§6Master";
        if (score >= 100000) result = "§l§aLegend";
        if (score >= 200000) result = "§l§bGrandmaster";
        if (score >= 300000) result = "§l§dCelestial";
        if (score >= 400000) result = "§l§cDivine";
        if (score >= 500000) result = "§l§6Ascended";
        return "§f☺ " + result;
    }
}
