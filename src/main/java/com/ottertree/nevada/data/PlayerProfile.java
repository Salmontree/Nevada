package com.ottertree.nevada.data;

public class PlayerProfile {
    public static class Bedwars {
        public int finalKills;
        public int finalDeaths;
        public int wins;
        public int losses;
        public int level;

        public float getFKDR() { return finalDeaths != 0.0f ? (float)finalKills / finalDeaths : 0.0f; }
        public float getWLR() { return losses != 0.0f ? (float)wins / losses : 0.0f; }
    }
    public Bedwars bedwars = new Bedwars();
    
    public static class Hypixel {
        public String displayName;
    }
    public Hypixel hypixel = new Hypixel();
}
