package com.ottertree.nevada.util.bedwars;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BedwarsUpgradesTrapsManager {

    private static final BedwarsUpgradesTrapsManager INSTANCE = new BedwarsUpgradesTrapsManager();

    private int sharpSwords = 0;
    private int reinforcedArmor = 0;
    private int maniacMiner = 0;
    private int cushionedBoots = 0;
    private int forgeLevel = 0;
    private boolean healPool = false;
    private int deadShot = 0;
    private final List<String> activeTraps = new ArrayList<>();

    private static final String[] FORGE_LEVELS = {
        "", "Iron Forge", "Golden Forge", "Emerald Forge", "Molten Forge",
    };

    public static BedwarsUpgradesTrapsManager getInstance() {
        return INSTANCE;
    }

    private BedwarsUpgradesTrapsManager() {}

    public void resetUpgradesAndTraps() {
        sharpSwords = 0;
        reinforcedArmor = 0;
        maniacMiner = 0;
        cushionedBoots = 0;
        forgeLevel = 0;
        healPool = false;
        deadShot = 0;
        activeTraps.clear();
    }

    public void processPurchaseMessage(String message) {
        String cleaned = cleanMessage(message);
        if (!cleaned.toLowerCase().contains("purchased")) return;

        String[] parts = cleaned.split("(?i)purchased");
        if (parts.length < 2) return;

        String item = parts[1].trim();
        item = item.replaceAll("^[\\s\"'`]+|[\\s\"'`]+$", "");
        item = item.replaceAll("[.!?]+$", "").trim();

        if (item.toLowerCase().contains("trap")) {
            activeTraps.add(normalize(item));
            return;
        }
        if (item.toLowerCase().startsWith("sharpened swords")) {
            int level = extractLevel(item);
            sharpSwords = Math.max(sharpSwords, level > 0 ? level : 1);
            return;
        }
        if (item.toLowerCase().startsWith("reinforced armor")) {
            int level = extractLevel(item);
            reinforcedArmor = Math.max(reinforcedArmor, level > 0 ? level : 1);
            return;
        }
        if (item.toLowerCase().startsWith("maniac miner")) {
            int level = extractLevel(item);
            maniacMiner = Math.max(maniacMiner, level > 0 ? level : 1);
            return;
        }
        if (item.toLowerCase().startsWith("cushioned boots") || item.toLowerCase().startsWith("cushioned")) {
            int level = extractLevel(item);
            cushionedBoots = Math.max(cushionedBoots, level > 0 ? level : 1);
            return;
        }
        for (int i = 1; i < FORGE_LEVELS.length; i++) {
            if (item.equalsIgnoreCase(FORGE_LEVELS[i])) {
                forgeLevel = Math.max(forgeLevel, i);
                return;
            }
        }
        if (item.toLowerCase().startsWith("heal pool")) {
            healPool = true;
            return;
        }
        if (item.toLowerCase().startsWith("deadshot")) {
            int level = extractLevel(item);
            deadShot = Math.max(deadShot, level > 0 ? level : 1);
        }
    }

    public void processTrapTriggeredMessage(String message) {
        String cleaned = cleanMessage(message);

        Pattern trapPattern = Pattern.compile("^(.+?)\\s+Trap was set off!$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = trapPattern.matcher(cleaned);
        if (matcher.matches()) {
            removeTrapFromQueue(normalize(matcher.group(1).trim()) + " Trap");
            return;
        }

        if (cleaned.toLowerCase().contains("reveal trap set off")) {
            removeTrapFromQueue("Reveal Trap");
            return;
        }

        Pattern removePattern = Pattern.compile("^Removed\\s+(.+?)\\s+Trap from the queue!$", Pattern.CASE_INSENSITIVE);
        Matcher removeMatcher = removePattern.matcher(cleaned);
        if (removeMatcher.matches()) {
            removeTrapFromQueue(normalize(removeMatcher.group(1).trim()) + " Trap");
        }
    }

    private void removeTrapFromQueue(String trapName) {
        String target = normalize(trapName);
        for (int i = 0; i < activeTraps.size(); i++) {
            if (normalize(activeTraps.get(i)).equalsIgnoreCase(target)) {
                activeTraps.remove(i);
                break;
            }
        }
    }

    private String normalize(String name) {
        return name.replaceAll("\\s+", " ").trim();
    }

    private int extractLevel(String item) {
        if (item.contains("I")) {
            if (item.contains("IV")) return 4;
            if (item.contains("III")) return 3;
            if (item.contains("II")) return 2;
            if (item.contains("I") && !item.contains("V") && !item.contains("X")) return 1;
        }
        Pattern numberPattern = Pattern.compile("([IV1-4])$");
        Matcher matcher = numberPattern.matcher(item.trim());
        if (matcher.find()) {
            String levelStr = matcher.group(1);
            switch (levelStr) {
                case "I": return 1;
                case "II": return 2;
                case "III": return 3;
                case "IV": return 4;
                default:
                    try { return Integer.parseInt(levelStr); }
                    catch (NumberFormatException e) { return 1; }
            }
        }
        return 1;
    }

    private String cleanMessage(String message) {
        return message.replaceAll("(?i)§[0-9A-FK-OR]", "").trim();
    }

    public List<String> getDisplayLinesWithFormatting(
        boolean useShortNames, boolean useRomanNumerals,
        int headingRed, int headingGreen, int headingBlue, int headingAlpha,
        int textRed, int textGreen, int textBlue, int textAlpha
    ) {
        List<String> lines = new ArrayList<>();
        String headingColorCode = "§" + formatColorCode(headingRed, headingGreen, headingBlue);
        String textColorCode = "§" + formatColorCode(textRed, textGreen, textBlue);

        lines.add(headingColorCode + "§lUpgrades:");

        if (sharpSwords == 2) {
            lines.add(textColorCode + (useShortNames ? getShortName("Sharpened Swords") : "Sharpened Swords") + " §7" + (useRomanNumerals ? "II" : "2"));
        } else if (sharpSwords == 1) {
            lines.add(textColorCode + (useShortNames ? getShortName("Sharpened Swords") : "Sharpened Swords"));
        }
        if (reinforcedArmor > 0) {
            lines.add(textColorCode + (useShortNames ? getShortName("Reinforced Armor") : "Reinforced Armor") + " §7" + (useRomanNumerals ? getRomanNumeral(reinforcedArmor) : String.valueOf(reinforcedArmor)));
        }
        if (maniacMiner > 0) {
            lines.add(textColorCode + (useShortNames ? getShortName("Maniac Miner") : "Maniac Miner") + " §7" + (useRomanNumerals ? getRomanNumeral(maniacMiner) : String.valueOf(maniacMiner)));
        }
        if (cushionedBoots > 0) {
            lines.add(textColorCode + (useShortNames ? getShortName("Cushioned Boots") : "Cushioned Boots") + " §7" + (useRomanNumerals ? getRomanNumeral(cushionedBoots) : String.valueOf(cushionedBoots)));
        }
        if (forgeLevel > 0 && forgeLevel < FORGE_LEVELS.length) {
            lines.add(textColorCode + FORGE_LEVELS[forgeLevel]);
        }
        if (healPool) {
            lines.add(textColorCode + "Heal Pool");
        }
        if (deadShot > 0) {
            lines.add(textColorCode + (useShortNames ? getShortName("deadShot") : "deadShot") + " §7" + (useRomanNumerals ? getRomanNumeral(deadShot) : String.valueOf(deadShot)));
        }
        if (sharpSwords == 0 && reinforcedArmor == 0 && maniacMiner == 0 && cushionedBoots == 0 && forgeLevel == 0 && !healPool && deadShot == 0) {
            lines.add(textColorCode + "None");
        }

        lines.add("");
        lines.add(headingColorCode + "§lTraps:");

        if (!activeTraps.isEmpty()) {
            for (String trap : activeTraps) {
                lines.add(textColorCode + (useShortNames ? getShortTrapName(trap) : trap));
            }
        } else {
            lines.add(textColorCode + "None");
        }

        return lines;
    }

    private String formatColorCode(int red, int green, int blue) {
        if (isCloseTo(red, green, blue, 0, 0, 0)) return "0";
        if (isCloseTo(red, green, blue, 0, 0, 170)) return "1";
        if (isCloseTo(red, green, blue, 0, 170, 0)) return "2";
        if (isCloseTo(red, green, blue, 0, 170, 170)) return "3";
        if (isCloseTo(red, green, blue, 170, 0, 0)) return "4";
        if (isCloseTo(red, green, blue, 170, 0, 170)) return "5";
        if (isCloseTo(red, green, blue, 255, 170, 0)) return "6";
        if (isCloseTo(red, green, blue, 170, 170, 170)) return "7";
        if (isCloseTo(red, green, blue, 85, 85, 85)) return "8";
        if (isCloseTo(red, green, blue, 85, 85, 255)) return "9";
        if (isCloseTo(red, green, blue, 85, 255, 85)) return "a";
        if (isCloseTo(red, green, blue, 85, 255, 255)) return "b";
        if (isCloseTo(red, green, blue, 255, 85, 85)) return "c";
        if (isCloseTo(red, green, blue, 255, 85, 255)) return "d";
        if (isCloseTo(red, green, blue, 255, 255, 85)) return "e";
        if (isCloseTo(red, green, blue, 255, 255, 255)) return "f";
        return "f";
    }

    private boolean isCloseTo(int r1, int g1, int b1, int r2, int g2, int b2) {
        return Math.abs(r1 - r2) <= 20 && Math.abs(g1 - g2) <= 20 && Math.abs(b1 - b2) <= 20;
    }

    private String getShortName(String fullName) {
        switch (fullName) {
            case "Sharpened Swords": return "Sharp";
            case "Reinforced Armor": return "Prot";
            case "Cushioned Boots": return "FF";
            case "Maniac Miner": return "Haste";
            default: return fullName;
        }
    }

    private String getShortTrapName(String fullName) {
        String name = fullName;
        if (name.toLowerCase().endsWith(" trap")) {
            name = name.substring(0, name.length() - 5).trim();
        }
        switch (name) {
            case "Miner Fatigue":
            case "Mining Fatigue": return "Fatigue";
            case "Reveal":
            case "Alarm": return "Reveal";
            case "Counter-Offensive":
            case "Counter Offensive":
            case "Counter Offense": return "Jump";
            case "Blindness": return "Blind";
            case "It's a Trap!":
            case "It's a Trap": return "Trap";
            default:
                String lower = name.toLowerCase();
                if (lower.contains("fatigue")) return "Fatigue";
                if (lower.contains("reveal") || lower.contains("alarm")) return "Reveal";
                if (lower.contains("counter")) return "Jump";
                if (lower.contains("blind")) return "Blind";
                return name;
        }
    }

    private String getRomanNumeral(int number) {
        switch (number) {
            case 1: return "I";
            case 2: return "II";
            case 3: return "III";
            case 4: return "IV";
            default: return String.valueOf(number);
        }
    }
}
