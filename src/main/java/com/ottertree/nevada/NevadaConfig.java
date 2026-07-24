package com.ottertree.nevada;

import cc.polyfrost.oneconfig.config.Config;
import cc.polyfrost.oneconfig.config.annotations.Switch;
import cc.polyfrost.oneconfig.config.annotations.Text;
import cc.polyfrost.oneconfig.config.data.Mod;
import cc.polyfrost.oneconfig.config.data.ModType;

public class NevadaConfig extends Config {
    @Text(name="Hypixel", secure=true, category="API Keys")
    public static String APIKey_Hypixel = "";
    @Text(name="Urchin (Coral)", secure=true, category="API Keys")
    public static String APIKey_Coral = "";

    @Switch(name="Use Urchin (Coral)", category="Tags", subcategory="Taglists")
    public static boolean Tags_UseCoral = false;
    
    public NevadaConfig() {
        super(new Mod(NevadaCommon.NAME, ModType.HYPIXEL), NevadaCommon.MODID + ".json");
        initialize();
    }
}
