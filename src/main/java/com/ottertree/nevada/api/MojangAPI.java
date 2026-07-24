package com.ottertree.nevada.api;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class MojangAPI {
    public static JsonObject getPlayerInfo(String username) throws IOException {
        URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + username);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        if (conn.getResponseCode() != 200) {
            throw new IOException("Player not found");
        }

        try (Reader reader = new InputStreamReader(conn.getInputStream())) {
            return new JsonParser().parse(reader).getAsJsonObject();
        }
    }
    
    public static String getPlayerUUID(String username) throws IOException {
        return getPlayerInfo(username).get("id").getAsString();
    }
    public static String getPlayerFullName(String username) throws IOException {
        return getPlayerInfo(username).get("name").getAsString();
    }
}
