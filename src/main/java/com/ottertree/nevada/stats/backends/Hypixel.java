package com.ottertree.nevada.stats.backends;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ottertree.nevada.NevadaConfig;
import com.ottertree.nevada.api.MojangAPI;
import com.ottertree.nevada.stats.PlayerProfile;
import com.ottertree.nevada.stats.StatCheckAPI;

public class Hypixel implements StatCheckAPI {
    public static PlayerProfile getPlayerProfile(String identifier) throws IOException {
        PlayerProfile profile = new PlayerProfile();

        String uuid = "";
        String displayName = "";
        try { JsonObject mojangInfo = MojangAPI.getPlayerInfo(identifier); uuid = mojangInfo.get("id").getAsString(); displayName = mojangInfo.get("name").getAsString(); }
        catch (IOException e) { throw e; }

        JsonObject stats = new JsonObject();
        try { stats = getPlayerStatsRaw(uuid); }
        catch (IOException e) { throw e; }

        if (!stats.has("player") || !stats.get("player").isJsonObject()) { throw new IOException("Player data not found"); }
        if (!stats.get("player").getAsJsonObject().has("stats")) { throw new IOException("Player data not found"); }
        if (!stats.get("player").getAsJsonObject().get("stats").getAsJsonObject().has("Bedwars")) { throw new IOException("Player data not found"); }

        JsonObject playerStats = stats.get("player").getAsJsonObject();
        try { profile.bedwarsFinalKills = playerStats.get("stats").getAsJsonObject().get("Bedwars").getAsJsonObject().get("final_kills_bedwars").getAsInt(); } catch (NullPointerException ignore) {}
        try { profile.bedwarsFinalDeaths = playerStats.get("stats").getAsJsonObject().get("Bedwars").getAsJsonObject().get("final_deaths_bedwars").getAsInt(); } catch (NullPointerException ignore) {}
        try { profile.bedwarsWins = playerStats.get("stats").getAsJsonObject().get("Bedwars").getAsJsonObject().get("wins_bedwars").getAsInt(); } catch (NullPointerException ignore) {}
        try { profile.bedwarsLosses = playerStats.get("stats").getAsJsonObject().get("Bedwars").getAsJsonObject().get("losses_bedwars").getAsInt(); } catch (NullPointerException ignore) {}
        try { profile.bedwarsBedsBroken = playerStats.get("stats").getAsJsonObject().get("Bedwars").getAsJsonObject().get("beds_broken_bedwars").getAsInt(); } catch (NullPointerException ignore) {}
        try { profile.bedwarsBedsLost = playerStats.get("stats").getAsJsonObject().get("Bedwars").getAsJsonObject().get("beds_lost_bedwars").getAsInt(); } catch (NullPointerException ignore) {}
        try { profile.bedwarsKills = playerStats.get("stats").getAsJsonObject().get("Bedwars").getAsJsonObject().get("kills_bedwars").getAsInt(); } catch (NullPointerException ignore) {}
        try { profile.bedwarsDeaths = playerStats.get("stats").getAsJsonObject().get("Bedwars").getAsJsonObject().get("deaths_bedwars").getAsInt(); } catch (NullPointerException ignore) {}
        try { profile.bedwarsLevel = playerStats.get("achievements").getAsJsonObject().get("bedwars_level").getAsInt(); } catch (NullPointerException ignore) {}
        profile.bedwarsLevelFormatted = getPlayerBedwarsStarsFormatted(stats);

        profile.uuid = uuid;
        profile.displayName = displayName;
        profile.hypixelDisplayName = formatNameWithRank(stats);
        profile.hypixelStatus = getPlayerStatus(uuid);

        return profile;
    }

    private static JsonObject getPlayerStatusRaw(String uuid) throws IOException {
        URL url = new URL("https://api.hypixel.net/v2/status?uuid=" + uuid);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("API-Key", NevadaConfig.APIKey_Hypixel);

        if (conn.getResponseCode() != 200) {
            if (conn.getResponseCode() == 403) throw new IOException("Invalid Hypixel API Key");

            throw new IOException("Hypixel API error: " + conn.getResponseCode());
        }

        try (Reader reader = new InputStreamReader(conn.getInputStream())) {
            return new JsonParser().parse(reader).getAsJsonObject();
        }
    }

