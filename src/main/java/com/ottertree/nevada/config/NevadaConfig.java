package com.ottertree.nevada.config;

import com.ottertree.nevada.Nevada;

import cc.polyfrost.oneconfig.config.Config;
import cc.polyfrost.oneconfig.config.annotations.Switch;
import cc.polyfrost.oneconfig.config.annotations.Text;
import cc.polyfrost.oneconfig.config.data.Mod;
import cc.polyfrost.oneconfig.config.data.ModType;

public class NevadaConfig extends Config {
    @Text(name="Hypixel", secure=true, category="API Keys")
    public String APIKeys_Hypixel;
    @Text(name="Urchin", secure=true, category="API Keys")
    public String APIKeys_Urchin;

    @Switch(name="Enable Urchin", category="Blacklists")
    public Boolean Blacklists_EnableUrchin = false;

    public NevadaConfig() {
        super(new Mod(Nevada.NAME, ModType.HYPIXEL), Nevada.MODID + ".json");
        initialize();
    }
}
