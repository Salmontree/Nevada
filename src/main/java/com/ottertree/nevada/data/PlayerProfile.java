package com.ottertree.nevada.data;

public class PlayerProfile {
    public static class Bedwars {
        public int finalKills;
        public int finalDeaths;
        public int wins;
        public int losses;
        public int bedsBroken;
        public int bedsLost;
        public int level;
        public float getFKDR() { return finalDeaths != 0 ? (float)finalKills / finalDeaths : finalKills; }
        public float getWLR() { return losses != 0 ? (float)wins / losses : wins; }
        public float getBBLR() { return bedsLost != 0 ? (float)bedsBroken / bedsLost : bedsBroken; }
    }
    public Bedwars bedwars = new Bedwars();

    public static class Duels {
        public static class Statistics {
            public int wins;
            public int losses;
            public int best_winstreak;
            public int winstreak;
            public float getWLR() { return losses != 0 ? (float)wins / losses : wins; }
        }

        public Statistics overall = new Statistics();
        public Statistics bridge = new Statistics();
    }
    public Duels duels = new Duels();

    public static class BuildBattle {
        public int score;
        public int wins;
        public int soloWins;
        public int doublesWins;
        public int gtbWins;
    }
    public BuildBattle buildbattle = new BuildBattle();
    
    public static class Hypixel {
        public String displayName;
        public boolean isNick;
    }
    public Hypixel hypixel = new Hypixel();
}
