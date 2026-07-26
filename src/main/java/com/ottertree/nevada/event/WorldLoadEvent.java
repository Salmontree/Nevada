package com.ottertree.nevada.event;

import com.ottertree.nevada.cache.PlayerNickCache;
import com.ottertree.nevada.cache.PlayerProfileCache;

import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class WorldLoadEvent {
    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        if (event.world.isRemote) {
            PlayerProfileCache.INSTANCE.clear();
            PlayerNickCache.INSTANCE.clear(); // you never know, someone might change their nick to be their normal player skin for some reason
        }
    }
}