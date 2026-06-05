package com.lwkslick.ghostmode.client.mixin;

import com.lwkslick.ghostmode.client.config.GhostModeConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerListHud.class)
public class PlayerListHudMixin {

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private void ghostmode$hideTabList(DrawContext context, int scaledWindowWidth,
                                       Scoreboard scoreboard, ScoreboardObjective objective,
                                       CallbackInfo ci) {
        GhostModeConfig cfg = GhostModeConfig.get();
        if (cfg == null || !cfg.enabled) return;
        if (cfg.hideTabList) ci.cancel();
    }

    @Inject(method = "getPlayerName", at = @At("HEAD"), cancellable = true)
    private void ghostmode$countOnly(PlayerListEntry entry, CallbackInfoReturnable<Text> cir) {
        GhostModeConfig cfg = GhostModeConfig.get();
        if (cfg == null || !cfg.enabled || !cfg.tabCountOnly) return;
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null) return;
        if (!entry.getProfile().id().equals(mc.player.getUuid())) {
            cir.setReturnValue(Text.literal(""));
        }
    }

    @Inject(method = "renderLatencyIcon", at = @At("HEAD"))
    private void ghostmode$fakePing(DrawContext context, int width, int x, int y,
                                    PlayerListEntry entry, CallbackInfo ci) {
        GhostModeConfig cfg = GhostModeConfig.get();
        if (cfg == null || !cfg.enabled || cfg.fakePing < 0) return;
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null) return;
        if (!entry.getProfile().id().equals(mc.player.getUuid())) return;
        // Render fake ping icon manually based on fakePing value, then cancel vanilla
        // Vanilla uses: <0 = unknown, 0-149=5bars, 150-299=4bars, 300-599=3bars, 600-999=2bars, 1000+=1bar
        // We just override the entry latency via reflection and let vanilla handle it next frame
        try {
            java.lang.reflect.Field f = PlayerListEntry.class.getDeclaredField("latency");
            f.setAccessible(true);
            f.setInt(entry, cfg.fakePing);
        } catch (Exception ignored) {}
    }
}