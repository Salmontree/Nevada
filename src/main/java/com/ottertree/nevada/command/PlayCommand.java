package com.ottertree.nevada.command;

import com.ottertree.nevada.util.ChatUtil;

import cc.polyfrost.oneconfig.utils.commands.annotations.Command;
import cc.polyfrost.oneconfig.utils.commands.annotations.Greedy;
import cc.polyfrost.oneconfig.utils.commands.annotations.Main;

@Command(value="play", description="/play <game>", customHelpMessage={ChatUtil.PREFIX + "§8/§7play§8: Provides shortcuts for Hypixel's /play.", ChatUtil.INDENT + "§8Usage: /§7play §8<game>"})
public class PlayCommand {
    @Main
    private void handle(@Greedy String game) {
        switch (game) {
            case "bw1": ChatUtil.say("/play bedwars_eight_one"); return;
            case "bw2": ChatUtil.say("/play bedwars_eight_two"); return;
            case "bw3": ChatUtil.say("/play bedwars_four_three"); return;
            case "bw4": ChatUtil.say("/play bedwars_four_four"); return;
        }

        ChatUtil.say("/play " + game);
    }
}
