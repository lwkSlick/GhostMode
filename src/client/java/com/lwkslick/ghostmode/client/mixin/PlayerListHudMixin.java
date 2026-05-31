package com.lwkslick.ghostmode.client.mixin;

import com.lwkslick.ghostmode.client.config.GhostModeConfig;
import com.lwkslick.ghostmode.client.sentinel.SentinelManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerListHud.class)
public class PlayerListHudMixin {

    // Hide tab list entirely
    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private void onRender(DrawContext context, int scaledWindowWidth,
                          net.minecraft.scoreboard.Scoreboard scoreboard,
                          net.minecraft.scoreboard.ScoreboardObjective objective,
                          CallbackInfo ci) {
        GhostModeConfig.Profile p = GhostModeConfig.get().getActiveProfile();
        if (p.hideTabList) {
            ci.cancel();
        }
    }

    // Mask player name in tab list
    @Inject(method = "getPlayerName", at = @At("RETURN"), cancellable = true)
    private void maskPlayerName(PlayerListEntry entry, CallbackInfoReturnable<Text> cir) {
        GhostModeConfig.Profile p = GhostModeConfig.get().getActiveProfile();
        if (!p.sentinelEnabled || !p.sentinelTabList) return;

        String original = cir.getReturnValue().getString();

        // Mask own entry with alias
        MinecraftClient mc = MinecraftClient.getInstance();
        if (p.maskOwnTabEntry && mc.player != null) {
            String ign = mc.player.getName().getString();
            if (original.equalsIgnoreCase(ign)) {
                cir.setReturnValue(Text.literal(p.useAlias ? p.alias : "•••••"));
                return;
            }
        }

        // Scrub watchlist names
        if (SentinelManager.sentenceContainsProtected(original)) {
            cir.setReturnValue(Text.literal(SentinelManager.scrub(original)));
        }
    }
}