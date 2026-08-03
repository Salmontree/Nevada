package com.ottertree.nevada.command;

import com.ottertree.nevada.util.ChatUtil;

import cc.polyfrost.oneconfig.utils.commands.annotations.Command;
import cc.polyfrost.oneconfig.utils.commands.annotations.Main;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;

@Command(value="debug", description="/debug ... *used for development purposes, does NOT do anything beneficial*")
public class DebugCommand {
    @Main
    private void handle(int finals) {
        // ChatUtil.send(String.valueOf(Item.itemRegistry.getIDForObject(Minecraft.getMinecraft().thePlayer.getHeldItem().getItem())));
    }
}
