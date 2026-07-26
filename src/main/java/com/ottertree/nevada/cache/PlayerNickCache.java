package com.ottertree.nevada.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

public class PlayerNickCache {
    public static final PlayerNickCache INSTANCE = new PlayerNickCache();

    private Cache<String, String> cache;

    public PlayerNickCache() {
        cache = CacheBuilder.newBuilder().maximumSize(1000).build();
    }

    public String getNick(String nick) {
        if (nick == null || nick.isEmpty()) return null;
        return cache.getIfPresent(nick);
    }

    public void cacheNick(String nick, String realName) {
        if (nick == null || nick.isEmpty() || realName == null) return;
        cache.put(nick, realName);
    }

    public void clear() {
        cache.invalidateAll();
    }
}
