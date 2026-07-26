package com.ottertree.nevada.mixin;

import java.util.concurrent.CompletableFuture;

import com.mojang.authlib.GameProfile;
import com.ottertree.nevada.Nevada;
import com.ottertree.nevada.api.Hypixel;
import com.ottertree.nevada.data.PlayerProfile;
import com.ottertree.nevada.util.BedwarsUtil;
import com.ottertree.nevada.util.SkinUtil;
import com.ottertree.nevada.util.TaglistUtil;

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

        if (nwProfile == null || nwProfile.getName() == null || nwProfile.getId() == null) return;
        if (!HypixelUtils.INSTANCE.isHypixel()) return;
        if (!Nevada.config.TabStats_Enable) return;

        if (BedwarsUtil.inBedwars()) {
            if (!Nevada.config.TabStats_EnableInLobby && BedwarsUtil.inLobby()) return;

            CompletableFuture<PlayerProfile> future = Hypixel.INSTANCE.getPlayerProfileFromUUID(nwProfile.getId().toString());

            if (future.isDone() && !future.isCompletedExceptionally()) {
                PlayerProfile profile = future.getNow(null);
                if (profile.hypixel.isNick) {
                    String skinDenick = SkinUtil.skinDenick(nwProfile.getName());
                    if (skinDenick != null) {
                        // Player's been successfully denicked
                        CompletableFuture<PlayerProfile> nickFuture = Hypixel.INSTANCE.getPlayerProfileFromName(skinDenick);
                        if (nickFuture.isDone() && !nickFuture.isCompletedExceptionally()) {
                            PlayerProfile denickedProfile = nickFuture.getNow(null);
                            cir.setReturnValue("§5[NICK] " + cir.getReturnValue() + " §8(" + denickedProfile.hypixel.displayName + "§8)");
                            return;
                        }
                    }

                    cir.setReturnValue("§5[NICK] " + cir.getReturnValue());
                    return;
                }

                String tablistStatTemp = "";
                switch (Nevada.config.TabStats_TablistStat) {
                    case 0: tablistStatTemp = BedwarsUtil.colorFinals(profile.bedwars.finalKills); break;
                    case 1: tablistStatTemp = BedwarsUtil.colorFKDR(profile.bedwars.getFKDR()); break;
                    case 2: tablistStatTemp = BedwarsUtil.colorWins(profile.bedwars.wins); break;
                    case 3: tablistStatTemp = BedwarsUtil.colorWLR(profile.bedwars.getWLR()); break;
                    case 4: tablistStatTemp = BedwarsUtil.colorBedsBroken(profile.bedwars.bedsBroken); break;
                    case 5: tablistStatTemp = BedwarsUtil.colorBBLR(profile.bedwars.getBBLR()); break;
                }
                final String tablistStat = tablistStatTemp;

                if (Nevada.config.TabStats_ShowTags) {
                    TaglistUtil.getFullTablistCompacted(nwProfile.getId().toString()).thenAccept(taglist -> {
                        if (!taglist.isEmpty()) taglist += " ";

                        cir.setReturnValue((Nevada.config.TabStats_ShowStars ? (BedwarsUtil.formatLevel(profile.bedwars.level) + " ") : "") + taglist + cir.getReturnValue() + (Nevada.config.TabStats_ShowStat ? (" §7| " + tablistStat) : ""));
                    });
                } else {
                    cir.setReturnValue((Nevada.config.TabStats_ShowStars ? (BedwarsUtil.formatLevel(profile.bedwars.level) + " ") : "") + cir.getReturnValue() + (Nevada.config.TabStats_ShowStat ? (" §7| " + tablistStat) : ""));
                }
            }
        }
    }
}