package com.ottertree.nevada.command;

import com.ottertree.nevada.util.CacheUtil;
import com.ottertree.nevada.util.ChatUtil;

import cc.polyfrost.oneconfig.utils.commands.annotations.Command;
import cc.polyfrost.oneconfig.utils.commands.annotations.Main;

@Command(value="clearcache", description="/clearcache", aliases={"refresh"}, customHelpMessage={ChatUtil.PREFIX + "§8/§7clearcache§8: Clears all caches.", ChatUtil.INDENT + "§8Usage: /§7clearcache", ChatUtil.INDENT + "§8Aliases: /§7refresh"})
public class ClearCacheCommand {
    @Main
    private void handle() {
        CacheUtil.clearAllCaches();
        ChatUtil.send(ChatUtil.PREFIX + "§8Cleared all caches");
    }
}
