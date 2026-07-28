package com.ottertree.nevada.event;

import com.ottertree.nevada.Nevada;
import com.ottertree.nevada.util.BedwarsUtil;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class InteractEvent {
        @SubscribeEvent
    public void onPlayerInteract(PlayerInteractEvent event) {
        antiObsidianMisplace(event);
    }

    private void antiObsidianMisplace(PlayerInteractEvent event) {
        if (event.action != PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) return;
        if (!Nevada.config.Bedwars_AntiObbyMisplace) return;
        if (Nevada.config.Bedwars_AntiObbyMisplace_OnlyInBedwars && !BedwarsUtil.inBedwars()) return;
 
        ItemStack heldItem = event.entityPlayer.getHeldItem();
        if (heldItem == null || Block.getBlockFromItem(heldItem.getItem()) != Blocks.obsidian) return;
 
        for (EnumFacing facing : EnumFacing.VALUES) {
            BlockPos neighbor = event.pos.offset(event.face).offset(facing);
            if (event.world.getBlockState(neighbor).getBlock() == Blocks.bed) {
                return;
            }
        }
        event.setCanceled(true);
    }
}
