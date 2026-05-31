package com.lwkslick.ghostmode.client.hud;

import com.lwkslick.ghostmode.client.config.GhostModeConfig;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public class WatermarkHud {

    public static void register() {
        HudRenderCallback.EVENT.register(WatermarkHud::render);
    }

    private static void render(DrawContext context, net.minecraft.client.render.RenderTickCounter tickCounter) {
        GhostModeConfig.Profile p = GhostModeConfig.get().getActiveProfile();
        if (!p.showWatermark) return;
        String text = p.watermarkText;
        if (text == null || text.isBlank()) return;

        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.textRenderer == null) return;

        int screenW = context.getScaledWindowWidth();
        int screenH = context.getScaledWindowHeight();
        int textW   = mc.textRenderer.getWidth(text);
        int margin  = 4;

        int x, y;
        switch (p.watermarkPosition) {
            case "TOP_LEFT"     -> { x = margin;              y = margin; }
            case "BOTTOM_LEFT"  -> { x = margin;              y = screenH - 10 - margin; }
            case "BOTTOM_RIGHT" -> { x = screenW - textW - margin; y = screenH - 10 - margin; }
            default             -> { x = screenW - textW - margin; y = margin; } // TOP_RIGHT
        }

        context.drawTextWithShadow(mc.textRenderer, text, x, y, 0xFFFFFFFF);
    }
}