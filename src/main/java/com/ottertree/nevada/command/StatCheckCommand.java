package com.ottertree.nevada.command;

import java.io.IOException;

import com.ottertree.nevada.Async;
import com.ottertree.nevada.NevadaCommon;
import com.ottertree.nevada.api.MojangAPI;
import com.ottertree.nevada.stats.PlayerProfile;
import com.ottertree.nevada.stats.backends.Hypixel;

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

                    Async.runOnMainThread(() -> NevadaCommon.addChatText("  §5▌ " + formattedStars + " " + formattedName + "§r§7 - Finals: " + colorFinals(finals) + finals + "§7 FKDR: " + colorFKDR(fkdr) + String.format("%.2f", fkdr) + "§7 Wins: " + colorWins(wins) + wins + "§7 WLR: " + colorWLR(wlr) + String.format("%.2f", wlr)));
                }
            }
        });
    }

    private String colorFKDR(float fkdr) {
        String code = "§7";
        if (fkdr >= 1) code = "§f";
        if (fkdr >= 3) code = "§a";
        if (fkdr >= 5) code = "§2";
        if (fkdr >= 7) code = "§e";
        if (fkdr >= 10) code = "§6";
        if (fkdr >= 20) code = "§c";
        if (fkdr >= 30) code = "§4";
        if (fkdr >= 50) code = "§d";
        if (fkdr >= 100) code = "§5";
        return code;
    }
    private String colorFinals(int finals) {
        String code = "§7";
        if (finals >= 500) code = "§f";
        if (finals >= 1000) code = "§a";
        if (finals >= 2500) code = "§2";
        if (finals >= 5000) code = "§e";
        if (finals >= 7500) code = "§6";
        if (finals >= 15000) code = "§c";
        if (finals >= 25000) code = "§4";
        if (finals >= 50000) code = "§d";
        if (finals >= 100000) code = "§5";
        return code;
    }
    private String colorWLR(float wlr) {
        String code = "§7";
        if (wlr >= 0.3) code = "§f";
        if (wlr >= 0.9) code = "§a";
        if (wlr >= 1.5) code = "§2";
        if (wlr >= 2.1) code = "§e";
        if (wlr >= 3) code = "§6";
        if (wlr >= 6) code = "§c";
        if (wlr >= 9) code = "§4";
        if (wlr >= 15) code = "§d";
        if (wlr >= 30) code = "§5";
        return code;
    }
    private String colorWins(int wins) {
        String code = "§7";
        if (wins >= 150) code = "§f";
        if (wins >= 300) code = "§a";
        if (wins >= 450) code = "§2";
        if (wins >= 1500) code = "§e";
        if (wins >= 2250) code = "§6";
        if (wins >= 4500) code = "§c";
        if (wins >= 7500) code = "§4";
        if (wins >= 15000) code = "§d";
        if (wins >= 30000) code = "§5";
        return code;
    }
}
