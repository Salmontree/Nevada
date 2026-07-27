package com.ottertree.nevada.anticheat.check;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.ottertree.nevada.Nevada;
import com.ottertree.nevada.anticheat.ACPlayerData;
import com.ottertree.nevada.util.ChatUtil;

public abstract class Check {
    private final String name;

    private static final String ACPREFIX = "§r§8[§5" + Nevada.NAME + " §cAC§8] §r";
    private static final long ALERT_COOLDOWN_MS = 2000;
    protected static boolean DEBUG = false;

    private final Map<UUID, Integer> violationLevels = new HashMap<>();
    private final Map<UUID, Long> lastAlertTimes = new HashMap<>();

    public Check(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    protected int addViolation(ACPlayerData player, int weight) {
        return violationLevels.merge(player.uuid, weight, (oldVal, inc) -> Math.max(0, oldVal + inc));
    }

    protected int reduceViolation(ACPlayerData player, int weight) {
        return violationLevels.merge(player.uuid, -weight, (oldVal, dec) -> Math.max(0, oldVal + dec));
    }

    protected int getViolationLevel(ACPlayerData player) {
        return violationLevels.getOrDefault(player.uuid, 0);
    }

    protected boolean shouldAlert(ACPlayerData player) {
        long now = System.currentTimeMillis();
        Long last = lastAlertTimes.get(player.uuid);
        return last == null || now - last > ALERT_COOLDOWN_MS;
    }

    protected void markAlert(ACPlayerData player) {
        lastAlertTimes.put(player.uuid, System.currentTimeMillis());
    }

    protected void flag(ACPlayerData player, int violationLevel) {
        ChatUtil.send(ACPREFIX + player.displayName + " §cfailed " + name + " §8(x" + violationLevel + ")");
    }

    protected void debugLog(String message) {
        if (DEBUG) System.out.println(message);
    }

    public void runCheck(ACPlayerData player) {}
}