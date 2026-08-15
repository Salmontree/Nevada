package com.ottertree.nevada.event;

import com.ottertree.nevada.cache.PlayerNickCache;
import com.ottertree.nevada.cache.PlayerProfileCache;
import com.ottertree.nevada.util.ChatUtil;

import net.minecraft.client.Minecraft;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class WorldLoadEvent {

    private static boolean announced = false;
    private static boolean pendingAnnounce = false;
    private static int delayTicks = 0;

    private static final int ANNOUNCE_DELAY = 40; // 2 seconds

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        if (event.world.isRemote) {
            PlayerProfileCache.INSTANCE.clear();
            PlayerNickCache.INSTANCE.clear();

            if (!announced) {
                pendingAnnounce = true;
                delayTicks = 0;
            }
        }
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        if (!pendingAnnounce) return;

        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null) return;

        delayTicks++;
        if (delayTicks < ANNOUNCE_DELAY) return;

        pendingAnnounce = false;
        announced = true;
        ChatUtil.send("§0§lD§8§le§7§lf§8§le§7§la§0§lt §f§lm§8§lo§7§ld §8§ll§7§lo§f§la§7§ld§8§le§7§ld§8§l.");
    }
}