package com.ottertree.nevada.anticheat.check;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.ottertree.nevada.Nevada;
import com.ottertree.nevada.anticheat.ACPlayerData;

public class TowerCheck extends Check {
    private static final double LOOKING_DOWN_PITCH = 30.0;
    private static final double FAST_ASCEND_THRESHOLD = 5.5;
    private static final double PROPER_RATIO_THRESHOLD = 0.8;
    private static final long HISTORY_RESET_MS = 2000;
    private static final int MAX_HISTORY_SIZE = 15;
    private static final int MIN_HISTORY_FOR_CHECK = 8;
    private static final double CONSISTENCY_THRESHOLD = 0.8;
    private static final double MIN_HEIGHT_GAIN = 3.0;
    private static final double MIN_TIMESPAN_SEC = 0.4;
    private static final double MAX_TIMESPAN_SEC = 1.5;
    private static final int VIOLATION_WEIGHT = 2;

    private static class HeightSample {
        final double y;
        final long time;

        HeightSample(double y, long time) {
            this.y = y;
            this.time = time;
        }
    }

    private static class TowerData {
        List<HeightSample> heightHistory = new ArrayList<>();
        long lastReset;

        TowerData(long now) {
            lastReset = now;
        }
    }

    private final Map<UUID, TowerData> towerDataMap = new HashMap<>();

    public TowerCheck() {
        super("Tower");
    }

    @Override
    public void runCheck(ACPlayerData player) {
        if (!Nevada.config.Anticheat_Tower) return;

        final long currentTime = System.currentTimeMillis();
        final double verticalSpeed = player.motionY;
        final double horizontalSpeed = Math.sqrt(player.motionX * player.motionX + player.motionZ * player.motionZ);

        final boolean isLookingDown = player.pitch >= LOOKING_DOWN_PITCH;
        final boolean isSwingingBlock = player.swingProgress > 0 && player.isHoldingBlock;
        final boolean hasNoJumpBoost = !player.hasJumpBoost;
        final boolean isAscendingFast = verticalSpeed > FAST_ASCEND_THRESHOLD;

        final double verticalToHorizontalRatio = horizontalSpeed > 0 ? verticalSpeed / horizontalSpeed : verticalSpeed;
        final boolean hasProperTowerRatio = verticalToHorizontalRatio >= PROPER_RATIO_THRESHOLD;

        final boolean hasRecentDamage = player.hurtTime > 0; // hurtTime counts down from 10 ticks (~500ms) after damage

        TowerData towerData = towerDataMap.computeIfAbsent(player.uuid, k -> new TowerData(currentTime));

        if (currentTime - towerData.lastReset > HISTORY_RESET_MS) {
            towerData.heightHistory.clear();
            towerData.lastReset = currentTime;
        }

        if (isLookingDown && isSwingingBlock && isAscendingFast && hasProperTowerRatio && hasNoJumpBoost && !hasRecentDamage) {
            towerData.heightHistory.add(new HeightSample(player.posY, currentTime));

            if (towerData.heightHistory.size() > MAX_HISTORY_SIZE) {
                towerData.heightHistory.remove(0);
            }
        }

        if (towerData.heightHistory.size() >= MIN_HISTORY_FOR_CHECK) {
            List<HeightSample> heights = towerData.heightHistory;
            HeightSample start = heights.get(0);
            HeightSample end = heights.get(heights.size() - 1);

            double totalHeightGain = end.y - start.y;
            double timeSpan = (end.time - start.time) / 1000.0;

            int consistentRiseCount = 0;
            for (int i = 1; i < heights.size(); i++) {
                if (heights.get(i).y > heights.get(i - 1).y) {
                    consistentRiseCount++;
                }
            }

            double consistencyRatio = (double) consistentRiseCount / (heights.size() - 1);
            boolean hasConsistentRise = consistencyRatio >= CONSISTENCY_THRESHOLD;
            boolean hasSignificantHeight = totalHeightGain >= MIN_HEIGHT_GAIN;
            boolean hasGoodTimespan = timeSpan >= MIN_TIMESPAN_SEC && timeSpan <= MAX_TIMESPAN_SEC;

            debugLog(String.format(
                "[TowerA] %s - VSpeed: %.2f, HSpeed: %.2f, Ratio: %.2f, HeightGain: %.2f, TimeSpan: %.2fs, ConsistentRise: %d/%d (%.2f), Consistent: %b, SignificantHeight: %b, GoodTimespan: %b",
                player.displayName, verticalSpeed, horizontalSpeed, verticalToHorizontalRatio, totalHeightGain,
                timeSpan, consistentRiseCount, heights.size() - 1, consistencyRatio, hasConsistentRise, hasSignificantHeight, hasGoodTimespan
            ));

            if (hasConsistentRise && hasSignificantHeight && hasGoodTimespan) {
                int level = addViolation(player, VIOLATION_WEIGHT);

                if (shouldAlert(player)) {
                    flag(player, level);
                    markAlert(player);
                }
            } else {
                reduceViolation(player, 1);
            }
        }
    }
}