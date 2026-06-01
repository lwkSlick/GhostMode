package com.lwkslick.ghostmode.client.mixin;

import com.lwkslick.ghostmode.client.GhostModeClient;
import com.lwkslick.ghostmode.client.config.GhostModeConfig;
import com.lwkslick.ghostmode.client.sentinel.SentinelManager;
import net.minecraft.client.gui.screen.DeathScreen;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(DeathScreen.class)
public class DeathScreenMixin {

    @ModifyVariable(method = "<init>", at = @At("HEAD"), argsOnly = true, index = 1)
    private static Text ghostmode$scrubDeathMessage(Text message) {
        GhostModeConfig.Profile p = GhostModeConfig.get().getActiveProfile();
        if (!p.streamModeEnabled || !p.sentinelEnabled || !p.sentinelDeathScreen) return message;
        if (GhostModeClient.isPeeking) return message;
        String raw = message.getString();
        if (SentinelManager.sentenceContainsProtected(raw)) {
            return Text.literal(SentinelManager.scrub(raw));
        }
        return message;
    }
}