package com.ottertree.nevada.cache;

import javax.annotation.Nonnull;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.ottertree.nevada.stats.PlayerProfile;

public class PlayerProfileCache {
    private static final Cache<String, PlayerProfile> cache = CacheBuilder.newBuilder().maximumSize(1024).build();

    public static void cache(@Nonnull String username, @Nonnull PlayerProfile stats) {
        cache.put(username.toLowerCase(), stats);
    }

    public static PlayerProfile getCached(@Nonnull String username) {
        return cache.getIfPresent(username.toLowerCase());
    }

    public static void clear() {
        cache.invalidateAll();
    }
}
