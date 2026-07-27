package com.ottertree.nevada.api;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.google.common.util.concurrent.RateLimiter;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.google.gson.JsonObject;
import com.ottertree.nevada.Nevada;
import com.ottertree.nevada.cache.PlayerProfileCache;
import com.ottertree.nevada.data.NevadaException;
import com.ottertree.nevada.data.PlayerProfile;
import com.ottertree.nevada.util.PlayerUtil;

import net.hypixel.api.HypixelAPI;
import net.hypixel.api.apache.ApacheHttpClient;
import net.hypixel.api.http.HypixelHttpClient;
import net.hypixel.api.reply.GuildReply;
import net.hypixel.api.reply.PlayerReply.Player;

public class Hypixel {
    public static final Hypixel INSTANCE = new Hypixel();

    private HypixelHttpClient client;
    private HypixelAPI api;

    private String cachedKey;

    private final ExecutorService executor = Executors.newFixedThreadPool(12, new ThreadFactoryBuilder().setNameFormat("hypixel-api-%d").setDaemon(true).build());
    private final RateLimiter rateLimiter = RateLimiter.create(7.0);
    private final ConcurrentHashMap<String, CompletableFuture<PlayerProfile>> pendingPlayerRequests = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, CompletableFuture<GuildReply>> pendingGuildRequests = new ConcurrentHashMap<>();

    public Hypixel() {
        refresh();
    }

    private void refresh() {
        cachedKey = Nevada.config.APIKeys_Hypixel;
        client = new ApacheHttpClient(UUID.fromString(Nevada.config.APIKeys_Hypixel));
        api = new HypixelAPI(client);
    }

    public CompletableFuture<PlayerProfile> getPlayerProfileFromName(String name) {
        return PlayerUtil.getPlayerUUID(name).thenCompose(this::getPlayerProfileFromUUID);
    }

    public CompletableFuture<PlayerProfile> getPlayerProfileFromUUID(String uuid) {
        if (uuid == null || uuid.isEmpty()) {
            CompletableFuture<PlayerProfile> failed = new CompletableFuture<>();
            failed.completeExceptionally(new NevadaException("Player not found"));
            return failed;
        }
        if (Nevada.config.APIKeys_Hypixel != cachedKey) { refresh(); }
        if (Nevada.config.APIKeys_Hypixel.isEmpty()) {
            CompletableFuture<PlayerProfile> failed = new CompletableFuture<>();
            failed.completeExceptionally(new NevadaException("Invalid Hypixel API key"));
            return failed;
        }

        PlayerProfile cachedProfile = PlayerProfileCache.INSTANCE.getProfile(uuid);
        if (cachedProfile != null) { return CompletableFuture.completedFuture(cachedProfile); }

        return pendingPlayerRequests.computeIfAbsent(uuid, id ->
            CompletableFuture.supplyAsync(() -> { rateLimiter.acquire(); return null; }, executor)
                .thenCompose(ignored -> api.getPlayerByUuid(id))
                .thenApply(playerReply -> { if (!playerReply.isSuccess()) throw new CompletionException(new NevadaException(playerReply.getCause())); return buildProfile(id, playerReply.getPlayer());  })
                .handle((profile, e) -> { if (e != null && e.getMessage().contains("Invalid API key")) throw new CompletionException(new NevadaException("Invalid Hypixel API key")); return profile; })
                .whenComplete((profile, err) -> pendingPlayerRequests.remove(id))
        );
    }

    public CompletableFuture<GuildReply> getGuildFromPlayerName(String name) {
        return PlayerUtil.getPlayerUUID(name).thenCompose(this::getGuildFromPlayerUUID);
    }

    public CompletableFuture<GuildReply> getGuildFromPlayerUUID(String uuid) {
        if (uuid == null || uuid.isEmpty()) {
            CompletableFuture<GuildReply> failed = new CompletableFuture<>();
            failed.completeExceptionally(new NevadaException("Player not found"));
            return failed;
        }
        if (Nevada.config.APIKeys_Hypixel != cachedKey) { refresh(); }
        if (Nevada.config.APIKeys_Hypixel.isEmpty()) {
            CompletableFuture<GuildReply> failed = new CompletableFuture<>();
            failed.completeExceptionally(new NevadaException("Invalid Hypixel API Key"));
            return failed;
        }

        return pendingGuildRequests.computeIfAbsent(uuid, id ->
            CompletableFuture.supplyAsync(() -> { rateLimiter.acquire(); return null; }, executor)
                .thenCompose(ignored -> api.getGuildByPlayer(uuid))
                .whenComplete((profile, err) -> pendingGuildRequests.remove(id))
        );
    }

