package com.ottertree.nevada.anticheat.check;

import com.ottertree.nevada.Nevada;
import com.ottertree.nevada.anticheat.ACPlayerData;

public class ScaffoldCheck extends Check {
    private static final double DEAD_Y_THRESHOLD = 100.0;
    private static final double LOOKING_DOWN_PITCH = 25.0;
    private static final double FAST_SPEED_THRESHOLD = 5.0;
    private static final double FLAT_VERTICAL_TOLERANCE = 0.1;

    public ScaffoldCheck() {
        super("Scaffold");
    }

    @Override
    public void runCheck(ACPlayerData player) {
        if (!Nevada.config.Anticheat_Scaffold) return;

        final double horizontalSpeed = Math.sqrt(player.motionX * player.motionX + player.motionZ * player.motionZ);

        final boolean isLikelyDead = player.posY > DEAD_Y_THRESHOLD;
        if (isLikelyDead) {
            reduceViolation(player, 1);
            return;
        }

        final boolean isLookingDown = player.pitch >= LOOKING_DOWN_PITCH;
        final boolean isPlacingBlocks = player.swingProgress > 0 && player.isHoldingBlock;
        final boolean isMovingFast = horizontalSpeed > FAST_SPEED_THRESHOLD;
        final boolean isNotSneaking = !player.isSneaking;
        final boolean isFlat = Math.abs(player.motionY) < FLAT_VERTICAL_TOLERANCE;

        final boolean isScaffold = isLookingDown && isPlacingBlocks && isMovingFast && isNotSneaking && isFlat;

        if (isScaffold) {
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