package com.ottertree.nevada.api;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.google.common.util.concurrent.RateLimiter;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ottertree.nevada.Nevada;

public class Aurora {
    public static final Aurora INSTANCE = new Aurora();

    private static final ExecutorService executor = Executors.newFixedThreadPool(4, new ThreadFactoryBuilder().setNameFormat("aurora-api-%d").setDaemon(true).build());
    private static final RateLimiter rateLimiter = RateLimiter.create(7.0);

    // Dedupes in-flight requests so repeated calls for the same type+value while a fetch
    // is still pending share one future instead of each firing a new HTTP request.
    private static final ConcurrentHashMap<String, CompletableFuture<List<AuroraResponse>>> pendingRequests = new ConcurrentHashMap<>();

    public Aurora() {

    }

    public class AuroraResponse {
        public int distance;
        public String name;
    }

    public CompletableFuture<List<AuroraResponse>> denick(String type, int value) {
        String key = type + ":" + value;

        return pendingRequests.computeIfAbsent(key, k ->
            CompletableFuture.supplyAsync(() -> {
                rateLimiter.acquire();
                return denickBlocking(type, value);
            }, executor)
            .whenComplete((responses, err) -> pendingRequests.remove(k))
        );
    }

    private List<AuroraResponse> denickBlocking(String type, int value) {
        ArrayList<AuroraResponse> responses = new ArrayList<>();
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) new URL("https://bordic.xyz/api/v2/resources/lookup/" + type + "?range=100&max=5&key=" + Nevada.config.APIKeys_Aurora + "&value=" + value).openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new Exception(connection.getResponseMessage());
            }

            JsonObject fullApiResponse = JsonParser.parseReader(new InputStreamReader(connection.getInputStream())).getAsJsonObject();

            if (fullApiResponse.get("success").getAsBoolean()) {
                fullApiResponse.get("data").getAsJsonArray().forEach(apiResponse -> {
                    AuroraResponse response = new AuroraResponse();
                    response.name = apiResponse.getAsJsonObject().get("name").getAsString();
                    response.distance = apiResponse.getAsJsonObject().get("distance").getAsInt();
                    responses.add(response);
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) connection.disconnect();
        }

        return responses;
    }

    public CompletableFuture<List<AuroraResponse>> denickFinals(int finals) {
        return denick("finals", finals);
    }

    public CompletableFuture<List<AuroraResponse>> denickBeds(int beds) {
        return denick("beds", beds);
    }
}