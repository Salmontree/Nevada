package com.ottertree.nevada.command;

import java.util.concurrent.CompletionException;

import com.ottertree.nevada.api.Hypixel;
import com.ottertree.nevada.data.NevadaException;
import com.ottertree.nevada.util.BedwarsUtil;
import com.ottertree.nevada.util.BuildBattleUtil;
import com.ottertree.nevada.util.ChatUtil;
import com.ottertree.nevada.util.DuelsUtil;
import com.ottertree.nevada.util.PlayerUtil;
import com.ottertree.nevada.util.TaglistUtil;

import cc.polyfrost.oneconfig.utils.commands.annotations.Command;
import cc.polyfrost.oneconfig.utils.commands.annotations.Greedy;
import cc.polyfrost.oneconfig.utils.commands.annotations.Main;

@Command(value="statcheck", description="/statcheck <username> <mode>", aliases={"sc", "stats"}, customHelpMessage={ChatUtil.PREFIX + "§8/§7statcheck§8: Check Hypixel stats for any player.", ChatUtil.INDENT + "§8Usage: /§7statcheck§8 <username> <mode>", ChatUtil.INDENT + "§8Aliases: /§7stats§8, /§7sc", ChatUtil.INDENT + "§8Gamemodes: §7bw§8, §7duels§8, §7bridge§8, §7bb"})
public class StatCheckCommand {
    @Main
    private void handle(String username, @Greedy String gameName) {
        ChatUtil.send(ChatUtil.PREFIX + "§8Fetching §7" + username + "§8's stats...");

        final String game = gameName.toLowerCase();
        switch (game) {
            case "bw": break; case "bb": break; case "duels": break; case "bridge": break; default: { ChatUtil.send(ChatUtil.PREFIX + "§8Invalid game (options include bw, bb, duels, bridge)"); return; }
        }

        PlayerUtil.playerExists(username).thenAccept(exists -> {
            if (!exists) {
                ChatUtil.send(ChatUtil.PREFIX + "§8Player not found");
                return;
            }

            Hypixel.INSTANCE.getPlayerProfileFromName(username).thenAccept(profile -> {
                if (profile.hypixel.isNick) {
                    ChatUtil.send(ChatUtil.PREFIX + "§8Player not found");
                    return;
                }
                
                TaglistUtil.getFullTablistCompacted(username).thenAccept(taglistResult -> {
                    String taglist = taglistResult;
                    if (!taglist.isEmpty()) taglist += " ";
                    switch (game) {
                        case "bw": ChatUtil.send(ChatUtil.INDENT + BedwarsUtil.formatLevel(profile.bedwars.level) + " " + taglist + profile.hypixel.displayName + "§8 - Finals: " + BedwarsUtil.colorFinals(profile.bedwars.finalKills) + " §8FKDR: " + BedwarsUtil.colorFKDR(profile.bedwars.getFKDR()) + " §8Wins: " + BedwarsUtil.colorFinals(profile.bedwars.wins) + " §8WLR: " + BedwarsUtil.colorWLR(profile.bedwars.getWLR()) + " §8Beds: " + BedwarsUtil.colorBedsBroken(profile.bedwars.bedsBroken) + " §8BBLR: " + BedwarsUtil.colorBBLR(profile.bedwars.getBBLR())); break;
                        case "bb": ChatUtil.send(ChatUtil.INDENT + BuildBattleUtil.formatScore(profile.buildbattle.score) + " " + taglist + profile.hypixel.displayName + " §8Wins: §7" + profile.buildbattle.wins + " §8Solo Wins: §7" + profile.buildbattle.soloWins + " §8Doubles Wins: §7" + profile.buildbattle.doublesWins + " §8GTB Wins: §7" + profile.buildbattle.gtbWins); break;
                        case "duels": ChatUtil.send(ChatUtil.INDENT + taglist + profile.hypixel.displayName + "§8 - Wins: §7" + DuelsUtil.colorWins(profile.duels.overall.wins) + "§8 - WLR: §7" + DuelsUtil.colorWLR(profile.duels.overall.getWLR()) + "§8 - BWS: §7" + DuelsUtil.colorBWS(profile.duels.overall.best_winstreak) + "§8 - WS: §7" + DuelsUtil.colorWS(profile.duels.overall.winstreak)); break;
                        case "bridge": ChatUtil.send(ChatUtil.INDENT + taglist + profile.hypixel.displayName + "§8 - Wins: §7" + DuelsUtil.colorWins(profile.duels.bridge.wins) + "§8 - WLR: §7" + DuelsUtil.colorWLR(profile.duels.bridge.getWLR()) + "§8 - BWS: §7" + DuelsUtil.colorBWS(profile.duels.bridge.best_winstreak)); break;
                    }
                }).exceptionally(e -> {
                    switch (game) {
                        case "bw": ChatUtil.send(ChatUtil.INDENT + profile.bedwars.displayLevel + " " + profile.hypixel.displayName + "§8 - Finals: " + profile.bedwars.displayFinals + " §8FKDR: " + profile.bedwars.displayFKDR + " §8Wins: " + profile.bedwars.displayWins + " §8WLR: " + profile.bedwars.displayWLR + " §8Beds: " + profile.bedwars.displayBeds + " §8BBLR: " + profile.bedwars.displayBBLR); break;
                        case "bb": ChatUtil.send(ChatUtil.INDENT + BuildBattleUtil.formatScore(profile.buildbattle.score) + " " + profile.hypixel.displayName + " §8Wins: §7" + profile.buildbattle.wins + " §8Solo Wins: §7" + profile.buildbattle.soloWins + " §8Doubles Wins: §7" + profile.buildbattle.doublesWins + " §8GTB Wins: §7" + profile.buildbattle.gtbWins); break;
                        case "duels": ChatUtil.send(ChatUtil.INDENT + profile.hypixel.displayName + "§8 - Wins: §7" + DuelsUtil.colorWins(profile.duels.overall.wins) + "§8 - WLR: §7" + DuelsUtil.colorWLR(profile.duels.overall.getWLR()) + "§8 - BWS: §7" + DuelsUtil.colorBWS(profile.duels.overall.best_winstreak) + "§8 - WS: §7" + DuelsUtil.colorWS(profile.duels.overall.winstreak)); break;
                        case "bridge": ChatUtil.send(ChatUtil.INDENT + profile.hypixel.displayName + "§8 - Wins: §7" + DuelsUtil.colorWins(profile.duels.bridge.wins) + "§8 - WLR: §7" + DuelsUtil.colorWLR(profile.duels.bridge.getWLR()) + "§8 - BWS: §7" + DuelsUtil.colorBWS(profile.duels.bridge.best_winstreak)); break;
                    }
                    return null;
                });
            }).exceptionally(e -> {
                if ((e instanceof CompletionException ? e.getCause() : e) instanceof NevadaException) {
                    ChatUtil.send(ChatUtil.PREFIX + "§8" + (e instanceof CompletionException ? e.getCause() : e).getMessage());
                    return null;
                }

                ChatUtil.send(ChatUtil.PREFIX + "§8Couldn't fetch player stats");
                e.printStackTrace();
                return null;
            });
        }).exceptionally(e -> {
            if ((e instanceof CompletionException ? e.getCause() : e) instanceof NevadaException) {
                ChatUtil.send(ChatUtil.PREFIX + "§8" + (e instanceof CompletionException ? e.getCause() : e).getMessage());
                return null;
            }
            
            ChatUtil.send(ChatUtil.PREFIX + "§8Couldn't fetch player stats");
            e.printStackTrace();
            return null;
        });
    }
}
