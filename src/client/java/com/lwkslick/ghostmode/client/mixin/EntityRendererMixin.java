package com.lwkslick.ghostmode.client.mixin;

import com.lwkslick.ghostmode.client.GhostModeClient;
import com.lwkslick.ghostmode.client.config.GhostModeConfig;
import com.lwkslick.ghostmode.client.sentinel.SentinelManager;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.entity.Entity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin {

    @Inject(method = "updateRenderState", at = @At("RETURN"))
    private void ghostmode$scrubNametag(Entity entity, EntityRenderState state, float tickDelta, CallbackInfo ci) {
        if (GhostModeClient.isPeeking) return;
        GhostModeConfig.Profile p = GhostModeConfig.get().getActiveProfile();
        if (!p.streamModeEnabled || !p.sentinelEnabled || !p.sentinelNametags) return;
        if (state.displayName == null) return;
        String raw = state.displayName.getString();
        if (SentinelManager.sentenceContainsProtected(raw)) {
            state.displayName = Text.literal(SentinelManager.scrub(raw));
        }
    }
}