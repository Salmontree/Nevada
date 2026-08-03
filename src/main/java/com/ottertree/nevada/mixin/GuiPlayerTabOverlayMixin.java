package com.ottertree.nevada.mixin;

import java.util.concurrent.CompletableFuture;

import com.mojang.authlib.GameProfile;
import com.ottertree.nevada.Nevada;
import com.ottertree.nevada.api.Hypixel;
import com.ottertree.nevada.cache.PlayerNickCache;
import com.ottertree.nevada.data.PlayerProfile;
import com.ottertree.nevada.util.BedwarsUtil;
import com.ottertree.nevada.util.ChatUtil;
import com.ottertree.nevada.util.DuelsUtil;
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

        if (BedwarsUtil.inBedwars()) {
            if (!Nevada.config.TabStats_Bedwars_Enable || (!Nevada.config.TabStats_Bedwars_EnableInLobby && BedwarsUtil.inLobby())) return;

            CompletableFuture<PlayerProfile> future = Hypixel.INSTANCE.getPlayerProfileFromUUID(nwProfile.getId().toString());

            if (future.isDone() && !future.isCompletedExceptionally()) {
                PlayerProfile profile = future.getNow(null);
                if (profile.hypixel.isNick) {
                    // Skin hasn't been denicked before
                    final boolean firstDenick = PlayerNickCache.INSTANCE.getNick(nwProfile.getName()) == null;

                    String skinDenick = SkinUtil.skinDenick(nwProfile.getName());
                    if (skinDenick != null) {
                        // Player's been successfully denicked
                        CompletableFuture<PlayerProfile> nickFuture = Hypixel.INSTANCE.getPlayerProfileFromName(skinDenick);
                        if (nickFuture.isDone() && !nickFuture.isCompletedExceptionally()) {
                            PlayerProfile denickedProfile = nickFuture.getNow(null);

                            String tablistStatTemp = "";
                            switch (Nevada.config.TabStats_Bedwars_TablistStat) {
                                case 0: tablistStatTemp = denickedProfile.bedwars.displayFinals; break;
                                case 1: tablistStatTemp = denickedProfile.bedwars.displayFKDR; break;
                                case 2: tablistStatTemp = denickedProfile.bedwars.displayWins; break;
                                case 3: tablistStatTemp = denickedProfile.bedwars.displayWLR; break;
                                case 4: tablistStatTemp = denickedProfile.bedwars.displayBeds; break;
                                case 5: tablistStatTemp = denickedProfile.bedwars.displayBBLR; break;
                            }
                            final String tablistStat = tablistStatTemp;

                            if (Nevada.config.TabStats_Bedwars_ShowTags) {
                                TaglistUtil.getFullTablistCompacted(nwProfile.getId().toString()).thenAccept(taglist -> {
                                    if (!taglist.isEmpty()) taglist += " ";

                                    cir.setReturnValue((Nevada.config.TabStats_Bedwars_ShowStars ? (denickedProfile.bedwars.displayLevel + " ") : "") + taglist + cir.getReturnValue() + " §7(" + denickedProfile.hypixel.displayName + "§8)" + (Nevada.config.TabStats_Bedwars_ShowStat ? (" §7| " + tablistStat) : ""));
                                    if (firstDenick)
                                        ChatUtil.send(ChatUtil.PREFIX + cir.getReturnValue() + " §awas denicked as " + denickedProfile.hypixel.displayName);
                                    return;
                                });
                            } else {
                                cir.setReturnValue((Nevada.config.TabStats_Bedwars_ShowStars ? (denickedProfile.bedwars.displayLevel + " ") : "") + cir.getReturnValue() + (Nevada.config.TabStats_Bedwars_ShowStat ? (" §7| " + tablistStat) : ""));
                                if (firstDenick)
                                    ChatUtil.send(ChatUtil.PREFIX + cir.getReturnValue() + " §awas denicked as " + denickedProfile.hypixel.displayName);
                                return;
                            }
                        }
                    }

                    cir.setReturnValue("§5[NICK] " + cir.getReturnValue());
                    return;
                }

                String tablistStatTemp = "";
                switch (Nevada.config.TabStats_Bedwars_TablistStat) {
                    case 0: tablistStatTemp = profile.bedwars.displayFinals; break;
                    case 1: tablistStatTemp = profile.bedwars.displayFKDR; break;
                    case 2: tablistStatTemp = profile.bedwars.displayWins; break;
                    case 3: tablistStatTemp = profile.bedwars.displayWLR; break;
                    case 4: tablistStatTemp = profile.bedwars.displayBeds; break;
                    case 5: tablistStatTemp = profile.bedwars.displayBBLR; break;
                }
                final String tablistStat = tablistStatTemp;

                if (Nevada.config.TabStats_Bedwars_ShowTags) {
                    TaglistUtil.getFullTablistCompacted(nwProfile.getId().toString()).thenAccept(taglist -> {
                        if (!taglist.isEmpty()) taglist += " ";

                        cir.setReturnValue((Nevada.config.TabStats_Bedwars_ShowStars ? (profile.bedwars.displayLevel + " ") : "") + taglist + cir.getReturnValue() + (Nevada.config.TabStats_Bedwars_ShowStat ? (" §7| " + tablistStat) : ""));
                    });
                } else {
                    cir.setReturnValue((Nevada.config.TabStats_Bedwars_ShowStars ? (profile.bedwars.displayLevel + " ") : "") + cir.getReturnValue() + (Nevada.config.TabStats_Bedwars_ShowStat ? (" §7| " + tablistStat) : ""));
                }
            }
        }

        if (DuelsUtil.inDuels()) {
            if (!Nevada.config.TabStats_Duels_Enable || (!Nevada.config.TabStats_Duels_EnableInLobby && DuelsUtil.inLobby())) return;

            CompletableFuture<PlayerProfile> future = Hypixel.INSTANCE.getPlayerProfileFromUUID(nwProfile.getId().toString());

            if (future.isDone() && !future.isCompletedExceptionally()) {
                PlayerProfile profile = future.getNow(null);
                if (profile.hypixel.isNick) {
                    // Skin hasn't been denicked before
                    final boolean firstDenick = PlayerNickCache.INSTANCE.getNick(nwProfile.getName()) == null;

                    String skinDenick = SkinUtil.skinDenick(nwProfile.getName());
                    if (skinDenick != null) {
                        // Player's been successfully denicked
                        CompletableFuture<PlayerProfile> nickFuture = Hypixel.INSTANCE.getPlayerProfileFromName(skinDenick);
                        if (nickFuture.isDone() && !nickFuture.isCompletedExceptionally()) {
                            PlayerProfile denickedProfile = nickFuture.getNow(null);

                            String tablistStatTemp = "";
                            switch (Nevada.config.TabStats_Duels_TablistStat) {
                                case 0: tablistStatTemp = DuelsUtil.colorWins(denickedProfile.duels.overall.wins); break;
                                case 1: tablistStatTemp = DuelsUtil.colorWLR(denickedProfile.duels.overall.getWLR()); break;
                                case 2: tablistStatTemp = DuelsUtil.colorWS(denickedProfile.duels.overall.winstreak); break;
                                case 3: tablistStatTemp = DuelsUtil.colorBWS(denickedProfile.duels.overall.best_winstreak); break;
                            }
                            final String tablistStat = tablistStatTemp;

                            if (Nevada.config.TabStats_Duels_ShowTags) {
                                TaglistUtil.getFullTablistCompacted(nwProfile.getId().toString()).thenAccept(taglist -> {
                                    if (!taglist.isEmpty()) taglist += " ";

                                    cir.setReturnValue(taglist + cir.getReturnValue() + " §7(" + denickedProfile.hypixel.displayName + "§8)" + (Nevada.config.TabStats_Duels_ShowStat ? (" §7| " + tablistStat) : ""));
                                });
                            } else {
                                cir.setReturnValue(cir.getReturnValue() + (Nevada.config.TabStats_Duels_ShowStat ? (" §7| " + tablistStat) : ""));
                            }

                            if (firstDenick)
                                ChatUtil.send(ChatUtil.PREFIX + cir.getReturnValue() + " §awas denicked as " + denickedProfile.hypixel.displayName);

                            return;
                        }
                    }

                    cir.setReturnValue("§5[NICK] " + cir.getReturnValue());
                    return;
                }

                String tablistStatTemp = "";
                switch (Nevada.config.TabStats_Duels_TablistStat) {
                    case 0: tablistStatTemp = DuelsUtil.colorWins(profile.duels.overall.wins); break;
                    case 1: tablistStatTemp = DuelsUtil.colorWLR(profile.duels.overall.getWLR()); break;
                    case 2: tablistStatTemp = DuelsUtil.colorWS(profile.duels.overall.winstreak); break;
                    case 3: tablistStatTemp = DuelsUtil.colorBWS(profile.duels.overall.best_winstreak); break;
                }
                final String tablistStat = tablistStatTemp;

                if (Nevada.config.TabStats_Duels_ShowTags) {
                    TaglistUtil.getFullTablistCompacted(nwProfile.getId().toString()).thenAccept(taglist -> {
                        if (!taglist.isEmpty()) taglist += " ";

                        cir.setReturnValue(taglist + cir.getReturnValue() + (Nevada.config.TabStats_Duels_ShowStat ? (" §7| " + tablistStat) : ""));
                    });
                } else {
                    cir.setReturnValue(cir.getReturnValue() + (Nevada.config.TabStats_Duels_ShowStat ? (" §7| " + tablistStat) : ""));
                }
            }
        }
    }
}