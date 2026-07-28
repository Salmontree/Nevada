package com.ottertree.nevada.event;

import com.ottertree.nevada.Nevada;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class BlockPlacedEvent {
    @SubscribeEvent
    public void onPlaceBlock(BlockEvent.PlaceEvent e) {
        antiObsidianMisplace(e);
    }

    private void antiObsidianMisplace(BlockEvent.PlaceEvent e) {
        if (!Nevada.config.Bedwars_AntiObbyMisplace) return;
        Block placed = e.placedBlock.getBlock();
        if (placed != Blocks.obsidian) return;

        for (EnumFacing facing : EnumFacing.VALUES) {
            BlockPos neighbor = e.pos.offset(facing);
            if (e.world.getBlockState(neighbor).getBlock() == Blocks.bed)
                return;
        }
        e.setCanceled(true);
    }
}
