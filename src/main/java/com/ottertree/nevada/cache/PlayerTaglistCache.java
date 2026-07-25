package com.ottertree.nevada.cache;

import java.util.List;
import java.util.concurrent.TimeUnit;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.ottertree.nevada.data.Tag;

public class PlayerTaglistCache {
    public static final PlayerTaglistCache INSTANCE = new PlayerTaglistCache();

    private Cache<String, List<Tag>> cache;

    public PlayerTaglistCache() {
        cache = CacheBuilder.newBuilder().expireAfterWrite(30, TimeUnit.MINUTES).maximumSize(1000).build();
    }

    public List<Tag> getTaglist(String uuid) {
        if (uuid == null || uuid.isEmpty()) return null;
        return cache.getIfPresent(uuid);
    }

    public void cacheTaglist(String uuid, List<Tag> taglist) {
        if (uuid == null || uuid.isEmpty() || taglist == null) return;
        cache.put(uuid, taglist);
    }

    public void clear() {
        cache.invalidateAll();
    }
}
