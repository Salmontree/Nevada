package com.ottertree.nevada;

import net.minecraft.client.Minecraft;

public class Async {
    public static void run(Runnable task) {
        new Thread(task).start();
    }

    public static void runOnMainThread(Runnable task) {
        Minecraft.getMinecraft().addScheduledTask(task);
    }
}