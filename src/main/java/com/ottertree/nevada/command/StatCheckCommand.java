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

@Command(value="statcheck", description="/statcheck <username> <mode>", aliases={"sc", "stats"}, customHelpMessage={ChatUtil.PREFIX + "§7/§fstatcheck§7: Check Hypixel stats for any player.", ChatUtil.INDENT + "§7Usage: /§fstatcheck§7 <username> <mode>", ChatUtil.INDENT + "§7Aliases: /§fstats§7, /§fsc", ChatUtil.INDENT + "§7Gamemodes: §fbw§7, §fduels§7, §fbridge§7, §fbb"})
public class StatCheckCommand {
    @Main
    private void handle(String username, @Greedy String gameName) {
        ChatUtil.send(ChatUtil.PREFIX + "§7Fetching §f" + username + "§7's stats...");

        final String game = gameName.toLowerCase();
        switch (game) {
            case "bw": break; case "bb": break; case "duels": break; case "bridge": break; default: { ChatUtil.send(ChatUtil.PREFIX + "§7Invalid game (expected bw, bb, duels, bridge)"); return; }
        }

        PlayerUtil.playerExists(username).thenAccept(exists -> {
            if (!exists) {
                ChatUtil.send(ChatUtil.PREFIX + "§7Player not found");
                return;
            }

            Hypixel.INSTANCE.getPlayerProfileFromName(username).thenAccept(profile -> {
                if (profile.hypixel.isNick) {
                    ChatUtil.send(ChatUtil.PREFIX + "§7Player not found");
                    return;
                }
                
                TaglistUtil.getFullTablistCompacted(username).thenAccept(taglistResult -> {
                    String taglist = taglistResult;
                    if (!taglist.isEmpty()) taglist += " ";
                    switch (game) {
                        case "bw": ChatUtil.send(ChatUtil.INDENT + BedwarsUtil.formatLevel(profile.bedwars.level) + " " + taglist + profile.hypixel.displayName + "§7 - Finals: " + BedwarsUtil.colorFinals(profile.bedwars.finalKills) + " §7FKDR: " + BedwarsUtil.colorFKDR(profile.bedwars.getFKDR()) + " §7Wins: " + BedwarsUtil.colorFinals(profile.bedwars.wins) + " §7WLR: " + BedwarsUtil.colorWLR(profile.bedwars.getWLR()) + " §7Beds: " + BedwarsUtil.colorBedsBroken(profile.bedwars.bedsBroken) + " §7BBLR: " + BedwarsUtil.colorBBLR(profile.bedwars.getBBLR())); break;
                        case "bb": ChatUtil.send(ChatUtil.INDENT + BuildBattleUtil.formatScore(profile.buildbattle.score) + " " + taglist + profile.hypixel.displayName + "§7 - Wins: §f" + profile.buildbattle.wins + " §7Solo Wins: §f" + profile.buildbattle.soloWins + " §7Doubles Wins: §f" + profile.buildbattle.doublesWins + " §7GTB Wins: §f" + profile.buildbattle.gtbWins); break;
                        case "duels": ChatUtil.send(ChatUtil.INDENT + taglist + profile.hypixel.displayName + "§7 - Wins: " + DuelsUtil.colorWins(profile.duels.overall.wins) + " §7WLR: " + DuelsUtil.colorWLR(profile.duels.overall.getWLR()) + " §7BWS: " + DuelsUtil.colorBWS(profile.duels.overall.best_winstreak) + " §7WS: " + DuelsUtil.colorWS(profile.duels.overall.winstreak)); break;
                        case "bridge": ChatUtil.send(ChatUtil.INDENT + taglist + profile.hypixel.displayName + "§7 - Wins: " + DuelsUtil.colorWins(profile.duels.bridge.wins) + " §7WLR: " + DuelsUtil.colorWLR(profile.duels.bridge.getWLR()) +  " §7BWS: " + DuelsUtil.colorBWS(profile.duels.bridge.best_winstreak)); break;
                    }
                }).exceptionally(e -> {
                    switch (game) {
                        case "bw": ChatUtil.send(ChatUtil.INDENT + BedwarsUtil.formatLevel(profile.bedwars.level) + " " + profile.hypixel.displayName + "§7 - Finals: " + BedwarsUtil.colorFinals(profile.bedwars.finalKills) + " §7FKDR: " + BedwarsUtil.colorFKDR(profile.bedwars.getFKDR()) + " §7Wins: " + BedwarsUtil.colorFinals(profile.bedwars.wins) + " §7WLR: " + BedwarsUtil.colorWLR(profile.bedwars.getWLR()) + " §7Beds: " + BedwarsUtil.colorBedsBroken(profile.bedwars.bedsBroken) + " §7BBLR: " + BedwarsUtil.colorBBLR(profile.bedwars.getBBLR())); break;
                        case "bb": ChatUtil.send(ChatUtil.INDENT + BuildBattleUtil.formatScore(profile.buildbattle.score) + " " + profile.hypixel.displayName + "§7 - Wins: §f" + profile.buildbattle.wins + " §7Solo Wins: §f" + profile.buildbattle.soloWins + " §7Doubles Wins: §f" + profile.buildbattle.doublesWins + " §7GTB Wins: §f" + profile.buildbattle.gtbWins); break;
                        case "duels": ChatUtil.send(ChatUtil.INDENT + profile.hypixel.displayName + "§7 - Wins: " + DuelsUtil.colorWins(profile.duels.overall.wins) + " §7WLR: " + DuelsUtil.colorWLR(profile.duels.overall.getWLR()) + " §7BWS: " + DuelsUtil.colorBWS(profile.duels.overall.best_winstreak) + " §7WS: " + DuelsUtil.colorWS(profile.duels.overall.winstreak)); break;
                        case "bridge": ChatUtil.send(ChatUtil.INDENT + profile.hypixel.displayName + "§7 - Wins: " + DuelsUtil.colorWins(profile.duels.bridge.wins) + " §7WLR: " + DuelsUtil.colorWLR(profile.duels.bridge.getWLR()) +  "§7BWS: " + DuelsUtil.colorBWS(profile.duels.bridge.best_winstreak)); break;
                    }
                    return null;
                });
            }).exceptionally(e -> {
                if ((e instanceof CompletionException ? e.getCause() : e) instanceof NevadaException) {
                    ChatUtil.send(ChatUtil.PREFIX + "§7" + (e instanceof CompletionException ? e.getCause() : e).getMessage());
                    return null;
                }

                ChatUtil.send(ChatUtil.PREFIX + "§7Couldn't fetch player stats");
                e.printStackTrace();
                return null;
            });
        }).exceptionally(e -> {
            if ((e instanceof CompletionException ? e.getCause() : e) instanceof NevadaException) {
                ChatUtil.send(ChatUtil.PREFIX + "§7" + (e instanceof CompletionException ? e.getCause() : e).getMessage());
                return null;
            }
            
            ChatUtil.send(ChatUtil.PREFIX + "§7Couldn't fetch player stats");
            e.printStackTrace();
            return null;
        });
    }
}
