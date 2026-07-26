package com.ottertree.nevada.anticheat.check;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.ottertree.nevada.anticheat.ACPlayerData;

public class NoSlowCheck extends Check {
    private static final int TICK_THRESHOLD = 5;
    private final Map<UUID, Integer> buffer = new HashMap<>();
    
    public NoSlowCheck() {
        super("NoSlow");
    }

    @Override
    public void runCheck(ACPlayerData player) {
        final boolean isSlowed = player.isUsingItem && (player.isHoldingBow || player.isHoldingConsumable || player.isHoldingSword);

        if (isSlowed && player.isSprinting) {
            int ticks = buffer.merge(player.uuid, 1, Integer::sum);

            if (ticks >= TICK_THRESHOLD) {
                failCheck(player);
                buffer.remove(player.uuid);
            }
        } else {
            buffer.remove(player.uuid);
        }
    }
}
