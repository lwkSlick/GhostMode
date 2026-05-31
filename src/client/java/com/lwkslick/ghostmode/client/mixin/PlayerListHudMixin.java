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

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Mixin(PlayerListHud.class)
public abstract class PlayerListHudMixin {

    // ── Hide entire tab list ───────────────────────────────────────────────
    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private void onRender(DrawContext context, int scaledWindowWidth,
                          net.minecraft.scoreboard.Scoreboard scoreboard,
                          net.minecraft.scoreboard.ScoreboardObjective objective,
                          CallbackInfo ci) {
        if (!com.lwkslick.ghostmode.client.GhostModeClient.isPeeking &&
                GhostModeConfig.get().getActiveProfile().hideTabList) ci.cancel();
    }

    // ── Remove own entry from the list before anything renders ────────────
    @Inject(method = "collectPlayerEntries", at = @At("RETURN"), cancellable = true)
    private void filterOwnEntry(CallbackInfoReturnable<Collection<PlayerListEntry>> cir) {
        GhostModeConfig.Profile p = GhostModeConfig.get().getActiveProfile();
        MinecraftClient mc = MinecraftClient.getInstance();
        if (!p.maskOwnTabEntry || mc.player == null) return;

        java.util.UUID myId = mc.player.getGameProfile().id();
        List<PlayerListEntry> filtered = cir.getReturnValue().stream()
                .map(e -> {
                    if (e.getProfile().id().equals(myId)) {
                        e.setDisplayName(Text.literal(p.useAlias ? p.alias : "•••••"));
                    }
                    return e;
                })
                .collect(Collectors.toList());
        cir.setReturnValue(filtered);
    }

    // ── Sentinel + own entry — intercept name at render time ──────────────
    @Inject(method = "getPlayerName", at = @At("RETURN"), cancellable = true)
    private void scrubWatchlistNames(PlayerListEntry entry, CallbackInfoReturnable<Text> cir) {
        GhostModeConfig.Profile p = GhostModeConfig.get().getActiveProfile();
        MinecraftClient mc = MinecraftClient.getInstance();

        // Own entry — force alias here too, in case setDisplayName was ignored
        if (p.maskOwnTabEntry && mc.player != null) {
            if (entry.getProfile().id().equals(mc.player.getGameProfile().id())) {
                cir.setReturnValue(Text.literal(p.useAlias ? p.alias : "•••••"));
                return;
            }
        }

        // Sentinel — scrub watchlist names
        if (!p.sentinelEnabled || !p.sentinelTabList) return;
        String original = cir.getReturnValue().getString();
        if (SentinelManager.sentenceContainsProtected(original)) {
            cir.setReturnValue(Text.literal(SentinelManager.scrub(original)));
        }
    }
}