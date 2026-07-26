package com.ottertree.nevada.command;

import com.ottertree.nevada.util.ChatUtil;

import cc.polyfrost.oneconfig.utils.commands.annotations.Command;
import cc.polyfrost.oneconfig.utils.commands.annotations.Greedy;
import cc.polyfrost.oneconfig.utils.commands.annotations.Main;

@Command(value="play", description="/play <game>", customHelpMessage={ChatUtil.PREFIX + "§8/§7play§8: Provides shortcuts for Hypixel's /play.", ChatUtil.INDENT + "§8Usage: /§7play §8<game>"})
public class PlayCommand {
    @Main
    private void handle(@Greedy String game) {
        ChatUtil.say("/play " + game);
    }
}
