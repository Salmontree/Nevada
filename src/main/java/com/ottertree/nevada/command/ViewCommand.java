package com.ottertree.nevada.command;

import com.ottertree.nevada.util.ChatUtil;
import com.ottertree.nevada.util.PlayerUtil;
import com.ottertree.nevada.util.TaglistUtil;

import cc.polyfrost.oneconfig.utils.commands.annotations.Command;
import cc.polyfrost.oneconfig.utils.commands.annotations.Main;

@Command(value="view", description="/view <username>", aliases={"v"})
public class ViewCommand {
    @Main
    private void handle(String username) {
        ChatUtil.send(ChatUtil.PREFIX + "§8Fetching §7" + username + "§8's tags...");

        PlayerUtil.playerExists(username).thenAccept(exists -> {
            if (!exists) {
                ChatUtil.send(ChatUtil.INDENT + "§8Player not found");
                return;
            }
            
            TaglistUtil.getFullTablist(username).thenAccept(tags -> {
                if (tags.isEmpty()) {
                    ChatUtil.send(ChatUtil.INDENT + "§8No tags found!");
                    return;
                }
                tags.forEach(tag -> {
                    ChatUtil.send(ChatUtil.INDENT + "§8Found Urchin tag: §4" + tag.typeTitle + " §7(" + tag.reason + ")");
                });
            }).exceptionally(e -> {
                Throwable cause = e instanceof java.util.concurrent.CompletionException ? e.getCause() : e;
                ChatUtil.send(ChatUtil.PREFIX + "§cError fetching tags: " + cause);
                return null;
            });
        });
    }
}
