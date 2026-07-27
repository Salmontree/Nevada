package com.ottertree.nevada.anticheat.check;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.ottertree.nevada.Nevada;
import com.ottertree.nevada.anticheat.ACPlayerData;

public class EagleCheck extends Check {
    private static final double LOOKING_DOWN_PITCH = 30.0;
    private static final double FAST_SPEED_THRESHOLD = 2.0;
    private static final double EXCESSIVE_SHIFT_SPEED_THRESHOLD = 2.5;
    private static final double DIAGONAL_MIN_SPEED = 0.1;
    private static final double CARDINAL_TOLERANCE = 15.0;
    private static final double[] CARDINAL_ANGLES = {0, 90, 180, 270};
    private static final long SHIFT_WINDOW_MS = 2000;
    private static final int SHIFT_COUNT_THRESHOLD = 6;
    private static final int VIOLATION_WEIGHT = 3;

    private final Map<UUID, List<Long>> shiftHistories = new HashMap<>();
    private final Map<UUID, Boolean> wasSneaking = new HashMap<>();

    public EagleCheck() {
        super("Eagle");
    }

    @Override
    public void runCheck(ACPlayerData player) {
        if (!Nevada.config.Anticheat_Eagle) return;

        final long currentTime = System.currentTimeMillis();

        List<Long> shiftHistory = shiftHistories.computeIfAbsent(player.uuid, k -> new ArrayList<>());
        boolean previouslySneaking = wasSneaking.getOrDefault(player.uuid, false);
        if (player.isSneaking && !previouslySneaking) {
            shiftHistory.add(currentTime);
        }
        wasSneaking.put(player.uuid, player.isSneaking);

        shiftHistory.removeIf(timestamp -> currentTime - timestamp >= SHIFT_WINDOW_MS);

        final boolean isLookingDown = player.pitch >= LOOKING_DOWN_PITCH;
        final boolean isOnGround = player.onGround;
        final boolean isSwingingBlock = player.swingProgress > 0 && player.isHoldingBlock;

        final double horizontalSpeed = Math.sqrt(player.motionX * player.motionX + player.motionZ * player.motionZ);
        final boolean isMovingFast = horizontalSpeed > FAST_SPEED_THRESHOLD;

        double movementAngle = Math.atan2(player.motionZ, player.motionX) * 180 / Math.PI;
        if (movementAngle < 0) movementAngle += 360;

        boolean isMovingStraight = false;
        for (double angle : CARDINAL_ANGLES) {
            if (Math.abs(movementAngle - angle) <= CARDINAL_TOLERANCE || Math.abs(movementAngle - angle - 360) <= CARDINAL_TOLERANCE) {
                isMovingStraight = true;
                break;
            }
        }
        final boolean isMovingDiagonal = !isMovingStraight && horizontalSpeed > DIAGONAL_MIN_SPEED;

        final int shiftCount = shiftHistory.size();
        final boolean hasExcessiveShifts = shiftCount > SHIFT_COUNT_THRESHOLD && horizontalSpeed > EXCESSIVE_SHIFT_SPEED_THRESHOLD;

        final boolean isEagle = isLookingDown && isOnGround && isSwingingBlock
            && isMovingDiagonal && isMovingFast && hasExcessiveShifts;

        if (isEagle) {
            int level = addViolation(player, VIOLATION_WEIGHT);

            if (shouldAlert(player)) {
                flag(player, level);
                markAlert(player);
            }
        } else {
            reduceViolation(player, VIOLATION_WEIGHT);
        }
    }
}