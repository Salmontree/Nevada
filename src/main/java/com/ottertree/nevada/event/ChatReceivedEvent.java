package com.ottertree.nevada.event;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ottertree.nevada.Nevada;
import com.ottertree.nevada.api.Hypixel;
import com.ottertree.nevada.util.BedwarsUtil;
import com.ottertree.nevada.util.ChatUtil;
import com.ottertree.nevada.util.PlayerUtil;
import com.ottertree.nevada.util.TaglistUtil;

import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ChatReceivedEvent {
    private Boolean bw1 = false;
    private Boolean bw2 = false;
    private Boolean bw3 = false;
    private Boolean bw4 = false;

    private static final Pattern playerMessagePattern = Pattern.compile("^((?:§.)*(?:(\\[[^\\]]+\\]) (?:§.)*)?(\\w+))(?:§.)*: (.*)$");
    private ArrayList<String> checkedPregamePlayers = new ArrayList<>();

    @SubscribeEvent
    public void onChatReceived(ClientChatReceivedEvent event) {
        pregameStatsLookup(event.message.getFormattedText());
        autoWho(event.message.getUnformattedText());
    }

    private void pregameStatsLookup(String text) {
        if (!BedwarsUtil.inPregame()) {
            if (!checkedPregamePlayers.isEmpty()) checkedPregamePlayers.clear();
            return;
        }

        Matcher matcher = playerMessagePattern.matcher(text);
        if (!matcher.matches()) return;

        if (checkedPregamePlayers.contains(matcher.group(3))) return;
        checkedPregamePlayers.add(matcher.group(3));

        PlayerUtil.playerExists(matcher.group(3)).thenAccept(exists -> {
            if (!exists) {
                ChatUtil.send(ChatUtil.PREFIX + matcher.group(1) + " §5is a Nick!");
                return;
            }

            Hypixel.INSTANCE.getPlayerProfileFromName(matcher.group(3)).thenAccept(profile -> {
                if (profile.hypixel.isNick) {
                    ChatUtil.send(ChatUtil.PREFIX + matcher.group(1) + " §5is a Nick!");
                    return;
                }
                
                ChatUtil.send(ChatUtil.PREFIX + BedwarsUtil.formatLevel(profile.bedwars.level) + " " + profile.hypixel.displayName + "§7 - §8Finals: " + BedwarsUtil.colorFinals(profile.bedwars.finalKills) + " §8FKDR: " + BedwarsUtil.colorFKDR(profile.bedwars.getFKDR()) + " §8Wins: " + BedwarsUtil.colorFinals(profile.bedwars.wins) + " §8WLR: " + BedwarsUtil.colorWLR(profile.bedwars.getWLR()) + " " + TaglistUtil.getFullTablistCompacted(matcher.group(3)));
            });
        });
    }

    private void autoWho(String text) {
        if (!Nevada.config.General_AutoWho) return;

        if (text.equals("▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬") && !bw1) bw1 = true;
        if (text.equals("                                  Bed Wars") && bw1) bw2 = true;
        if (text.equals("") && bw2) bw3 = true;
        if (text.equals("     Protect your bed and destroy the enemy beds.") && bw3) bw4 = true;

        // We've just queued into a Bedwars game
        if (bw4) {
            bw1 = false; bw2 = false; bw3 = false; bw4 = false;
        
            ChatUtil.say("/who");
        }
    }
}