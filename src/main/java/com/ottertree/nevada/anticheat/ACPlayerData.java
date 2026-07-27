package com.ottertree.nevada.anticheat;

import java.util.UUID;

public class ACPlayerData {
    public String displayName;
    public UUID uuid;

    public boolean isUsingItem;
    public boolean isHoldingBow;
    public boolean isHoldingSword;
    public boolean isHoldingConsumable;

    public boolean isSprinting;

    public boolean isBlocking;
    public long blockingStartTime;
    public float swingProgress;

    public float pitch;
    public boolean onGround;
    public boolean isHoldingBlock;
    public boolean isSneaking;
    public double motionX;
    public double motionZ;
    public double motionY;

    public double posY;

    public boolean hasJumpBoost;
    public int hurtTime;
}