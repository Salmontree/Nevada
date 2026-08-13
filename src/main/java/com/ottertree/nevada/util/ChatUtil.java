package com.ottertree.nevada.util;

import com.ottertree.nevada.Nevada;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;

public class ChatUtil {
    private static final Minecraft mc = Minecraft.getMinecraft();
    
    public static final String PREFIX = "§0[§lD§r§0] §r";
    public static final String INDENT = "§0[§lD§r§0] §r"; // "§r  §5▌ §r";

    public static void send(String text) {
        mc.thePlayer.addChatMessage(new ChatComponentText(text));
    }

    public static void say(String text) {
        mc.thePlayer.sendChatMessage(text);
    }

    public static void setActionbar(String text) {
        mc.ingameGUI.setRecordPlayingMessage(text);
    }
}