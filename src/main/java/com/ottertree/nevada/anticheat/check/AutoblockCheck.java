package com.ottertree.nevada.anticheat.check;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.ottertree.nevada.Nevada;
import com.ottertree.nevada.anticheat.ACPlayerData;

public class AutoblockCheck extends Check {
    private static final int SWING_COOLDOWN_MS = 100;
    private static final int MIN_BLOCK_HOLD_MS = 150;
    private static final int PAIR_WINDOW_MIN_MS = 150;
    private static final int PAIR_WINDOW_MAX_MS = 200;
    private static final int HISTORY_WINDOW_MS = 1000;
    private static final int MAX_HISTORY_SIZE = 20;
    private static final int VIOLATION_THRESHOLD = 2;

    private static class SwingRecord {
        final long time;
        final boolean wasBlockingBefore;
        Boolean wasBlockingAfter; // null = not yet determined

        SwingRecord(long time, boolean wasBlockingBefore) {
            this.time = time;
            this.wasBlockingBefore = wasBlockingBefore;
        }
    }

    private final Map<UUID, List<SwingRecord>> swingHistories = new HashMap<>();
    private final Map<UUID, Long> lastSwingDetected = new HashMap<>();

    public AutoblockCheck() {
        super("Autoblock");
    }

    @Override
    public void runCheck(ACPlayerData player) {
        if (!Nevada.config.Anticheat_Autoblock) return;

        final long currentTime = System.currentTimeMillis();
        final boolean isHoldingSword = player.isHoldingSword;
        final boolean isSwinging = player.swingProgress > 0;

        List<SwingRecord> history = swingHistories.computeIfAbsent(player.uuid, k -> new ArrayList<>());
        Long lastSwing = lastSwingDetected.get(player.uuid);

        if (isSwinging && (lastSwing == null || currentTime - lastSwing > SWING_COOLDOWN_MS)) {
            boolean hasBeenBlockingLongEnough = player.isBlocking
                && player.blockingStartTime != 0
                && (currentTime - player.blockingStartTime >= MIN_BLOCK_HOLD_MS);

            history.add(new SwingRecord(currentTime, hasBeenBlockingLongEnough));
            lastSwingDetected.put(player.uuid, currentTime);

            if (history.size() > MAX_HISTORY_SIZE) {
                history.remove(0);
            }
        }

        for (SwingRecord swing : history) {
            if (swing.wasBlockingAfter == null) {
                long timeSinceSwing = currentTime - swing.time;
                if (timeSinceSwing >= PAIR_WINDOW_MIN_MS && timeSinceSwing <= PAIR_WINDOW_MAX_MS) {
                    swing.wasBlockingAfter = player.isBlocking;
                } else if (timeSinceSwing > PAIR_WINDOW_MAX_MS) {
                    swing.wasBlockingAfter = false;
                }
            }
        }

        int autoBlockCount = 0;
        if (isHoldingSword) {
            for (SwingRecord swing : history) {
                if (currentTime - swing.time < HISTORY_WINDOW_MS
                    && swing.wasBlockingAfter != null
                    && swing.wasBlockingBefore
                    && swing.wasBlockingAfter) {
                    autoBlockCount++;
                }
            }
        }

        if (autoBlockCount >= VIOLATION_THRESHOLD) {
            int level = addViolation(player, 1);

            if (shouldAlert(player)) {
                flag(player, level);
                markAlert(player);
            }
        } else {
            reduceViolation(player, 1);
        }
    }
}