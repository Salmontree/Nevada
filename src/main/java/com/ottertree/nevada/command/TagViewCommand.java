package com.ottertree.nevada.command;

import java.io.IOException;
import java.util.ArrayList;

import com.ottertree.nevada.Async;
import com.ottertree.nevada.NevadaCommon;
import com.ottertree.nevada.NevadaConfig;
import com.ottertree.nevada.blacklist.Coral;
import com.ottertree.nevada.blacklist.Tag;
import com.ottertree.nevada.stats.PlayerProfile;
import com.ottertree.nevada.stats.backends.Hypixel;

import cc.polyfrost.oneconfig.utils.commands.annotations.Command;
import cc.polyfrost.oneconfig.utils.commands.annotations.Main;

@Command(value = "tagview", description = "/tagview <username>", aliases = {"view", "tv"})
public class TagViewCommand {
    @Main
    private void handle(String name) {
        Async.run(() -> {
            Async.runOnMainThread(() -> NevadaCommon.addChatTextP("§8Fetching §7" + name + "'s§8 tags..."));
            ArrayList<Tag> tags = new ArrayList<Tag>();
            
            if (NevadaConfig.Tags_UseCoral) {
                try { tags.addAll(Coral.getPlayerTags(name)); }
                catch (IOException e) { Async.runOnMainThread(() -> NevadaCommon.addChatTextP("§cError: Couldn't fetch Coral tags - " + e.getMessage())); }
            }

            PlayerProfile profile = new PlayerProfile();
            try { profile = Hypixel.getPlayerProfile(name); }
            catch (IOException e) { profile.hypixelDisplayName = name; }

            final String displayName = profile.hypixelDisplayName;
            
            if (tags.isEmpty()) {
                Async.runOnMainThread(() -> NevadaCommon.addChatText("  §5▌ §8Found no tags for " + displayName));
            }

            tags.forEach((tag) -> {
                Async.runOnMainThread(() -> NevadaCommon.addChatText("  §5▌ §cFound " + tag.source + " tag for " + displayName + "§c: §4" + tag.typeTitle + " §7(" + tag.reason + ")"));
            });
        });
    }
}
