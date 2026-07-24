package com.ottertree.nevada.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.ottertree.nevada.data.PlayerProfile;

public class PlayerProfileCache {
    public static final PlayerProfileCache INSTANCE = new PlayerProfileCache();

    private Cache<String, PlayerProfile> cache;

    public PlayerProfileCache() {
        cache = CacheBuilder.newBuilder().maximumSize(1000).build();
    }

    public PlayerProfile getProfile(String uuid) {
        if (uuid == null || uuid.isEmpty()) return null;
        return cache.getIfPresent(uuid);
    }

    public void cacheProfile(String uuid, PlayerProfile profile) {
        if (uuid == null || uuid.isEmpty() || profile == null) return;
        cache.put(uuid, profile);
    }

    public void clear() {
        cache.invalidateAll();
    }
}
