package com.ottertree.nevada.command;

import com.ottertree.nevada.util.CacheUtil;
import com.ottertree.nevada.util.ChatUtil;

import cc.polyfrost.oneconfig.utils.commands.annotations.Command;
import cc.polyfrost.oneconfig.utils.commands.annotations.Main;

@Command(value="clearcache", description="/clearcache", aliases={"refresh"}, customHelpMessage={ChatUtil.PREFIX + "§7/§fclearcache§7: Clears all caches.", ChatUtil.INDENT + "§7Usage: /§fclearcache", ChatUtil.INDENT + "§7Aliases: /§frefresh"})
public class ClearCacheCommand {
    @Main
    private void handle() {
        CacheUtil.clearAllCaches();
        ChatUtil.send(ChatUtil.PREFIX + "§7Cleared all caches");
    }
}
