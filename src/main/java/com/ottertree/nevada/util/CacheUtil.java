package com.ottertree.nevada.util;

import com.ottertree.nevada.cache.PlayerNickCache;
import com.ottertree.nevada.cache.PlayerProfileCache;
import com.ottertree.nevada.cache.PlayerTaglistCache;

public class CacheUtil {
    public static void clearAllCaches() {
        PlayerProfileCache.INSTANCE.clear();
        PlayerNickCache.INSTANCE.clear();
        PlayerTaglistCache.INSTANCE.clear();
    }
}
