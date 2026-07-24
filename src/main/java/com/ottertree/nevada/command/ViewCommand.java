package com.ottertree.nevada.command;

import com.ottertree.nevada.util.ChatUtil;

import cc.polyfrost.oneconfig.utils.commands.annotations.Command;
import cc.polyfrost.oneconfig.utils.commands.annotations.Main;

@Command(value="view", description="/view <username>", aliases={"v"})
public class ViewCommand {
    @Main
    private void handle(String username) {
        ChatUtil.send(ChatUtil.PREFIX + "§8Fetching §7" + username + "§8's tags...");

        
    }
}
