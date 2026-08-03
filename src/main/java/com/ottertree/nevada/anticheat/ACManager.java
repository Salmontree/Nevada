package com.ottertree.nevada.anticheat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.ottertree.nevada.anticheat.check.Check;

import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemSword;
import net.minecraft.potion.Potion;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class ACManager {
    public static final ACManager INSTANCE = new ACManager();
    private final Map<UUID, ACPlayerData> playerDataMap = new HashMap<>();
    private final List<Check> checks = new ArrayList<>();

    public ACManager() {}

    public void initialize() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void runChecks(ACPlayerData player) {
        checks.forEach(check -> {
            check.runCheck(player);
        });
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        ACPlayerData data = playerDataMap.get(event.player.getUniqueID());
        if (data == null)
            data = new ACPlayerData();

        data.displayName = event.player.getDisplayNameString();
        data.uuid = event.player.getUniqueID();

        data.isUsingItem = event.player.isUsingItem();
        data.isHoldingBow = event.player.getHeldItem() != null && event.player.getHeldItem().getItem().equals(Item.itemRegistry.getObjectById(261));
        data.isHoldingSword = event.player.getHeldItem() != null && event.player.getHeldItem().getItem() instanceof ItemSword;
        data.isHoldingConsumable = event.player.getHeldItem() != null && (event.player.getHeldItem().getItem().getItemUseAction(event.player.getHeldItem()) == EnumAction.EAT || event.player.getHeldItem().getItem().getItemUseAction(event.player.getHeldItem()) == EnumAction.DRINK);

        data.isSprinting = event.player.isSprinting();

        boolean isBlockingNow = event.player.isBlocking();
        if (isBlockingNow && !data.isBlocking) {
            data.blockingStartTime = System.currentTimeMillis();
        } else if (!isBlockingNow) {
            data.blockingStartTime = 0;
        }
        data.isBlocking = isBlockingNow;
        data.swingProgress = event.player.swingProgress;

        data.pitch = event.player.rotationPitch;
        data.onGround = event.player.onGround;
        data.isHoldingBlock = event.player.getHeldItem() != null && event.player.getHeldItem().getItem() instanceof ItemBlock;
        data.isSneaking = event.player.isSneaking();
        data.motionX = event.player.motionX;
        data.motionY = event.player.motionY;
        data.motionZ = event.player.motionZ;

        data.posY = event.player.posY;

        data.hasJumpBoost = event.player.isPotionActive(Potion.jump);
        data.hurtTime = event.player.hurtTime;

        playerDataMap.put(event.player.getUniqueID(), data);
        runChecks(data);
    }
}