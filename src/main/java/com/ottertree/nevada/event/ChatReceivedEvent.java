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
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
public class ChatReceivedEvent {
    private static final Pattern playerMessagePattern = Pattern.compile("^((?:§.)*(?:(\\[[^\\]]+\\]) (?:§.)*)?(\\w+))(?:§.)*: (.*)$");
    private static final Pattern lobbyMessagePattern = Pattern.compile("^((?:§.)*(?:(?:§.)*\\[[^\\]]+\\](?:§.)*\\s*)*(\\w+))(?:§.)*: (.*)$");

    private ArrayList<String> checkedPregamePlayers = new ArrayList<>();
    private ArrayList<String> mentionedByPlayers = new ArrayList<>();

    @SubscribeEvent
    public void onChatReceived(ClientChatReceivedEvent event) {
        String text = event.message.getFormattedText();
        pregameStatsLookup(text);
        mentionStatsLookup(text);
    }

    private void pregameStatsLookup(String text) {
        if (!Nevada.config.Pregame_ShowPlayerStats) return;
        if (!BedwarsUtil.inPregame()) {
            if (!checkedPregamePlayers.isEmpty()) checkedPregamePlayers.clear();
            return;
        }
        if (!checkedPregamePlayers.isEmpty() && !Nevada.config.Pregame_OnlyShowOnce) checkedPregamePlayers.clear();
        Matcher matcher = playerMessagePattern.matcher(text);
        if (!matcher.matches()) return;
        if (Nevada.config.Pregame_OnlyShowOnce) {
            if (checkedPregamePlayers.contains(matcher.group(3))) return;
            checkedPregamePlayers.add(matcher.group(3));
        }
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
                
                TaglistUtil.getFullTablistCompacted(matcher.group(3)).thenAccept(taglistResult -> {
                    String taglist = taglistResult;
                    if (!taglist.isEmpty()) taglist += " ";
                    ChatUtil.send(ChatUtil.PREFIX + BedwarsUtil.formatLevel(profile.bedwars.level) + " " + taglist + profile.hypixel.displayName + "§8 - Finals: " + BedwarsUtil.colorFinals(profile.bedwars.finalKills) + " §8FKDR: " + BedwarsUtil.colorFKDR(profile.bedwars.getFKDR()) + " §8Wins: " + BedwarsUtil.colorFinals(profile.bedwars.wins) + " §8WLR: " + BedwarsUtil.colorWLR(profile.bedwars.getWLR()));
                }).exceptionally(e -> {
                    e.printStackTrace();
                    return null;
                });
            });
        });
    }

    private void mentionStatsLookup(String text) {
        if (!Nevada.config.Lobby_ShowStatsWhenMentioned) return;
        if (!BedwarsUtil.inLobby()) {
            if (!mentionedByPlayers.isEmpty()) mentionedByPlayers.clear();
            return;
        }

        Matcher matcher = lobbyMessagePattern.matcher(text);
        boolean matched = matcher.matches();
        ChatUtil.send("§7[DEBUG] matched=" + matched + " text=" + text.replace("§", "&"));
        if (!matched) return;

        String sender = matcher.group(2);
        String message = matcher.group(3);

        if (Minecraft.getMinecraft().thePlayer == null) return;
        String ownName = Minecraft.getMinecraft().thePlayer.getName();

        ChatUtil.send("§7[DEBUG] sender=" + sender + " message=" + message + " ownName=" + ownName);

        if (sender.equalsIgnoreCase(ownName)) return;
        if (message == null || !message.toLowerCase().contains(ownName.toLowerCase())) {
            ChatUtil.send("§7[DEBUG] name not in message, skipping");
            return;
        }

        if (mentionedByPlayers.contains(sender)) {
            ChatUtil.send("§7[DEBUG] already alerted this sender, skipping");
            return;
        }
        mentionedByPlayers.add(sender);

        PlayerUtil.playerExists(sender).thenAccept(exists -> {
            if (!exists) {
                ChatUtil.send(ChatUtil.PREFIX + matcher.group(1) + " §5is a Nick!");
                return;
            }
            Hypixel.INSTANCE.getPlayerProfileFromName(sender).thenAccept(profile -> {
                if (profile.hypixel.isNick) {
                    ChatUtil.send(ChatUtil.PREFIX + matcher.group(1) + " §5is a Nick!");
                    return;
                }

                TaglistUtil.getFullTablistCompacted(sender).thenAccept(taglistResult -> {
                    String taglist = taglistResult;
                    if (!taglist.isEmpty()) taglist += " ";
                    ChatUtil.send(ChatUtil.PREFIX + BedwarsUtil.formatLevel(profile.bedwars.level) + " " + taglist + profile.hypixel.displayName + "§8 - Finals: " + BedwarsUtil.colorFinals(profile.bedwars.finalKills) + " §8FKDR: " + BedwarsUtil.colorFKDR(profile.bedwars.getFKDR()) + " §8Wins: " + BedwarsUtil.colorFinals(profile.bedwars.wins) + " §8WLR: " + BedwarsUtil.colorWLR(profile.bedwars.getWLR()));
                }).exceptionally(e -> {
                    e.printStackTrace();
                    return null;
                });
            });
        });
    }
}