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
    private boolean wasActive = false;
    private int tickCounter = 0;
    private int lastGlobalAlertTick = -1000;

    private final Map<String, Integer> lastAlertTick = new HashMap<>();

    private static final int CHECK_INTERVAL = 10;
    private static final int PER_PLAYER_COOLDOWN = 300;
    private static final int GLOBAL_COOLDOWN = 60;

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        Minecraft mc = Minecraft.getMinecraft();

        if (mc.thePlayer == null || mc.theWorld == null) return;

        tickCounter++;

        // DEBUG: proves that the event is actually running.
        // Only send it once every 20 ticks so it doesn't completely spam chat.
        if (tickCounter % 20 == 0) {
            ChatUtil.send("§c[DEBUG] IncomingAlertEvent is running!");
        }

        boolean active = BedwarsUtil.inBedwars() && !BedwarsUtil.inPregame();

        if (!active) {
            spawnPos = null;
            wasActive = false;
            lastAlertTick.clear();
            return;
        }

        if (!wasActive) {
            spawnPos = mc.thePlayer.getPosition();
            wasActive = true;

            ChatUtil.send(
                "§7[DEBUG] Alert system activated. spawnPos=" + spawnPos
            );
        }

        if (!Nevada.config.IncomingAlert_Enable) return;
        if (tickCounter % CHECK_INTERVAL != 0) return;
        if (spawnPos == null) return;
        if (tickCounter - lastGlobalAlertTick < GLOBAL_COOLDOWN) return;

        double radius = Nevada.config.IncomingAlert_Radius;
        double radiusSq = radius * radius;

        for (EntityPlayer player : mc.theWorld.playerEntities) {
            if (player == mc.thePlayer) continue;

            boolean enemy = isConfirmedEnemy(mc.thePlayer, player);

            double dx = player.posX - spawnPos.getX();
            double dy = player.posY - spawnPos.getY();
            double dz = player.posZ - spawnPos.getZ();

            double distSq = dx * dx + dy * dy + dz * dz;
            double dist = Math.sqrt(distSq);

            ChatUtil.send(
                "§7[DEBUG] "
                + player.getName()
                + " enemy="
                + enemy
                + " dist="
                + String.format("%.1f", dist)
                + "/"
                + radius
            );

            if (!enemy) continue;

            if (distSq <= radiusSq) {
                String name = player.getName();

                Integer lastTick = lastAlertTick.get(name);

                if (lastTick != null
                        && tickCounter - lastTick < PER_PLAYER_COOLDOWN) {
                    continue;
                }

                lastAlertTick.put(name, tickCounter);
                lastGlobalAlertTick = tickCounter;

                sendAlert();
                return;
            }
        }
    }

    private boolean isConfirmedEnemy(EntityPlayer self, EntityPlayer other) {
        Scoreboard scoreboard = self.getWorldScoreboard();

        ScorePlayerTeam selfTeam =
            scoreboard.getPlayersTeam(self.getName());

        ScorePlayerTeam otherTeam =
            scoreboard.getPlayersTeam(other.getName());

        ChatUtil.send(
            "§8[DEBUG-TEAM] "
            + other.getName()
            + " selfTeam="
            + (selfTeam == null
                ? "null"
                : selfTeam.getRegisteredName())
            + " otherTeam="
            + (otherTeam == null
                ? "null"
                : otherTeam.getRegisteredName())
        );

        if (selfTeam == null || otherTeam == null) {
            return false;
        }

        return !selfTeam.getRegisteredName()
            .equals(otherTeam.getRegisteredName());
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