    private static PlayerProfile buildProfile(String uuid, Player player) {
        PlayerProfile profile = new PlayerProfile();

        if (player == null || !player.exists()) {
            profile.hypixel.isNick = true;
            PlayerProfileCache.INSTANCE.cacheProfile(uuid, profile);
            return profile;
        }

        // Format name with rank
        if (!player.hasRank()) profile.hypixel.displayName = "§7" + player.getName();
        else if (player.hasProperty("prefix")) profile.hypixel.displayName = player.getStringProperty("prefix", "§7[UNKNOWN] ") + " " + player.getName();
        else switch (player.getHighestRank()) {
            case "VIP": profile.hypixel.displayName = "§a[VIP] " + player.getName(); break;
            case "VIP_PLUS": profile.hypixel.displayName = "§a[VIP§6+§a] " + player.getName(); break;
            case "MVP": profile.hypixel.displayName = "§b[MVP] " + player.getName(); break;
            case "MVP_PLUS": profile.hypixel.displayName = "§b[MVP" + colorCodeFromName(player.getSelectedPlusColor()) + "+§b] " + player.getName(); break;
            case "SUPERSTAR": profile.hypixel.displayName = colorCodeFromName(player.getSuperstarTagColor()) + "[MVP" + colorCodeFromName(player.getSelectedPlusColor()) + "++" + colorCodeFromName(player.getSuperstarTagColor()) + "] " + player.getName(); break;
            case "STAFF": profile.hypixel.displayName = "§c[§6ዞ§c] " + player.getName(); break;
            case "YOUTUBER": profile.hypixel.displayName = "§c[§fYOUTUBE§c] " + player.getName(); break;
            default: profile.hypixel.displayName = "§7" + player.getName(); break;
        }

        // Bedwars stats
        if (player.getObjectProperty("stats").getAsJsonObject().has("Bedwars")) {
            JsonObject stats = player.getObjectProperty("stats").get("Bedwars").getAsJsonObject();
            if (stats.has("final_deaths_bedwars")) profile.bedwars.finalDeaths = stats.get("final_deaths_bedwars").getAsInt();
            if (stats.has("final_kills_bedwars")) profile.bedwars.finalKills = stats.get("final_kills_bedwars").getAsInt();
            if (stats.has("wins_bedwars")) profile.bedwars.wins = stats.get("wins_bedwars").getAsInt();
            if (stats.has("losses_bedwars")) profile.bedwars.losses = stats.get("losses_bedwars").getAsInt();
            if (stats.has("beds_broken_bedwars")) profile.bedwars.bedsBroken = stats.get("beds_broken_bedwars").getAsInt();
            if (stats.has("beds_lost_bedwars")) profile.bedwars.bedsLost = stats.get("beds_lost_bedwars").getAsInt();
            if (player.getObjectProperty("achievements").has("bedwars_level")) profile.bedwars.level = player.getObjectProperty("achievements").get("bedwars_level").getAsInt();
        }

        // Duels stats
        if (player.getObjectProperty("stats").getAsJsonObject().has("Duels")) {
            JsonObject stats = player.getObjectProperty("stats").get("Duels").getAsJsonObject();
            if (stats.has("wins")) profile.duels.overall.wins = stats.get("wins").getAsInt();
            if (stats.has("losses")) profile.duels.overall.losses = stats.get("losses").getAsInt();
            if (stats.has("current_winstreak")) profile.duels.overall.winstreak = stats.get("current_winstreak").getAsInt();
            if (stats.has("best_winstreak")) profile.duels.overall.best_winstreak = stats.get("best_winstreak").getAsInt();
        }
        if (player.getObjectProperty("stats").getAsJsonObject().has("Duels")) {
            JsonObject stats = player.getObjectProperty("stats").get("Duels").getAsJsonObject();
            if (stats.has("bridge_duel_wins")) profile.duels.bridge.wins += stats.get("bridge_duel_wins").getAsInt();
            if (stats.has("bridge_doubles_wins")) profile.duels.bridge.wins += stats.get("bridge_doubles_wins").getAsInt();
            if (stats.has("bridge_threes_wins")) profile.duels.bridge.wins += stats.get("bridge_threes_wins").getAsInt();
            if (stats.has("bridge_four_wins")) profile.duels.bridge.wins += stats.get("bridge_four_wins").getAsInt();
            if (stats.has("bridge_duel_losses")) profile.duels.bridge.losses += stats.get("bridge_duel_losses").getAsInt();
            if (stats.has("bridge_doubles_losses")) profile.duels.bridge.losses += stats.get("bridge_doubles_losses").getAsInt();
            if (stats.has("bridge_threes_losses")) profile.duels.bridge.losses += stats.get("bridge_threes_losses").getAsInt();
            if (stats.has("bridge_four_losses")) profile.duels.bridge.losses += stats.get("bridge_four_losses").getAsInt();
            if (stats.has("best_bridge_winstreak")) profile.duels.bridge.best_winstreak = stats.get("best_bridge_winstreak").getAsInt();
        }

        // Build Battle stats
        if (player.getObjectProperty("stats").getAsJsonObject().has("BuildBattle")) {
            JsonObject stats = player.getObjectProperty("stats").get("BuildBattle").getAsJsonObject();
            if (stats.has("score")) profile.buildbattle.score = stats.get("score").getAsInt();
            if (stats.has("wins")) profile.buildbattle.wins = stats.get("wins").getAsInt();
            if (stats.has("wins_solo_normal")) profile.buildbattle.soloWins = stats.get("wins_solo_normal").getAsInt();
            if (stats.has("wins_doubles_normal")) profile.buildbattle.doublesWins = stats.get("wins_doubles_normal").getAsInt();
            if (stats.has("wins_guess_the_build")) profile.buildbattle.gtbWins = stats.get("wins_guess_the_build").getAsInt();
        }

        PlayerProfileCache.INSTANCE.cacheProfile(uuid, profile);
        return profile;
    }

    private static String colorCodeFromName(String color) {
        switch (color) {
            case "BLACK": return "§0";
            case "DARK_BLUE": return "§1";
            case "DARK_GREEN": return "§2";
            case "DARK_AQUA": return "§3";
            case "DARK_RED": return "§4";
            case "DARK_PURPLE": return "§5";
            case "GOLD": return "§6";
            case "GRAY": return "§7";
            case "DARK_GRAY": return "§8";
            case "BLUE": return "§9";
            case "GREEN": return "§a";
            case "AQUA": return "§b";
            case "RED": return "§c";
            case "LIGHT_PURPLE": return "§d";
            case "YELLOW": return "§e";
            case "WHITE": return "§f";
        }
        return null;
    }
}