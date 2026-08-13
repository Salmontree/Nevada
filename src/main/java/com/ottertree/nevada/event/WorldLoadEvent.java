package com.ottertree.nevada.event;

import com.ottertree.nevada.cache.PlayerNickCache;
import com.ottertree.nevada.cache.PlayerProfileCache;
import com.ottertree.nevada.util.ChatUtil;

import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class WorldLoadEvent {

    private static boolean announced = false;

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        if (event.world.isRemote) {
            PlayerProfileCache.INSTANCE.clear();
            PlayerNickCache.INSTANCE.clear();

            if (!announced) {
                announced = true;
                ChatUtil.send("§0§lD§8§le§7§lf§8§le§7§la§0§lt §f§lm§8§lo§7§ld §8§ll§7§lo§f§la§7§ld§8§le§7§ld§8§l.");
            }
        }
    }
}