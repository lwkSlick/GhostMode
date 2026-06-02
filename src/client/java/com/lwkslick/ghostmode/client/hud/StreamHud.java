package com.lwkslick.ghostmode.client.hud;

import com.lwkslick.ghostmode.client.GhostModeClient;
import com.lwkslick.ghostmode.client.config.GhostModeConfig;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public class StreamHud {

    public static void register() {
        HudRenderCallback.EVENT.register(StreamHud::render);
    }

    private static void render(DrawContext context, net.minecraft.client.render.RenderTickCounter tickCounter) {
        GhostModeConfig.Profile p = GhostModeConfig.get().getActiveProfile();

        // ── Stream mode indicator dot ──────────────────────────────────────
        if (p.streamModeEnabled && p.showStreamDot) {
            int x = context.getScaledWindowWidth() - 8;
            int y = 4;
            // Red filled circle approximated as a 5x5 dot
            context.fill(x - 2, y,     x + 3, y + 1, 0xFFFF3333); // top
            context.fill(x - 3, y + 1, x + 4, y + 4, 0xFFFF3333); // middle
            context.fill(x - 2, y + 4, x + 3, y + 5, 0xFFFF3333); // bottom
        }

        // ── Panic flash overlay ────────────────────────────────────────────
        if (GhostModeClient.panicFlashTicks > 0) {
            float progress = GhostModeClient.panicFlashTicks / 8f; // 1.0 → 0.0
            int alpha = (int)(progress * 180) << 24;
            boolean streamOn = p.streamModeEnabled;
            // Green flash = stream mode enabled, red flash = stream mode disabled
            int color = streamOn ? (alpha | 0x00AA44) : (alpha | 0xFF4444);
            context.fill(0, 0, context.getScaledWindowWidth(), context.getScaledWindowHeight(), color);

            // Show action bar text on panic
            MinecraftClient mc = MinecraftClient.getInstance();
            if (mc.player != null && GhostModeClient.panicFlashTicks == 7) {
                String msg = streamOn ? "§a§lSTREAM MODE ON" : "§c§lSTREAM MODE OFF";
                mc.player.sendMessage(net.minecraft.text.Text.literal(msg), true);
            }
        }
    }
}