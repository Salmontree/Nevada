package com.ottertree.nevada;

import com.ottertree.nevada.anticheat.ACManager;
import com.ottertree.nevada.command.ClearCacheCommand;
import com.ottertree.nevada.command.DebugCommand;
import com.ottertree.nevada.command.DenickCommand;
import com.ottertree.nevada.command.StatCheckCommand;
import com.ottertree.nevada.command.ViewCommand;
import com.ottertree.nevada.config.NevadaConfig;
import com.ottertree.nevada.event.ChatReceivedEvent;
import com.ottertree.nevada.event.WorldLoadEvent;

import cc.polyfrost.oneconfig.utils.commands.CommandManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

@Mod(modid = Nevada.MODID, name = Nevada.NAME, version = Nevada.VERSION)
public class Nevada {
    public static final String MODID = "@ID@";
    public static final String NAME = "@NAME@";
    public static final String VERSION = "@VER@";

    public static NevadaConfig config;

    @Mod.EventHandler
    public void onInit(FMLInitializationEvent event) {
        config = new NevadaConfig();
        ACManager.INSTANCE.initialize();

        MinecraftForge.EVENT_BUS.register(new ChatReceivedEvent());
        MinecraftForge.EVENT_BUS.register(new WorldLoadEvent());

        CommandManager.register(new ClearCacheCommand());
        CommandManager.register(new DebugCommand());
        CommandManager.register(new DenickCommand());
        CommandManager.register(new StatCheckCommand());
        CommandManager.register(new ViewCommand());
    }
}
