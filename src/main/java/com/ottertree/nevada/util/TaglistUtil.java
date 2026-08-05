package com.ottertree.nevada.util;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.ottertree.nevada.api.Urchin;
import com.ottertree.nevada.data.Tag;

public class TaglistUtil {
    public static CompletableFuture<List<Tag>> getFullTablist(String player) {
        return Urchin.INSTANCE.getPlayerTaglist(player).thenApply(ArrayList::new);
    }

    public static CompletableFuture<String> getFullTablistCompacted(String player) {
        return getFullTablist(player).thenApply(tags -> {
            StringBuilder result = new StringBuilder();
            tags.forEach(tag -> {
                String label;
                String color;
                switch (tag.type) {
                    case "confirmed_cheater": label = "CCC"; color = "§d§l"; break; // pink, bold
                    case "closet_cheater":    label = "CC";  color = "§6";   break; // orange
                    case "blatant_cheater":   label = "BC";  color = "§6§l"; break; // orange, bold
                    case "caution":           label = "C";   color = "§6";   break; // orange
                    case "sniper":            label = "S";   color = "§c§l"; break; // red, bold
                    case "possible_sniper":   label = "PS";  color = "§c";   break; // red
                    case "legit_sniper":      label = "LS";  color = "§c";   break; // red
                    default:                  label = tag.typeAbbrv; color = "§c"; break;
                }
                result.append("§4[").append(color).append(label).append("§4] ");
            });
            return result.toString().trim();
        });
    }
}
