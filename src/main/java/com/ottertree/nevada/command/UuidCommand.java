package com.ottertree.nevada.command;

import java.io.IOException;

import com.google.gson.JsonObject;
import com.ottertree.nevada.Async;
import com.ottertree.nevada.NevadaCommon;
import com.ottertree.nevada.api.MojangAPI;

import cc.polyfrost.oneconfig.utils.commands.annotations.Command;
import cc.polyfrost.oneconfig.utils.commands.annotations.Main;
import net.minecraft.client.Minecraft;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;

@Command(value = "uuid", description = "/uuid <username>")
public class UuidCommand {
    @Main
    private void handle(String name) {
        Async.run(() -> {
            try {
                JsonObject result = MojangAPI.getPlayerInfo(name);

                ChatStyle nameStyle = new ChatStyle();
                nameStyle.setChatClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, result.get("name").getAsString()));
                nameStyle.setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ChatComponentText("Click to put name in chat.")));
                nameStyle.setColor(EnumChatFormatting.GRAY);
                ChatComponentText nameComponent = new ChatComponentText(result.get("name").getAsString());
                nameComponent.setChatStyle(nameStyle);
                ChatComponentText namePrefix = new ChatComponentText("  §5▌ §8Name: §7");
                namePrefix.appendSibling(nameComponent);
                Async.runOnMainThread(() -> Minecraft.getMinecraft().thePlayer.addChatMessage(namePrefix));

                ChatStyle uuidStyle = new ChatStyle();
                uuidStyle.setChatClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, result.get("id").getAsString()));
                uuidStyle.setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ChatComponentText("Click to put UUID in chat.")));
                uuidStyle.setColor(EnumChatFormatting.GRAY);
                ChatComponentText uuidComponent = new ChatComponentText(result.get("id").getAsString());
                uuidComponent.setChatStyle(uuidStyle);
                ChatComponentText uuidPrefix = new ChatComponentText("  §5▌ §8UUID: §7");
                uuidPrefix.appendSibling(uuidComponent);
                Async.runOnMainThread(() -> Minecraft.getMinecraft().thePlayer.addChatMessage(uuidPrefix));
            }
            catch (IOException e) { NevadaCommon.addChatTextP("§cError: " + e.getMessage()); return; }
        });
        NevadaCommon.addChatTextP("§8Fetching §7" + name + "'s§8 UUID...");
    }
}
