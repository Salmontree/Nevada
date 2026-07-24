package com.ottertree.nevada.mixin;

import com.mojang.authlib.GameProfile;
import com.ottertree.nevada.config.NevadaConfig;
import com.ottertree.nevada.stats.PlayerProfile;
import com.ottertree.nevada.stats.backends.Hypixel;
import com.ottertree.nevada.util.BedwarsUtil;

import cc.polyfrost.oneconfig.utils.hypixel.HypixelUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.network.NetworkPlayerInfo;

import java.io.IOException;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GuiPlayerTabOverlay.class)
public abstract class GuiPlayerTabOverlayMixin {
    @Inject(method = "getPlayerName", at = @At("RETURN"), cancellable = true)
    private void onGetPlayerName(NetworkPlayerInfo networkPlayerInfoIn, CallbackInfoReturnable<String> cir) {
        GameProfile profile = networkPlayerInfoIn.getGameProfile();
        if (profile == null || profile.getName() == null)
            return;

        if (!NevadaConfig.TabStats_Enable)
            return;

        if (!HypixelUtils.INSTANCE.isHypixel())
            return;

        if (BedwarsUtil.inBedwars()) {


            Hypixel.getPlayerProfileAsync(profile.getName()).thenAcceptAsync(stats -> {
                float fkdr = (float)stats.bedwarsFinalKills / stats.bedwarsFinalDeaths;
                cir.setReturnValue(stats.bedwarsLevelFormatted + " " + cir.getReturnValue() + "§7 | " + BedwarsUtil.colorFKDR(fkdr) + String.format("%.2f", fkdr));
            }, runnable -> Minecraft.getMinecraft().addScheduledTask(runnable));
        }
    }
}