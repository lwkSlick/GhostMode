package com.lwkslick.ghostmode.client.mixin;

import com.lwkslick.ghostmode.client.GhostModeClient;
import com.lwkslick.ghostmode.client.config.GhostModeConfig;
import net.minecraft.client.toast.AdvancementToast;
import net.minecraft.client.toast.Toast;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AdvancementToast.class)
public class AdvancementWidgetMixin {

    @Inject(method = "getVisibility", at = @At("HEAD"), cancellable = true)
    private void ghostmode$hideAdvancement(CallbackInfoReturnable<Toast.Visibility> cir) {
        if (GhostModeClient.isPeeking) return;
        GhostModeConfig.Profile p = GhostModeConfig.get().getActiveProfile();
        if (!p.streamModeEnabled || !p.sentinelEnabled || !p.sentinelAdvancements) return;
        cir.setReturnValue(Toast.Visibility.HIDE);
    }
}