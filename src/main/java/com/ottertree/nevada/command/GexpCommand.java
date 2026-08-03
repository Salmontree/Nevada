package com.ottertree.nevada.command;

import java.util.concurrent.CompletionException;

import com.ottertree.nevada.api.Hypixel;
import com.ottertree.nevada.api.Urchin;
import com.ottertree.nevada.util.ChatUtil;
import com.ottertree.nevada.util.PlayerUtil;

import cc.polyfrost.oneconfig.utils.commands.annotations.Command;
import cc.polyfrost.oneconfig.utils.commands.annotations.Main;

@Command(value="gexp", description="/gexp [username] [daily|weekly|monthly]", customHelpMessage={ChatUtil.PREFIX + "§7/§fgexp§7: Get player's earned guild exp.", ChatUtil.INDENT + "§7Usage: /§fgexp §7[username] [daily|weekly|monthly]"})
public class GexpCommand {
    private static final java.text.NumberFormat COMMA_FORMAT = java.text.NumberFormat.getNumberInstance(java.util.Locale.US);

    @Main
    private void handle() {
        handle(PlayerUtil.getPlayerName(), "weekly");
    }
    
    @Main
    private void handle(String user) {
        handle(user, "weekly");
    }

    @Main
    private void handle(String user, String timePeriod) {
        final String period = timePeriod.toLowerCase();
        switch (period) { case "daily": break; case "weekly": break; case "monthly": break; default: ChatUtil.send(ChatUtil.PREFIX + "§cInvalid usage: expected 'daily', 'weekly', or 'monthly', got: " + timePeriod); return; }
        
        PlayerUtil.playerExists(user).thenAccept(exists -> {
            if (!exists) {
                ChatUtil.send(ChatUtil.PREFIX + "§7Player not found");
                return;
            }

            ChatUtil.send(ChatUtil.PREFIX + "§7Fetching §f" + user + "§7's " + period + " gexp...");

            Urchin.INSTANCE.getPlayerGexp(user, period).thenAccept(gexp -> {
                Hypixel.INSTANCE.getPlayerProfileFromName(user).thenAccept(profile -> {
                    ChatUtil.send(ChatUtil.INDENT + "§f" + profile.hypixel.displayName + " §7collected §f" + COMMA_FORMAT.format(gexp) + " §7" + period + " gexp");
                });
            }).exceptionally(e -> {
                if ((e instanceof CompletionException ? e.getCause() : e) instanceof Error) {
                    ChatUtil.send(ChatUtil.PREFIX + "§7" + (e instanceof CompletionException ? e.getCause() : e).getMessage());
                    return null;
                }

                ChatUtil.send(ChatUtil.PREFIX + "§7Couldn't lookup player stats");
                e.printStackTrace();
                return null;
            });
        });
    }
}