    private static JsonObject getPlayerStatsRaw(String uuid) throws IOException {
        URL url = new URL("https://api.hypixel.net/v2/player?uuid=" + uuid);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("API-Key", NevadaConfig.APIKey_Hypixel);

        if (conn.getResponseCode() != 200) {
            if (conn.getResponseCode() == 403) throw new IOException("Invalid Hypixel API Key");

            throw new IOException("Hypixel API error: " + conn.getResponseCode());
        }

        try (Reader reader = new InputStreamReader(conn.getInputStream())) {
            return new JsonParser().parse(reader).getAsJsonObject();
        }
    }

    private static String getPlayerStatus(String uuid) {
        try {
            JsonObject response = getPlayerStatusRaw(uuid);
            if (!response.get("success").getAsBoolean()) {
                return "Hypixel API Failure";
            }

            JsonObject session = response.get("session").getAsJsonObject();
            if (!session.has("online") || !session.get("online").getAsBoolean() || !session.has("gameType")) {
                return "Hidden / Offline";
            }

            if (session.has("gameType")) {
                String status;
                if (session.get("gameType").getAsString().equals("BEDWARS")) {
                    String mode = "";
                    switch (session.get("mode").getAsString()) {
                        case "BEDWARS_EIGHT_ONE": mode = " Solo"; break;
                        case "BEDWARS_EIGHT_TWO": mode = " Doubles"; break;
                        case "BEDWARS_FOUR_THREE": mode = " 3v3v3v3"; break;
                        case "BEDWARS_FOUR_FOUR": mode = " 4v4v4v4"; break;
                        case "BEDWARS_TWO_FOUR": mode = " 4v4"; break;
                        case "BEDWARS_EIGHT_TWO_RUSH": mode = " Doubles Rush"; break;
                        case "BEDWARS_FOUR_FOUR_RUSH": mode = " 4v4v4v4 Rush"; break;
                        case "BEDWARS_EIGHT_TWO_ULTIMATE": mode = " Doubles Ultimate"; break;
                        case "BEDWARS_FOUR_FOUR_ULTIMATE": mode = " 4v4v4v4 Ultimate"; break;
                        case "BEDWARS_EIGHT_TWO_VOIDLESS": mode = " Doubles Voidless"; break;
                        case "BEDWARS_FOUR_FOUR_VOIDLESS": mode = " 4v4v4v4 Voidless"; break;
                        case "BEDWARS_EIGHT_TWO_ARMED": mode = " Doubles Armed"; break;
                        case "BEDWARS_FOUR_FOUR_ARMED": mode = " 4v4v4v4 Armed"; break;
                        case "BEDWARS_EIGHT_TWO_LUCKY": mode = " Doubles Lucky Blocks"; break;
                        case "BEDWARS_FOUR_FOUR_LUCKY": mode = " 4v4v4v4 Lucky Blocks"; break;
                        case "BEDWARS_CAPTURE": mode = " Capture"; break;
                        case "BEDWARS_CASTLE": mode = " Castle"; break;
                    }
                    status = cleanGameName(session.get("gameType").getAsString() + mode);
                }
                status = cleanGameName(session.get("gameType").getAsString());

                if (session.has("mode") && session.get("mode").getAsString().equals("LOBBY")) status += " Lobby";

                if (session.has("map")) status += " - " + session.get("map").getAsString();

                return status;
            }
        }
        catch (IOException e) { return "Hypixel API Error"; }

        return "Error";
    }

    private static String cleanGameName(String game) {
        switch (game) {
            case "MAIN": return "Main";
            case "QUAKECRAFT": return "Quakecraft";
            case "WALLS": return "Walls";
            case "PAINTBALL": return "Paintball";
            case "SURVIVAL_GAMES": return "Blitz Survival Games";
            case "TNTGAMES": return "TNT Games";
            case "VAMPIREZ": return "VampireZ";
            case "WALLS3": return "Mega Walls";
            case "ARCADE": return "Arcade";
            case "ARENA": return "Arena";
            case "UHC": return "UHC Champions";
            case "MCGO": return "Cops and Crims";
            case "BATTLEGROUND": return "Warlords";
            case "SUPER_SMASH": return "Smash Heroes";
            case "GINGERBREAD": return "Turbo Kart Racers";
            case "HOUSING": return "Housing";
            case "SKYWARS": return "Skywars";
            case "TRUE_COMBAT": return "Crazy Walls";
            case "SPEED_UHC": return "Speed UHC";
            case "SKYCLASH": return "SkyClash";
            case "LEGACY": return "Classic Games";
            case "PROTOTYPE": return "Prototype";
            case "BEDWARS": return "Bedwars";
            case "MURDER_MYSTERY": return "Murder Mystery";
            case "BUILD_BATTLE": return "Build Battle";
            case "DUELS": return "Duels";
            case "SKYBLOCK": return "Skyblock";
            case "PIT": return "The Pit";
            case "REPLAY": return "Replay";
            case "SMP": return "SMP";
            case "WOOL_GAMES": return "Wool Wars";
        }

        return "";
    }

