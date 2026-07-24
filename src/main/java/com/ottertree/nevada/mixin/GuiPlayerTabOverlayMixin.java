package com.ottertree.nevada.mixin;

import java.util.concurrent.CompletableFuture;

import com.mojang.authlib.GameProfile;
import com.ottertree.nevada.Nevada;
import com.ottertree.nevada.api.Hypixel;
import com.ottertree.nevada.data.PlayerProfile;
import com.ottertree.nevada.util.BedwarsUtil;

import cc.polyfrost.oneconfig.utils.hypixel.HypixelUtils;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.network.NetworkPlayerInfo;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GuiPlayerTabOverlay.class)
public abstract class GuiPlayerTabOverlayMixin {
    @Inject(method = "getPlayerName", at = @At("RETURN"), cancellable = true)
    private void onGetPlayerName(NetworkPlayerInfo networkPlayerInfoIn, CallbackInfoReturnable<String> cir) {
        GameProfile nwProfile = networkPlayerInfoIn.getGameProfile();
        if (nwProfile == null || nwProfile.getName() == null || nwProfile.getId() == null)
            return;

        if (!Nevada.config.TabStats_Enable)
            return;

        if (!HypixelUtils.INSTANCE.isHypixel())
            return;

        if (BedwarsUtil.inBedwars()) {
            CompletableFuture<PlayerProfile> future = Hypixel.INSTANCE.getPlayerProfileFromUUID(nwProfile.getId().toString());

            if (future.isDone() && !future.isCompletedExceptionally()) {
                PlayerProfile profile = future.getNow(null);
                if (profile != null) {
                    cir.setReturnValue(BedwarsUtil.formatLevel(profile.bedwars.level) + " " + cir.getReturnValue() + " §7| " + BedwarsUtil.colorFKDR(profile.bedwars.getFKDR()));
                }
            }
        }
    }
}