package com.ottertree.nevada.anticheat.check;

import com.ottertree.nevada.Nevada;
import com.ottertree.nevada.anticheat.ACPlayerData;
import com.ottertree.nevada.util.ChatUtil;

public abstract class Check {
    private final String name;
    private static final String ACPREFIX = "§r§8[§5" + Nevada.NAME + " §cAC§8] §r";

    public Check(String name) {
        this.name = name;
    }

    protected void failCheck(ACPlayerData player) {
        ChatUtil.setActionbar(ACPREFIX + player.displayName + " §cfailed " + name);
    }

    public void runCheck(ACPlayerData player) {}
}
