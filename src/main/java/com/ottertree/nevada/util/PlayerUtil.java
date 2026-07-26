package com.ottertree.nevada.util;

import java.util.concurrent.CompletableFuture;

import com.ottertree.nevada.api.Mojang;

import net.minecraft.client.Minecraft;

public class PlayerUtil {
    public static String getPlayerName() {
        return Minecraft.getMinecraft().thePlayer.getName();
    }

    public static CompletableFuture<String> getPlayerUUID(String name) {
        return Mojang.getPlayerUUID(name);
    }

    public static CompletableFuture<Boolean> playerExists(String name) {
        return Mojang.playerExists(name);
    }
}
