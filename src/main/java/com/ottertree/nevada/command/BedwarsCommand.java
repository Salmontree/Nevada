package com.ottertree.nevada.command;

import cc.polyfrost.oneconfig.utils.commands.annotations.Command;
import cc.polyfrost.oneconfig.utils.commands.annotations.Main;

import com.ottertree.nevada.util.ChatUtil;

@Command(value="bw", description="/bw <username>", customHelpMessage={ChatUtil.PREFIX + "§7/§fbw§7: Shortcut for /statcheck <username> bw", ChatUtil.INDENT + "§7Usage: /§fbw §7<username>"})
public class BedwarsCommand {
    @Main
    private void handle(String username) {
        new StatCheckCommand().handle(username, "bw");
    }
}
