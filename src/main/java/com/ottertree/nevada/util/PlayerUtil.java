package com.ottertree.nevada.util;

import java.util.concurrent.CompletableFuture;

import com.ottertree.nevada.api.Mojang;

public class PlayerUtil {
    public static CompletableFuture<String> getPlayerUUID(String name) {
        return Mojang.getPlayerUUID(name);
    }

    public static CompletableFuture<Boolean> playerExists(String name) {
        return Mojang.playerExists(name);
    }
}
