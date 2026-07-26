package com.ottertree.nevada.command;

import com.ottertree.nevada.api.Aurora;
import com.ottertree.nevada.api.Hypixel;
import com.ottertree.nevada.util.ChatUtil;
import com.ottertree.nevada.util.SkinUtil;

import cc.polyfrost.oneconfig.utils.commands.annotations.Command;
import cc.polyfrost.oneconfig.utils.commands.annotations.Main;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;

@Command(value="denick", description="/denick <skin|finals|beds> <value>", aliases={"dn"})
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
            ChatUtil.send(ChatUtil.PREFIX + "§c'" + key + "' must be an integer");
            return;
        }

        ChatUtil.send(ChatUtil.PREFIX + "§cInvalid usage: use /denick <skin|finals|beds> <value>");
    }
    private void denickSkin(String name) {
        ChatUtil.send(ChatUtil.PREFIX + "§8Denicking §7" + name + "§8...");

        NetworkPlayerInfo playerInfo = null;

        for (NetworkPlayerInfo info : Minecraft.getMinecraft()
            .getNetHandler()
            .getPlayerInfoMap()) {
            if (info.getGameProfile().getName().equalsIgnoreCase(name)) {
                playerInfo = info;
                break;
            }
        }

        if (playerInfo == null) {
            ChatUtil.send(ChatUtil.INDENT + "§8Player not found");
            return;
        }

        String realName = SkinUtil.getRealName(playerInfo);

        if (realName != null)
            Hypixel.INSTANCE.getPlayerProfileFromName(realName).thenAccept(profile -> {
                ChatUtil.send(ChatUtil.INDENT + "§aPlayer is nicked as " + profile.hypixel.displayName + "§a!");
            });
        else
            ChatUtil.send(ChatUtil.INDENT + "§cCould not denick " + name);
    }

    private void denickFinals(int finals) {
        ChatUtil.send(ChatUtil.PREFIX + "§8Searching for players...");

        Aurora.INSTANCE.denickFinals(finals).thenAccept(responses -> {
            if (responses.isEmpty()) {
                ChatUtil.send(ChatUtil.INDENT + "§8No players found");
                return;
            }
            
            responses.forEach(response -> {
                ChatUtil.send(ChatUtil.INDENT + "§8Found player: §7" + response.name + " §8(" + response.distance + ")");
            });
        });
    }

    private void denickBeds(int beds) {
        ChatUtil.send(ChatUtil.PREFIX + "§8Searching for players...");
        
        Aurora.INSTANCE.denickBeds(beds).thenAccept(responses -> {
            if (responses.isEmpty()) {
                ChatUtil.send(ChatUtil.INDENT + "§8No players found");
                return;
            }

            responses.forEach(response -> {
                ChatUtil.send(ChatUtil.INDENT + "§8Found player: §7" + response.name + " §8(" + response.distance + ")");
            });
        });
    }
}
