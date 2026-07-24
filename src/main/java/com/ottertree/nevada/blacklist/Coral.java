package com.ottertree.nevada.blacklist;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ottertree.nevada.NevadaConfig;

public class Coral {
    public static List<Tag> getPlayerTags(String identifier) throws IOException {
        URL url = new URL("https://api.urchin.gg/v3/player/tags?player=" + identifier);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestProperty("User-Agent", "Mozilla/5.0"); // anti-anti-bot
        conn.setRequestMethod("GET");
        conn.setRequestProperty("X-API-Key", NevadaConfig.APIKey_Coral);

        if (conn.getResponseCode() == 401) throw new IOException("Invalid Coral API Key");
        if (conn.getResponseCode() == 404) throw new IOException("Player not found");
        if (conn.getResponseCode() == 429) throw new IOException("Coral API Rate Limit Reached (try again later)");
        if (conn.getResponseCode() != 200) throw new IOException("Coral API Failure (" + conn.getResponseCode() + ")");

        try (Reader reader = new InputStreamReader(conn.getInputStream())) {
            ArrayList<Tag> tagList = new ArrayList<Tag>();
            JsonArray tags = new JsonParser().parse(reader).getAsJsonObject().get("tags").getAsJsonArray();
            tags.forEach((tag) -> {
                JsonObject tago = tag.getAsJsonObject();
                Tag result = new Tag();
                result.source = "Urchin";
                result.reason = tago.get("reason").getAsString();
                result.type = tago.get("tag_type").getAsString();
                result.typeTitle = Arrays.stream(result.type.split("_")).map(word -> (word.substring(0, 1).toUpperCase() + word.substring(1) + " ")).collect(Collectors.joining()).trim();
                result.typeAbbrv = Arrays.stream(result.typeTitle.split(" ")).map(word -> String.valueOf(word.charAt(0))).collect(Collectors.joining());
                tagList.add(result);
            });
            return tagList;
        }
    }
}
