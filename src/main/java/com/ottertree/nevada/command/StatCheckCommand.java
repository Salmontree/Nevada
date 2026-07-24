package com.ottertree.nevada.command;

import com.ottertree.nevada.api.Hypixel;
import com.ottertree.nevada.util.BedwarsUtil;
import com.ottertree.nevada.util.ChatUtil;
import com.ottertree.nevada.util.PlayerUtil;

import cc.polyfrost.oneconfig.utils.commands.annotations.Command;
import cc.polyfrost.oneconfig.utils.commands.annotations.Main;

@Command(value="statcheck", description="/statcheck <username> <mode> [recency]", aliases={"sc"})
public class StatCheckCommand {
    @Main
    private void handle(String username) {
        ChatUtil.send(ChatUtil.PREFIX + "§8Fetching §7" + username + "§8's stats...");

        PlayerUtil.playerExists(username).thenAccept(exists -> {
            if (!exists) {
                ChatUtil.send(ChatUtil.INDENT + "§8Player not found");
                return;
            }

            Hypixel.INSTANCE.getPlayerProfileFromName(username).thenAccept(profile -> {
                ChatUtil.send(ChatUtil.INDENT + BedwarsUtil.formatLevel(profile.bedwars.level) + " " + profile.hypixel.displayName + "§7 - Finals: " + BedwarsUtil.colorFinals(profile.bedwars.finalKills) + " §7FKDR: " + BedwarsUtil.colorFKDR(profile.bedwars.getFKDR()) + " §7Wins: " + BedwarsUtil.colorFinals(profile.bedwars.wins) + " §7WLR: " + BedwarsUtil.colorWLR(profile.bedwars.getWLR()));
            }).exceptionally(e -> {
                ChatUtil.send(ChatUtil.PREFIX + "§cError: " + e.getMessage());
                return null;
            });
        });
    }
}
