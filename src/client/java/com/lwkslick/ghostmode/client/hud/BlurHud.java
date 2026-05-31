package com.lwkslick.ghostmode.client.hud;

import com.lwkslick.ghostmode.client.GhostModeClient;
import com.lwkslick.ghostmode.client.config.GhostModeConfig;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public class BlurHud {

    public static void register() {
        HudRenderCallback.EVENT.register(BlurHud::render);
    }

    private static void render(DrawContext context, net.minecraft.client.render.RenderTickCounter tickCounter) {
        if (!GhostModeClient.isBlurring) return;

        GhostModeConfig.Profile p = GhostModeConfig.get().getActiveProfile();
        int w = context.getScaledWindowWidth();
        int h = context.getScaledWindowHeight();

        switch (p.blurStyle) {
            case "PIXELATE" -> renderPixelate(context, w, h, p.manualBlurOpacity);
            case "BLACKBAR" -> renderBlackbar(context, w, h, p.manualBlurOpacity);
            default         -> renderSolid(context, w, h, p.manualBlurOpacity);   // ALIAS / fallback
        }
    }

    // ── Solid black fill (original behaviour) ─────────────────────────────
    private static void renderSolid(DrawContext context, int w, int h, float opacity) {
        int alpha = (int)(opacity * 255) << 24;
        context.fill(0, 0, w, h, alpha | 0x000000);
    }

    // ── Pixelate: draw a grid of large solid tiles ─────────────────────────
    // True GPU blur isn't available without a shader, so we fake it with a
    // semi-transparent black grid that obscures fine detail while still
    // letting the viewer see rough shapes.
    private static void renderPixelate(DrawContext context, int w, int h, float opacity) {
        int tileSize = 12; // pixels per "block" — bigger = more obscured
        int alpha = (int)(opacity * 200) << 24; // slightly less than full so tiles are visible
        // Full dark wash first
        context.fill(0, 0, w, h, (int)(opacity * 180) << 24 | 0x000000);
        // Grid lines to give the pixelated look
        for (int x = 0; x < w; x += tileSize) {
            for (int y = 0; y < h; y += tileSize) {
                context.fill(x, y, x + tileSize - 1, y + tileSize - 1, alpha | 0x101010);
            }
        }
    }

    // ── Black bars: letterbox top + bottom, plus side bars ────────────────
    private static void renderBlackbar(DrawContext context, int w, int h, float opacity) {
        int alpha = (int)(opacity * 255) << 24 | 0x000000;
        int barH = h / 5;   // top and bottom bars ~20% each
        int barW = w / 8;   // left and right bars ~12% each
        context.fill(0,       0,     w,          barH,      alpha); // top
        context.fill(0,       h - barH, w,     h,           alpha); // bottom
        context.fill(0,       barH,   barW,      h - barH,  alpha); // left
        context.fill(w - barW, barH, w,          h - barH,  alpha); // right
    }
}