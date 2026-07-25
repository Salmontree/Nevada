package com.ottertree.nevada.api;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ConcurrentHashMap;
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
    private static final RateLimiter rateLimiter = RateLimiter.create(7.0);
    private static final ConcurrentHashMap<String, CompletableFuture<List<Tag>>> pendingRequests = new ConcurrentHashMap<>();

    public Urchin() {}

    public CompletableFuture<List<Tag>> getPlayerTaglist(String name) {
        if (name == null || name.isEmpty()) {
            CompletableFuture<List<Tag>> failed = new CompletableFuture<>();
            failed.completeExceptionally(new Exception("Player not found"));
            return failed;
        }

        List<Tag> cachedTaglist = PlayerTaglistCache.INSTANCE.getTaglist(name);
        if (cachedTaglist != null) {
            return CompletableFuture.completedFuture(cachedTaglist);
        }

        return pendingRequests.computeIfAbsent(name, key ->
            CompletableFuture.supplyAsync(() -> {
                rateLimiter.acquire();
                try {
                    return fetchTaglistBlocking(key);
                } catch (Exception e) {
                    throw new CompletionException(e);
                }
            }, executor)
            .thenApply(taglist -> {
                PlayerTaglistCache.INSTANCE.cacheTaglist(key, taglist);
                return taglist;
            })
            .whenComplete((taglist, err) -> pendingRequests.remove(key))
        );
    }

    private static List<Tag> fetchTaglistBlocking(String name) throws Exception {
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) new URL("https://api.urchin.gg/v3/player/tags?player=" + name).openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("X-API-Key", Nevada.config.APIKeys_Urchin);
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");

            int code = connection.getResponseCode();
            if (code == 404)
                return new ArrayList<>();
            else if (code != HttpURLConnection.HTTP_OK)
                throw new Exception(connection.getResponseMessage());

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