    private static String formatNameWithRank(JsonObject stats) {
        JsonObject player = stats.get("player").getAsJsonObject();
        String name = player.get("displayname").getAsString();

        if (player.has("prefix")) return player.get("prefix").getAsString() + " " + name;
        if (player.has("rank") && !player.get("rank").getAsString().equals("NONE") && !player.get("rank").getAsString().equals("NORMAL")) {
            switch (player.get("rank").getAsString()) {
                case "YOUTUBER": return "§r§c[§fYOUTUBE§c] " + name;
                case "ADMIN": return "§r§c[ADMIN] " + name;
                case "MODERATOR": return "§r§2[MOD] " + name;
                case "HELPER": return "§r§9[HELPER] " + name;
                case "STAFF": return "§r§c[§6ዞ§c] " + name;
                default: return "§r§c[" + player.get("rank").getAsString() + "] " + name;
            }
        }
        if (player.has("monthlyPackageRank") && player.get("monthlyPackageRank").getAsString().equals("SUPERSTAR")) {
            String color = player.has("monthlyRankColor") ? colorNameToCode(player.get("monthlyRankColor").getAsString().toUpperCase()) : "§6";
            String plusColor = player.has("rankPlusColor") ? colorNameToCode(player.get("rankPlusColor").getAsString().toUpperCase()) : "§c";
            return "§r" + color + "[MVP" + plusColor + "++" + color + "] " + name;
        }
        if (player.has("newPackageRank")) {
            String plusColor = player.has("rankPlusColor") ? colorNameToCode(player.get("rankPlusColor").getAsString().toUpperCase()) : "§c";

            switch (player.get("newPackageRank").getAsString()) {
                case "VIP": return "§r§a[VIP] " + name;
                case "VIP_PLUS": return "§r§a[VIP" + "§6+" + "§a] " + name;
                case "MVP": return "§r§b[MVP] " + name;
                case "MVP_PLUS": return "§r§b[MVP" + plusColor + "+" + "§b] " + name;
            }
        }
        if (player.has("packageRank")) {
            String plusColor = player.has("rankPlusColor") ? colorNameToCode(player.get("rankPlusColor").getAsString().toUpperCase()) : "§c";

            switch (player.get("packageRank").getAsString()) {
                case "VIP": return "§r§a[VIP] " + name;
                case "VIP_PLUS": return "§r§a[VIP" + "§6+" + "§a] " + name;
                case "MVP": return "§r§b[MVP] " + name;
                case "MVP_PLUS": return "§r§b[MVP" + plusColor + "+" + "§b] " + name;
            }
        }

        return "§r§7" + name;
    }
    private static String colorNameToCode(String name) {
        return name.replace("BLACK", "§0").replace("DARK_BLUE", "§1").replace("DARK_GREEN", "§2").replace("DARK_AQUA", "§3").replace("DARK_RED", "§4").replace("DARK_PURPLE", "§5").replace("GOLD", "§6").replace("GRAY", "§7").replace("DARK_GRAY", "§8").replace("BLUE", "§9").replace("GREEN", "§a").replace("AQUA", "§b").replace("RED", "§c").replace("LIGHT_PURPLE", "§d").replace("YELLOW", "§6").replace("WHITE", "§f");
    }

    private static String getPlayerBedwarsStarsFormatted(JsonObject stats) {
        int stars = stats.get("player").getAsJsonObject().get("achievements").getAsJsonObject().get("bedwars_level").getAsInt();

        return "§r§7[" + stars + "✫]";
    }
}
