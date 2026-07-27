package com.ottertree.nevada.config;

import com.ottertree.nevada.Nevada;

import cc.polyfrost.oneconfig.config.Config;
import cc.polyfrost.oneconfig.config.annotations.Dropdown;
import cc.polyfrost.oneconfig.config.annotations.Info;
import cc.polyfrost.oneconfig.config.annotations.Switch;
import cc.polyfrost.oneconfig.config.annotations.Text;
import cc.polyfrost.oneconfig.config.data.InfoType;
import cc.polyfrost.oneconfig.config.data.Mod;
import cc.polyfrost.oneconfig.config.data.ModType;
import cc.polyfrost.oneconfig.config.data.OptionSize;

public class NevadaConfig extends Config {
    // API Keys
    @Text(name="Hypixel", secure=true, category="API Keys")
    public String APIKeys_Hypixel;
    @Text(name="Urchin", secure=true, category="API Keys")
    public String APIKeys_Urchin;
    @Text(name="Aurora", secure=true, category="API Keys")
    public String APIKeys_Aurora;

    // Tab Stats
    @Switch(name="Enable Tab Stats", category="Tab Stats")
    public boolean TabStats_Enable = true;
    @Switch(name="Enable Tab Stats In Lobby", category="Tab Stats")
    public boolean TabStats_EnableInLobby = false;

    @Switch(name="Show Stars", category="Tab Stats")
    public boolean TabStats_ShowStars = true;
    @Switch(name="Show Tags", category="Tab Stats")
    public boolean TabStats_ShowTags = true;

    @Info(text="Stat that shows up next to name in tablist", type=InfoType.INFO, category="Tab Stats")
    private boolean TabStats_TablistStat_Info;
    @Switch(name="Show Tablist Stat", category="Tab Stats")
    public boolean TabStats_ShowStat = true;

    @Dropdown(name="Bedwars Stat", options={"Finals", "FKDR", "Wins", "WLR", "Beds", "BBLR"}, category="Tab Stats", size=OptionSize.DUAL)
    public int TabStats_TablistStat_Bedwars = 1; // FKDR
    @Dropdown(name="Duels Stat", options={"Wins", "WLR", "Current Winstreak", "Best Winstreak"}, category="Tab Stats", size=OptionSize.DUAL)
    public int TabStats_TablistStat_Duels = 1; // WLR

    // Pregame
    @Switch(name="Auto-show Player Stats in Pregame", category="Pregame")
    public boolean Pregame_ShowPlayerStats = true;
    @Switch(name="Only Show Once", category="Pregame")
    public boolean Pregame_OnlyShowOnce = true;

    // Anticheat
    @Switch(name="Check Autoblock", category="Anticheat")
    public boolean Anticheat_Autoblock = true;
    @Switch(name="Check Tower", category="Anticheat")
    public boolean Anticheat_Tower = true;
    @Switch(name="Check Eagle", category="Anticheat")
    public boolean Anticheat_Eagle = true;
    @Switch(name="Check Scaffold", category="Anticheat")
    public boolean Anticheat_Scaffold = true;

    public NevadaConfig() {
        super(new Mod(Nevada.NAME, ModType.HYPIXEL), Nevada.MODID + ".json");
        initialize();

        addDependency("TabStats_EnableInLobby", "TabStats_Enable");
        addDependency("TabStats_ShowStars", "TabStats_Enable");
        addDependency("TabStats_ShowTags", "TabStats_Enable");
        addDependency("TabStats_ShowStat", "TabStats_Enable");
        addDependency("TabStats_TablistStat", "TabStats_ShowStat");
        addDependency("TabStats_TablistStat", "TabStats_Enable");

        addDependency("Pregame_OnlyShowOnce", "Pregame_ShowPlayerStats");
    }
}
