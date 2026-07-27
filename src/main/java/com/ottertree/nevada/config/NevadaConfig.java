package com.ottertree.nevada.config;

import com.ottertree.nevada.Nevada;

import cc.polyfrost.oneconfig.config.Config;
import cc.polyfrost.oneconfig.config.annotations.Dropdown;
import cc.polyfrost.oneconfig.config.annotations.Switch;
import cc.polyfrost.oneconfig.config.annotations.Text;
import cc.polyfrost.oneconfig.config.data.Mod;
import cc.polyfrost.oneconfig.config.data.ModType;
import cc.polyfrost.oneconfig.config.data.OptionSize;

public class NevadaConfig extends Config {
    // Tab Stats
    @Switch(name="Enable Tab Stats", category="Tab Stats", subcategory="General")
    public boolean TabStats_Enable = true;
    @Switch(name="Enable Tab Stats In Lobby", category="Tab Stats", subcategory="General")
    public boolean TabStats_EnableInLobby = false;

    @Switch(name="Show Stars", category="Tab Stats", subcategory="Bedwars")
    public boolean TabStats_Bedwars_ShowStars = true;
    @Switch(name="Show Tags", category="Tab Stats", subcategory="Bedwars")
    public boolean TabStats_Bedwars_ShowTags = true;
    @Switch(name="Show Custom Stat", category="Tab Stats", subcategory="Bedwars")
    public boolean TabStats_Bedwars_ShowStat = true;
    @Dropdown(name="Custom Stat", options={"Finals", "FKDR", "Wins", "WLR", "Beds", "BBLR"}, category="Tab Stats", subcategory="Bedwars")
    public int TabStats_Bedwars_TablistStat = 1; // FKDR
    
    @Switch(name="Show Tags", category="Tab Stats", subcategory="Duels")
    public boolean TabStats_Duels_ShowTags = true;
    @Switch(name="Show Custom Stat", category="Tab Stats", subcategory="Duels")
    public boolean TabStats_Duels_ShowStat = true;
    @Dropdown(name="Duels Stat", options={"Wins", "WLR", "Current Winstreak", "Best Winstreak"}, category="Tab Stats", subcategory="Duels", size=OptionSize.DUAL)
    public int TabStats_Duels_TablistStat = 1; // WLR

    // API Keys
    @Text(name="Hypixel", secure=true, category="API Keys")
    public String APIKeys_Hypixel;
    @Text(name="Urchin", secure=true, category="API Keys")
    public String APIKeys_Urchin;
    @Text(name="Aurora", secure=true, category="API Keys")
    public String APIKeys_Aurora;

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
        addDependency("TabStats_Bedwars_ShowStars", "TabStats_Enable");
        addDependency("TabStats_Bedwars_ShowTags", "TabStats_Enable");
        addDependency("TabStats_Bedwars_ShowStat", "TabStats_Enable");
        addDependency("TabStats_Bedwars_TablistStat", "TabStats_Bedwars_ShowStat");
        addDependency("TabStats_Bedwars_TablistStat", "TabStats_Enable");
        addDependency("TabStats_Duels_ShowStars", "TabStats_Enable");
        addDependency("TabStats_Duels_ShowTags", "TabStats_Enable");
        addDependency("TabStats_Duels_ShowStat", "TabStats_Enable");
        addDependency("TabStats_Duels_TablistStat", "TabStats_Duels_ShowStat");
        addDependency("TabStats_Duels_TablistStat", "TabStats_Enable");

        addDependency("Pregame_OnlyShowOnce", "Pregame_ShowPlayerStats");
    }
}
