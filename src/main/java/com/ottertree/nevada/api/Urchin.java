package com.ottertree.nevada.api;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import com.google.common.util.concurrent.RateLimiter;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ottertree.nevada.Nevada;
import com.ottertree.nevada.cache.PlayerTaglistCache;
import com.ottertree.nevada.data.Tag;

public class Urchin {
    public static final Urchin INSTANCE = new Urchin();

    private static final ExecutorService executor = Executors.newFixedThreadPool(4, new ThreadFactoryBuilder().setNameFormat("urchin-api-%d").setDaemon(true).build());
    private static final RateLimiter rateLimiter = RateLimiter.create(1.0);

    public Urchin() {}

    public CompletableFuture<List<Tag>> getPlayerTaglist(String name) {
        if (name == null || name.isEmpty()) {
            CompletableFuture<List<Tag>> failed = new CompletableFuture<>();
            failed.completeExceptionally(new IllegalArgumentException("Player not found"));
            return failed;
        }

        List<Tag> cachedTaglist = PlayerTaglistCache.INSTANCE.getTaglist(name);
        if (cachedTaglist != null) {
            return CompletableFuture.completedFuture(cachedTaglist);
        }

        return CompletableFuture.supplyAsync(() -> {
            rateLimiter.acquire();
            try {
                return fetchTaglistBlocking(name);
            } catch (Exception e) {
                throw new CompletionException(e);
            }
        }, executor)
        .thenApply(taglist -> {
            PlayerTaglistCache.INSTANCE.cacheTaglist(name, taglist);
            return taglist;
        });
    }

    private static List<Tag> fetchTaglistBlocking(String name) throws Exception {
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) new URL("https://api.urchin.gg/v3/player/tags?player=" + name).openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("X-API-Key", Nevada.config.APIKeys_Urchin);
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new Exception(connection.getResponseMessage());
            }

            ArrayList<Tag> tagList = new ArrayList<Tag>();
            JsonArray tags = JsonParser.parseReader(new InputStreamReader(connection.getInputStream())).getAsJsonObject().get("tags").getAsJsonArray();
            tags.forEach((tag) -> {
                JsonObject tago = tag.getAsJsonObject();
                Tag result = new Tag();
                result.reason = tago.get("reason").getAsString();
                result.type = tago.get("tag_type").getAsString();
                result.typeTitle = Arrays.stream(result.type.split("_")).map(word -> (word.substring(0, 1).toUpperCase() + word.substring(1) + " ")).collect(Collectors.joining()).trim();
                result.typeAbbrv = Arrays.stream(result.typeTitle.split(" ")).map(word -> String.valueOf(word.charAt(0))).collect(Collectors.joining());
                tagList.add(result);
            });
            return tagList;
        } finally {
            if (connection != null) connection.disconnect();
        }
    }
}