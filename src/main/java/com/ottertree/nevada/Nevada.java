package com.ottertree.nevada;

import com.ottertree.nevada.command.PlayCommand;
import com.ottertree.nevada.command.StatCheckCommand;
import com.ottertree.nevada.command.StatusCommand;
import com.ottertree.nevada.command.TagViewCommand;
import com.ottertree.nevada.command.UuidCommand;
import com.ottertree.nevada.config.NevadaConfig;

import cc.polyfrost.oneconfig.utils.commands.CommandManager;
import cc.polyfrost.oneconfig.utils.hypixel.HypixelUtils;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

@Mod(modid = NevadaCommon.MODID, name = NevadaCommon.NAME, version = NevadaCommon.VERSION)
public class Nevada {
    public static NevadaConfig config;
    
    @Mod.EventHandler
    public void onInit(FMLInitializationEvent event) {
        config = new NevadaConfig();
        HypixelUtils.INSTANCE.initialize();

        CommandManager.INSTANCE.registerCommand(new PlayCommand());
        CommandManager.INSTANCE.registerCommand(new StatCheckCommand());
        CommandManager.INSTANCE.registerCommand(new StatusCommand());
        CommandManager.INSTANCE.registerCommand(new TagViewCommand());
        CommandManager.INSTANCE.registerCommand(new UuidCommand());
    }
}
