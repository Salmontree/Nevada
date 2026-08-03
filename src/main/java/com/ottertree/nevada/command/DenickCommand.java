package com.ottertree.nevada.command;

import java.util.concurrent.CompletionException;

import com.ottertree.nevada.api.Aurora;
import com.ottertree.nevada.api.Hypixel;
import com.ottertree.nevada.util.ChatUtil;
import com.ottertree.nevada.util.SkinUtil;

import cc.polyfrost.oneconfig.utils.commands.annotations.Command;
import cc.polyfrost.oneconfig.utils.commands.annotations.Main;

@Command(value="denick", description="/denick <skin|finals|beds> <value>", aliases={"dn"}, customHelpMessage={ChatUtil.PREFIX + "§7/§fdenick§7: Denick players via. their skin, finals, or beds.", ChatUtil.INDENT + "§7Usage: /§fdenick§7 <skin|finals|beds> <value>", ChatUtil.INDENT + "§7Aliases: /§fdn"})
public class DenickCommand {
    @Main
    private void handle(String key, String value) {
        try {
            switch (key) {
                case "skin": denickSkin(value); return;
                case "finals": denickFinals(Integer.valueOf(value)); return;
                case "beds": denickBeds(Integer.valueOf(value)); return;
            }
        } catch (NumberFormatException e) {
            ChatUtil.send(ChatUtil.PREFIX + "§7'" + key + "' must be an integer");
            return;
        }

        ChatUtil.send(ChatUtil.PREFIX + "§7Invalid usage: use /denick <skin|finals|beds> <value>");
    }
    private void denickSkin(String name) {
        ChatUtil.send(ChatUtil.PREFIX + "§7Denicking §f" + name + "§7...");

        String realName = SkinUtil.skinDenick(name);

        if (realName != null)
            Hypixel.INSTANCE.getPlayerProfileFromName(realName).thenAccept(profile -> {
                ChatUtil.send(ChatUtil.INDENT + "§aPlayer denicked as " + profile.hypixel.displayName + "§a!");
            }).exceptionally(e -> {
                if ((e instanceof CompletionException ? e.getCause() : e) instanceof Error) {
                    ChatUtil.send(ChatUtil.PREFIX + "§7" + (e instanceof CompletionException ? e.getCause() : e).getMessage());
                    return null;
                }

                ChatUtil.send(ChatUtil.PREFIX + "§7Could not denick " + name);
                e.printStackTrace();
                return null;
            });
        else
            ChatUtil.send(ChatUtil.PREFIX + "§7Could not denick " + name);
    }

    private void denickFinals(int finals) {
        ChatUtil.send(ChatUtil.PREFIX + "§7Searching for players...");

        Aurora.INSTANCE.denickFinals(finals).thenAccept(responses -> {
            if (responses.isEmpty()) {
                ChatUtil.send(ChatUtil.INDENT + "§7No players found");
                return;
            }
            
            responses.forEach(response -> {
                ChatUtil.send(ChatUtil.INDENT + "§7Found player: §f" + response.name + " §7(" + response.distance + ")");
            });
        }).exceptionally(e -> {
            if ((e instanceof CompletionException ? e.getCause() : e) instanceof Error) {
                ChatUtil.send(ChatUtil.PREFIX + "§7" + (e instanceof CompletionException ? e.getCause() : e).getMessage());
                return null;
            }

            ChatUtil.send(ChatUtil.PREFIX + "§7Could not denick player");
            e.printStackTrace();
            return null;
        });
    }

    private void denickBeds(int beds) {
        ChatUtil.send(ChatUtil.PREFIX + "§7Searching for players...");
        
        Aurora.INSTANCE.denickBeds(beds).thenAccept(responses -> {
            if (responses.isEmpty()) {
                ChatUtil.send(ChatUtil.INDENT + "§7No players found");
                return;
            }

            responses.forEach(response -> {
                ChatUtil.send(ChatUtil.INDENT + "§7Found player: §f" + response.name + " §7(" + response.distance + ")");
            });
        }).exceptionally(e -> {
            if ((e instanceof CompletionException ? e.getCause() : e) instanceof Error) {
                ChatUtil.send(ChatUtil.PREFIX + "§7" + (e instanceof CompletionException ? e.getCause() : e).getMessage());
                return null;
            }

            ChatUtil.send(ChatUtil.PREFIX + "§7Could not denick player");
            e.printStackTrace();
            return null;
        });
    }
}
