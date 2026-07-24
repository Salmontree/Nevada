package com.ottertree.nevada.command;

import java.io.IOException;

import com.ottertree.nevada.Async;
import com.ottertree.nevada.NevadaCommon;
import com.ottertree.nevada.api.MojangAPI;
import com.ottertree.nevada.stats.PlayerProfile;
import com.ottertree.nevada.stats.backends.Hypixel;
import com.ottertree.nevada.util.BedwarsUtil;

import cc.polyfrost.oneconfig.utils.commands.annotations.Command;
import cc.polyfrost.oneconfig.utils.commands.annotations.Main;

@Command(value = "statcheck", description = "/statcheck <username> <bw | sw | sb | bb | mm | duels>", aliases = {"sc"})
public class StatCheckCommand {
    @Main
    private void handle(String name, String gameName) {
        Async.run(() -> {
            Async.runOnMainThread(() -> NevadaCommon.addChatTextP("§8Fetching §7" + name + "'s§8 stats..."));

            try { MojangAPI.getPlayerInfo(name); }
            catch (IOException e) { Async.runOnMainThread(() -> NevadaCommon.addChatTextP("§cError: " + e.getMessage())); return; }
            
            String game = gameName.toLowerCase();
            game = game.replace("bedwars", "bw"); game = game.replace("skywars", "sw"); game = game.replace("skyblock", "sb"); game = game.replace("build_battle", "bb"); game = game.replace("murder_mystery", "mm");
            switch (game) {
                case "bw": break; case "sw": break; case "sb": break; case "bb": break; case "mm": break; case "duels": break;
                default: {
                    Async.runOnMainThread(() -> NevadaCommon.addChatTextP("§cError: Invalid game"));
                    return;
                }
            }

            switch (game) {
                case "bw": {
                    PlayerProfile profile = null; try { profile = Hypixel.getPlayerProfile(name); } catch (IOException e) { NevadaCommon.addChatTextP("§cError: " + e.getMessage()); return; }

                    int finals = profile.bedwarsFinalKills;
                    float fkdr = (float)profile.bedwarsFinalKills / profile.bedwarsFinalDeaths;
                    int wins = profile.bedwarsWins;
                    float wlr = (float)profile.bedwarsWins / profile.bedwarsLosses;
                    String formattedName = profile.hypixelDisplayName;
                    String formattedStars = profile.bedwarsLevelFormatted;

                    Async.runOnMainThread(() -> NevadaCommon.addChatText("  §5▌ " + formattedStars + " " + formattedName + "§r§7 - Finals: " + BedwarsUtil.colorFinals(finals) + finals + "§7 FKDR: " + BedwarsUtil.colorFKDR(fkdr) + String.format("%.2f", fkdr) + "§7 Wins: " + BedwarsUtil.colorWins(wins) + wins + "§7 WLR: " + BedwarsUtil.colorWLR(wlr) + String.format("%.2f", wlr)));
                }
            }
        });
    }
}
