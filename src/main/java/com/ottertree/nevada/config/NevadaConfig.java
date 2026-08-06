package com.ottertree.nevada.config;

import com.ottertree.nevada.Nevada;

import cc.polyfrost.oneconfig.config.Config;
import cc.polyfrost.oneconfig.config.annotations.Dropdown;
import cc.polyfrost.oneconfig.config.annotations.Switch;
import cc.polyfrost.oneconfig.config.annotations.Text;
import cc.polyfrost.oneconfig.config.data.Mod;
import cc.polyfrost.oneconfig.config.data.ModType;
import cc.polyfrost.oneconfig.config.data.OptionSize;
import cc.polyfrost.oneconfig.config.annotations.HUD;
import com.ottertree.nevada.hud.BedwarsUpgradesTrapsHUD;
import cc.polyfrost.oneconfig.config.annotations.Slider;

public class NevadaConfig extends Config {
    // Bedwars
    @Switch(name="Anti-Obby Misplace", category="Bedwars", subcategory="Bed Defense")
    public boolean Bedwars_AntiObbyMisplace = false;
    @Switch(name="Only Enable Anti-Obby Misplace in Bedwars", category="Bedwars", subcategory="Bed Defense")
    public boolean Bedwars_AntiObbyMisplace_OnlyInBedwars = true;

    @HUD(name = "Bedwars Upgrades/Traps", category = "Bedwars")
    public BedwarsUpgradesTrapsHUD upgradesTrapsHud = new BedwarsUpgradesTrapsHUD();

    @Switch(name="Enable Tab Stats", category="Bedwars", subcategory="Tab Stats")
    public boolean TabStats_Bedwars_Enable = true;
    @Switch(name="Enable Tab Stats In Lobby", category="Bedwars", subcategory="Tab Stats")
    public boolean TabStats_Bedwars_EnableInLobby = false;
    @Switch(name="Show Stars", category="Bedwars", subcategory="Tab Stats")
    public boolean TabStats_Bedwars_ShowStars = true;
    @Switch(name="Show Tags", category="Bedwars", subcategory="Tab Stats")
    public boolean TabStats_Bedwars_ShowTags = true;
    @Switch(name="Show Custom Stat", category="Bedwars", subcategory="Tab Stats")
    public boolean TabStats_Bedwars_ShowStat = true;
    @Dropdown(name="Custom Stat", options={"Finals", "FKDR", "Wins", "WLR", "Beds", "BBLR"}, category="Bedwars", subcategory="Tab Stats")
    public int TabStats_Bedwars_TablistStat = 1; // FKDR

    // Duels
    @Switch(name="Enable Tab Stats", category="Duels", subcategory="Tab Stats")
    public boolean TabStats_Duels_Enable = true;
    @Switch(name="Enable Tab Stats In Lobby", category="Duels", subcategory="Tab Stats")
    public boolean TabStats_Duels_EnableInLobby = false;
    @Switch(name="Show Tags", category="Duels", subcategory="Tab Stats")
    public boolean TabStats_Duels_ShowTags = true;
    @Switch(name="Show Custom Stat", category="Duels", subcategory="Tab Stats")
    public boolean TabStats_Duels_ShowStat = true;
    @Dropdown(name="Duels Stat", options={"Wins", "WLR", "Current Winstreak", "Best Winstreak"}, category="Duels", subcategory="Tab Stats", size=OptionSize.DUAL)
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

    // Incoming Player Alerts
    @Switch(name="Enable", category="Incoming Player Alerts", subcategory="General")
    public boolean IncomingAlert_Enable = false;
    @Text(name="Alert Message", category="Incoming Player Alerts", subcategory="General")
    public String IncomingAlert_Message = "inc";
    @Dropdown(name="Alert Channel", options={"Party Chat", "All Chat"}, category="Incoming Player Alerts", subcategory="General")
    public int IncomingAlert_Channel = 0; // 0 = Party Chat, 1 = All Chat
    @Slider(name="Alert Radius", min=10f, max=100f, category="Incoming Player Alerts", subcategory="General")
    public float IncomingAlert_Radius = 50f;

    public NevadaConfig() {
        super(new Mod(Nevada.NAME, ModType.HYPIXEL), Nevada.MODID + ".json");
        initialize();

        addDependency("TabStats_Bedwars_EnableInLobby", "TabStats_Bedwars_Enable");
        addDependency("TabStats_Bedwars_ShowStars", "TabStats_Bedwars_Enable");
        addDependency("TabStats_Bedwars_ShowTags", "TabStats_Bedwars_Enable");
        addDependency("TabStats_Bedwars_ShowStat", "TabStats_Bedwars_Enable");
        addDependency("TabStats_Bedwars_TablistStat", "TabStats_Bedwars_ShowStat");
        addDependency("TabStats_Bedwars_TablistStat", "TabStats_Bedwars_Enable");
        addDependency("TabStats_Duels_EnableInLobby", "TabStats_Duels_Enable");
        addDependency("TabStats_Duels_ShowStars", "TabStats_Duels_Enable");
        addDependency("TabStats_Duels_ShowTags", "TabStats_Duels_Enable");
        addDependency("TabStats_Duels_ShowStat", "TabStats_Duels_Enable");
        addDependency("TabStats_Duels_TablistStat", "TabStats_Duels_ShowStat");
        addDependency("TabStats_Duels_TablistStat", "TabStats_Duels_Enable");

        addDependency("Bedwars_AntiObbyMisplace_OnlyInBedwars", "Bedwars_AntiObbyMisplace");

        addDependency("Pregame_OnlyShowOnce", "Pregame_ShowPlayerStats");

        addDependency("IncomingAlert_Message", "IncomingAlert_Enable");
        addDependency("IncomingAlert_Channel", "IncomingAlert_Enable");
        addDependency("IncomingAlert_Radius", "IncomingAlert_Enable");
    }
}
