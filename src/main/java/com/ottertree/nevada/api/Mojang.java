package com.ottertree.nevada.api;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.*;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.util.concurrent.RateLimiter;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.google.gson.JsonParser;

public class Mojang {
    private static final ExecutorService executor = Executors.newFixedThreadPool(4,
            new ThreadFactoryBuilder().setNameFormat("mojang-api-%d").setDaemon(true).build());
    private static final RateLimiter rateLimiter = RateLimiter.create(30.0);
    private static final Cache<String, String> cache = CacheBuilder.newBuilder().expireAfterWrite(1, TimeUnit.HOURS).maximumSize(2000).build();
    private static final ConcurrentHashMap<String, CompletableFuture<String>> inFlight = new ConcurrentHashMap<>();
 
    public static CompletableFuture<String> getPlayerUUID(String name) {
        if (name == null || name.isEmpty()) {
            CompletableFuture<String> failed = new CompletableFuture<>();
            failed.completeExceptionally(new Error("Player not found"));
            return failed;
        }
 
        String key = name.toLowerCase();

        if (key == null) {
            CompletableFuture<String> failed = new CompletableFuture<>();
            failed.completeExceptionally(new Exception("key == null returned true somehow"));
            return failed;
        }
 
        String cached = cache.getIfPresent(key);
        if (cached != null) {
            return CompletableFuture.completedFuture(cached);
        }
 
        return inFlight.computeIfAbsent(key, k -> {
            CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
                rateLimiter.acquire();
                try {
                    String uuid = fetchUUIDBlocking(name);
                    if (uuid == null) return "";
                    cache.put(key, uuid);
                    return uuid;
                } catch (Exception e) {
                    throw new CompletionException(e);
                }
            }, executor);
 
            future.whenComplete((result, error) -> inFlight.remove(key));
            return future;
        });
    }
 
    private static String fetchUUIDBlocking(String name) throws Exception {
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) new URL("https://api.mojang.com/minecraft/profile/lookup/name/" + name).openConnection();
            connection.setRequestMethod("GET");
 
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new Exception(connection.getResponseMessage());
            }
 
            return JsonParser.parseReader(new InputStreamReader(connection.getInputStream())).getAsJsonObject().get("id").getAsString();
        } finally {
            if (connection != null) connection.disconnect();
        }
    }

    public static CompletableFuture<Boolean> playerExists(String name) {
        if (name == null || name.isEmpty()) {
            CompletableFuture<Boolean> failed = new CompletableFuture<>();
            failed.completeExceptionally(new Error("Player not found"));
            return failed;
        }
 
        String key = name.toLowerCase();

        if (key == null) {
            CompletableFuture<Boolean> failed = new CompletableFuture<>();
            failed.completeExceptionally(new Exception("key == null returned true somehow"));
            return failed;
        }
 
        String cached = cache.getIfPresent(key);
        if (cached != null) {
            return CompletableFuture.completedFuture(true);
        }
 
        return CompletableFuture.supplyAsync(() -> {
            rateLimiter.acquire();
            try {
                String uuid = fetchUUIDBlocking(name);
                if (uuid == null) return false;
                cache.put(key, uuid);
                return true;
            } catch (Exception e) {
                return false;
            }
        }, executor);
    }
}