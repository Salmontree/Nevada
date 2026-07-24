package com.ottertree.nevada.data;

public class PlayerProfile {
    public static class Bedwars {
        public int finalKills;
        public int finalDeaths;
        public int wins;
        public int losses;
        public int level;

        public float getFKDR() { return (float)finalKills / finalDeaths; }
        public float getWLR() { return (float)wins / losses; }
    }
    public Bedwars bedwars = new Bedwars();
    
    public static class Hypixel {
        public String displayName;
    }
    public Hypixel hypixel = new Hypixel();
}
