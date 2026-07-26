package com.ottertree.nevada.command;

import com.ottertree.nevada.util.CacheUtil;

import cc.polyfrost.oneconfig.utils.commands.annotations.Command;
import cc.polyfrost.oneconfig.utils.commands.annotations.Main;

@Command(value="clearcache", description="/clearcache", aliases={"refresh"})
public class ClearCacheCommand {
    @Main
    private void handle() {
        CacheUtil.clearAllCaches();;
    }
}
