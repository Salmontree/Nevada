package com.ottertree.nevada.command;

import com.ottertree.nevada.NevadaCommon;

import cc.polyfrost.oneconfig.utils.commands.annotations.Command;
import cc.polyfrost.oneconfig.utils.commands.annotations.Main;

@Command(value = "play", description = "/play <game> <mode>", aliases = {"p"})
public class PlayCommand {
    @Main
    private void handle(String game, String mode) {
        game = game.replace("bedwars", "bw");
        switch (game) {
            case "bw": switch (mode) {
                case "solo": NevadaCommon.sendChat("/play bedwars_eight_one"); return;
                case "doubles": NevadaCommon.sendChat("/play bedwars_eight_two"); return;
                case "threes": NevadaCommon.sendChat("/play bedwars_four_three"); return;
                case "fours": NevadaCommon.sendChat("/play bedwars_four_four"); return;
                case "1s": NevadaCommon.sendChat("/play bedwars_eight_one"); return;
                case "2s": NevadaCommon.sendChat("/play bedwars_eight_two"); return;
                case "3s": NevadaCommon.sendChat("/play bedwars_four_three"); return;
                case "4s": NevadaCommon.sendChat("/play bedwars_four_four"); return;
                case "1": NevadaCommon.sendChat("/play bedwars_eight_one"); return;
                case "2": NevadaCommon.sendChat("/play bedwars_eight_two"); return;
                case "3": NevadaCommon.sendChat("/play bedwars_four_three"); return;
                case "4": NevadaCommon.sendChat("/play bedwars_four_four"); return;
                case "4v4": NevadaCommon.sendChat("/play bedwars_two_four"); return;
                // case "dream": NevadaCommon.sendChat("/play bedwars_four_four"); // TODO: Make this actually work with current dream mode in rotation
            }
        }

        NevadaCommon.addChatTextP("§cError: Invalid game");
    }
}
