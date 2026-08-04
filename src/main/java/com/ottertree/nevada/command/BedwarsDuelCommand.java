package com.ottertree.nevada.command;

import com.ottertree.nevada.util.ChatUtil;
import cc.polyfrost.oneconfig.utils.commands.annotations.Command;
import cc.polyfrost.oneconfig.utils.commands.annotations.Greedy;
import cc.polyfrost.oneconfig.utils.commands.annotations.Main;

@Command(value = "bwd", description = "/bwd <player>", customHelpMessage = {
        ChatUtil.PREFIX + "§7/§fbwd§7: Challenges a player to a Bed Wars Rush duel.",
        ChatUtil.INDENT + "§7Usage: /§fbwd §7<player>"
})
public class BedwarsDuelCommand {
    @Main
    private void handle(@Greedy String player) {
        ChatUtil.say("/duel " + player + " bed wars rush ");
    }
}
