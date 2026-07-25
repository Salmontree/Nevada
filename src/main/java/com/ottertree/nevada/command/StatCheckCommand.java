package com.ottertree.nevada.command;

import com.ottertree.nevada.api.Hypixel;
import com.ottertree.nevada.util.BedwarsUtil;
import com.ottertree.nevada.util.BuildBattleUtil;
import com.ottertree.nevada.util.ChatUtil;
import com.ottertree.nevada.util.PlayerUtil;
import com.ottertree.nevada.util.TaglistUtil;

import cc.polyfrost.oneconfig.utils.commands.annotations.Command;
import cc.polyfrost.oneconfig.utils.commands.annotations.Main;

@Command(value="statcheck", description="/statcheck <username> <mode> [recency]", aliases={"sc"})
public class StatCheckCommand {
    @Main
    private void handle(String username, String game) {
        ChatUtil.send(ChatUtil.PREFIX + "§8Fetching §7" + username + "§8's stats...");

        switch (game) {
            case "bw": break; case "bb": break; default: { ChatUtil.send(ChatUtil.PREFIX + "§cError: Invalid game (options include bw, bb)"); return; }
        }

        PlayerUtil.playerExists(username).thenAccept(exists -> {
            if (!exists) {
                ChatUtil.send(ChatUtil.INDENT + "§8Player not found");
                return;
            }

            Hypixel.INSTANCE.getPlayerProfileFromName(username).thenAccept(profile -> {
                TaglistUtil.getFullTablistCompacted(username).thenAccept(taglist -> {
                    if (!taglist.isEmpty()) taglist += " ";
                    switch (game) {
                        case "bw": ChatUtil.send(ChatUtil.INDENT + taglist + BedwarsUtil.formatLevel(profile.bedwars.level) + " " + profile.hypixel.displayName + "§8 - Finals: " + BedwarsUtil.colorFinals(profile.bedwars.finalKills) + " §8FKDR: " + BedwarsUtil.colorFKDR(profile.bedwars.getFKDR()) + " §8Wins: " + BedwarsUtil.colorFinals(profile.bedwars.wins) + " §8WLR: " + BedwarsUtil.colorWLR(profile.bedwars.getWLR())); break;
                        case "bb": ChatUtil.send(ChatUtil.INDENT + taglist + BuildBattleUtil.formatScore(profile.buildbattle.score) + " " + profile.hypixel.displayName + " §8Wins: §7" + profile.buildbattle.wins + " §8Solo Wins: §7" + profile.buildbattle.soloWins + " §8Doubles Wins: §7" + profile.buildbattle.doublesWins + " §8GTB Wins: §7" + profile.buildbattle.gtbWins); break;
                    }
                });
            }).exceptionally(e -> {
                ChatUtil.send(ChatUtil.PREFIX + "§cError: " + e.getMessage());
                return null;
            });
        }).exceptionally(e -> {
            Throwable cause = e instanceof java.util.concurrent.CompletionException ? e.getCause() : e;
            ChatUtil.send(ChatUtil.PREFIX + "§cError: " + cause);
            return null;
        });
    }
}
