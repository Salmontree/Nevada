package com.ottertree.nevada.command;

import com.ottertree.nevada.util.ChatUtil;

import cc.polyfrost.oneconfig.utils.commands.annotations.Command;
import cc.polyfrost.oneconfig.utils.commands.annotations.Main;

@Command(value="denick", description="/denick <skin|finals|beds> <value>", aliases={"dn"})
public class DenickCommand {
    @Main
    private void handle(String key, String value) {
        try {
            switch (key) {
                case "skin": denickSkin(value); break;
                case "finals": denickFinals(Integer.valueOf(value)); break;
                case "beds": denickBeds(Integer.valueOf(value)); break;
            }
        } catch (NumberFormatException e) {
            ChatUtil.send(ChatUtil.PREFIX + "§c'" + key + "' must be an integer");
        }

        ChatUtil.send(ChatUtil.PREFIX + "§cInvalid usage: use /denick <skin|finals|beds> <value>");
    }
    private void denickSkin(String name) {
        
    }

    private void denickFinals(int finals) {
        ChatUtil.send(ChatUtil.PREFIX + "§cFinals denicking not implemented yet");
    }

    private void denickBeds(int beds) {
        ChatUtil.send(ChatUtil.PREFIX + "§cBeds denicking implemented yet");
    }
}
