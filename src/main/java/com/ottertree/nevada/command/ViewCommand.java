package com.ottertree.nevada.command;

import java.util.ArrayList;

import com.ottertree.nevada.Nevada;
import com.ottertree.nevada.api.Urchin;
import com.ottertree.nevada.data.Tag;
import com.ottertree.nevada.util.ChatUtil;
import com.ottertree.nevada.util.PlayerUtil;

import cc.polyfrost.oneconfig.utils.commands.annotations.Command;
import cc.polyfrost.oneconfig.utils.commands.annotations.Main;

@Command(value="view", description="/view <username>", aliases={"v"})
public class ViewCommand {
    @Main
    private void handle(String username) {
        ChatUtil.send(ChatUtil.PREFIX + "§8Fetching §7" + username + "§8's tags...");

        PlayerUtil.playerExists(username).thenAccept(exists -> {
            if (!exists) {
                ChatUtil.send(ChatUtil.INDENT + "§8Player not found");
                return;
            }
        
            ArrayList<Tag> list = new ArrayList<Tag>();

            if (Nevada.config.Blacklists_EnableUrchin)
            Urchin.INSTANCE.getPlayerTaglist(username).thenAccept(tags -> {
                list.addAll(tags);
            
                if (list.isEmpty()) {
                    ChatUtil.send(ChatUtil.INDENT + "§8Found no tags!");
                    return;
                }

                list.forEach(tag -> {
                    ChatUtil.send(ChatUtil.INDENT + "§8Found Urchin tag: §4" + tag.typeTitle + " §7(" + tag.reason + ")");
                });
            });
        });
    }
}
