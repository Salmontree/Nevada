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
import com.ottertree.nevada.util.bedwars.BedwarsUpgradesTrapsManager;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
public class ChatReceivedEvent {
    private static final Pattern playerMessagePattern = Pattern.compile("^((?:§.)*(?:(\\[[^\\]]+\\]) (?:§.)*)?(\\w+))(?:§.)*: (.*)$");
    private ArrayList<String> checkedPregamePlayers = new ArrayList<>();
    private boolean wasInBedwars = false;

    @SubscribeEvent
    public void onChatReceived(ClientChatReceivedEvent event) {
        String text = event.message.getFormattedText();
        pregameStatsLookup(text);
        upgradesTrapsTracking(text);
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

    private void upgradesTrapsTracking(String text) {
        boolean inBedwars = BedwarsUtil.inBedwars();

        // Reset when a new Bedwars game starts (transition from not-in-bedwars to in-bedwars)
        if (inBedwars && !wasInBedwars) {
            BedwarsUpgradesTrapsManager.getInstance().resetUpgradesAndTraps();
        }
        wasInBedwars = inBedwars;

        if (!inBedwars) return;

        BedwarsUpgradesTrapsManager.getInstance().processPurchaseMessage(text);
        BedwarsUpgradesTrapsManager.getInstance().processTrapTriggeredMessage(text);
    }

    // private void autoWho(String text) {
    //     if (!Nevada.config.General_AutoWho) return;
    //     if (text.equals("▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬") && !bw1) bw1 = true;
    //     if (text.equals("                                  Bed Wars") && bw1) bw2 = true;
    //     if (text.equals("") && bw2) bw3 = true;
    //     if (text.equals("     Protect your bed and destroy the enemy beds.") && bw3) bw4 = true;
    //     // We've just queued into a Bedwars game
    //     if (bw4) {
    //         bw1 = false; bw2 = false; bw3 = false; bw4 = false;
        
    //         ChatUtil.say("/who");
    //     }
    // }
}
