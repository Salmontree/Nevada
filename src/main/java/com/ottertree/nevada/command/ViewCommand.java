package com.ottertree.nevada.command;

import java.util.concurrent.CompletionException;

import com.ottertree.nevada.data.NevadaException;
import com.ottertree.nevada.util.ChatUtil;
import com.ottertree.nevada.util.PlayerUtil;
import com.ottertree.nevada.util.TaglistUtil;

import cc.polyfrost.oneconfig.utils.commands.annotations.Command;
import cc.polyfrost.oneconfig.utils.commands.annotations.Main;

@Command(value="view", description="/view [username]", aliases={"v"}, customHelpMessage={ChatUtil.PREFIX + "§7/§fview§7: Check blacklist tags for any player.", ChatUtil.INDENT + "§7Usage: /§fview§7 [username]", ChatUtil.INDENT + "§7Aliases: /§fv"})
public class ViewCommand {
    @Main
    private void handle() {
        handle(PlayerUtil.getPlayerName());
    }
    
    @Main
    private void handle(String username) {
        ChatUtil.send(ChatUtil.PREFIX + "§7Fetching §f" + username + "§7's tags...");

        PlayerUtil.playerExists(username).thenAccept(exists -> {
            if (!exists) {
                ChatUtil.send(ChatUtil.PREFIX + "§7Player not found");
                return;
            }
            
            TaglistUtil.getFullTablist(username).thenAccept(tags -> {
                if (tags.isEmpty()) {
                    ChatUtil.send(ChatUtil.INDENT + "§7No tags found!");
                    return;
                }
                tags.forEach(tag -> {
                    ChatUtil.send(ChatUtil.INDENT + "§7Found Urchin tag for §f" + username + "§7: §4" + tag.typeTitle + " §7(" + tag.reason + ")");
                });
            }).exceptionally(e -> {
                if ((e instanceof CompletionException ? e.getCause() : e) instanceof NevadaException) {
                    ChatUtil.send(ChatUtil.PREFIX + "§7" + (e instanceof CompletionException ? e.getCause() : e).getMessage());
                    return null;
                }

                ChatUtil.send(ChatUtil.PREFIX + "§7Couldn't fetch tags");
                e.printStackTrace();
                return null;
            });
        });
    }
}
