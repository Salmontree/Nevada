package com.ottertree.nevada.mixin;

import com.mojang.authlib.GameProfile;
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
        GameProfile profile = networkPlayerInfoIn.getGameProfile();
        if (profile == null || profile.getName() == null) {
            return;
        }

        cir.setReturnValue("testing");
    }
}