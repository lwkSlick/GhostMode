package com.lwkslick.ghostmode.client.mixin;

import com.lwkslick.ghostmode.client.GhostModeClient;
import com.lwkslick.ghostmode.client.config.GhostModeConfig;
import com.lwkslick.ghostmode.client.sentinel.SentinelManager;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin {

    @ModifyVariable(method = "renderLabelIfPresent", at = @At("HEAD"), argsOnly = true, index = 2)
    private Text ghostmode$scrubNametag(Text label, Entity entity) {
        if (GhostModeClient.isPeeking) return label;
        GhostModeConfig.Profile p = GhostModeConfig.get().getActiveProfile();
        if (!p.streamModeEnabled || !p.sentinelEnabled || !p.sentinelNametags) return label;
        String raw = label.getString();
        if (SentinelManager.sentenceContainsProtected(raw)) {
            return Text.literal(SentinelManager.scrub(raw));
        }
        return label;
    }
}