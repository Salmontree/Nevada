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
                switch (tag.type) {
                    case "confirmed_cheater": result.append("§5[§lCCC§r§5] "); break;
                    case "closet_cheater":    result.append("§6[CC§r§6] "); break;
                    case "blatant_cheater":   result.append("§6[§lBC§r§6] "); break;
                    case "caution":           result.append("§6[C§r§6] "); break;
                    case "sniper":            result.append("§c[§lS§r§c] "); break;
                    case "possible_sniper":   result.append("§c[PS§r§c] "); break;
                    case "legit_sniper":      result.append("§c[LS§r§c] "); break;
                    default:                  result.append("§c[" + tag.typeAbbrv + "§r§c] "); break;
                }
            });
			return result.toString().trim();
        });
    }
}