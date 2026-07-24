package com.ottertree.nevada;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;

public class NevadaCommon {
    // Sets the variables from `gradle.properties`. See the `blossom` config in `build.gradle.kts`.
    public static final String MODID = "@ID@";
    public static final String NAME = "@NAME@";
    public static final String VERSION = "@VER@";

    public static void addChatText(String text) {
        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(text));
    }
    public static void addChatTextP(String text) {
        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§r§8[§5" + NevadaCommon.NAME + "§8] §r" + text));
    }

    public static void sendChat(String text) {
        Minecraft.getMinecraft().thePlayer.sendChatMessage(text);
    }
}
