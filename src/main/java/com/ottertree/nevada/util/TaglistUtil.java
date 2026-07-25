package com.ottertree.nevada.util;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.ottertree.nevada.Nevada;
import com.ottertree.nevada.api.Urchin;
import com.ottertree.nevada.data.Tag;

public class TaglistUtil {
    public static CompletableFuture<List<Tag>> getFullTablist(String player) {
        if (!Nevada.config.Blacklists_EnableUrchin) {
            return CompletableFuture.completedFuture(new ArrayList<>());
        }

        return Urchin.INSTANCE.getPlayerTaglist(player).thenApply(ArrayList::new);
    }

    public static CompletableFuture<String> getFullTablistCompacted(String player) {
        return getFullTablist(player).thenApply(tags -> {
            StringBuilder result = new StringBuilder();
            tags.forEach(tag -> result.append("§4[§c").append(tag.typeAbbrv).append("§4] "));
            return result.toString().trim();
        });
    }
}
