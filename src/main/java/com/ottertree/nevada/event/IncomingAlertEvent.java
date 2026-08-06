package com.ottertree.nevada.event;

import java.util.HashMap;
import java.util.Map;

import com.ottertree.nevada.Nevada;
import com.ottertree.nevada.util.BedwarsUtil;
import com.ottertree.nevada.util.ChatUtil;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class IncomingAlertEvent {

    private BlockPos spawnPos = null;
    private boolean wasInBedwars = false;
    private int tickCounter = 0;
    private final Map<String, Integer> lastAlertTick = new HashMap<>();

    private static final int CHECK_INTERVAL = 10; // run the check every 10 ticks (~0.5s)
    private static final int ALERT_COOLDOWN = 100; // ~5 seconds before re-alerting the same player

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        tickCounter++;

        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null || mc.theWorld == null) return;

        if (!BedwarsUtil.inBedwars()) {
            spawnPos = null;
            wasInBedwars = false;
            lastAlertTick.clear();
            return;
        }

        if (!wasInBedwars) {
            spawnPos = mc.thePlayer.getPosition();
            wasInBedwars = true;
        }

        if (!Nevada.config.IncomingAlert_Enable) return;
        if (tickCounter % CHECK_INTERVAL != 0) return;
        if (spawnPos == null) return;

        double radius = Nevada.config.IncomingAlert_Radius;
        double radiusSq = radius * radius;

        for (EntityPlayer player : mc.theWorld.playerEntities) {
            if (player == mc.thePlayer) continue;
            if (isTeammate(mc.thePlayer, player)) continue;

            double dx = player.posX - spawnPos.getX();
            double dy = player.posY - spawnPos.getY();
            double dz = player.posZ - spawnPos.getZ();
            double distSq = dx * dx + dy * dy + dz * dz;

            if (distSq <= radiusSq) {
                String name = player.getName();
                Integer lastTick = lastAlertTick.get(name);
                if (lastTick != null && tickCounter - lastTick < ALERT_COOLDOWN) continue;

                lastAlertTick.put(name, tickCounter);
                sendAlert();
            }
        }
    }

    private boolean isTeammate(EntityPlayer self, EntityPlayer other) {
        Scoreboard scoreboard = self.getWorldScoreboard();
        ScorePlayerTeam selfTeam = scoreboard.getPlayersTeam(self.getName());
        ScorePlayerTeam otherTeam = scoreboard.getPlayersTeam(other.getName());
        if (selfTeam == null || otherTeam == null) return false;
        return selfTeam.getRegisteredName().equals(otherTeam.getRegisteredName());
    }

    private void sendAlert() {
        String message = Nevada.config.IncomingAlert_Message;
        if (Nevada.config.IncomingAlert_Channel == 0) {
            ChatUtil.say("/pc " + message);
        } else {
            ChatUtil.say(message);
        }
    }
}
