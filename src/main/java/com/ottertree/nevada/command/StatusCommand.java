package com.ottertree.nevada.command;

import java.io.IOException;

import com.ottertree.nevada.Async;
import com.ottertree.nevada.NevadaCommon;
import com.ottertree.nevada.stats.backends.Hypixel;

import cc.polyfrost.oneconfig.utils.commands.annotations.Command;
import cc.polyfrost.oneconfig.utils.commands.annotations.Main;

@Command(value = "nstatus", description = "/nstatus <username>", aliases = {"st"})
public class StatusCommand {
    @Main
    private void handle(String name) {
        Async.run(() -> {
            Async.runOnMainThread(() -> NevadaCommon.addChatTextP("§8Fetching §7" + name + "'s§8 status..."));

            String status = "";
            try { status = Hypixel.getPlayerProfile(name).hypixelStatus; }
            catch (IOException e) { Async.runOnMainThread(() -> NevadaCommon.addChatTextP("§cError: " + e.getMessage())); }

            final String statusfuckyoujavawhyareyoulikethis = status;
            Async.runOnMainThread(() -> NevadaCommon.addChatText("  §5▌ §8Status: §7" + statusfuckyoujavawhyareyoulikethis));
        });
    }
}
