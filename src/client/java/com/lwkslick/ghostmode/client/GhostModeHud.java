package com.lwkslick.ghostmode.client;

import com.lwkslick.ghostmode.client.config.GhostModeConfig;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.font.TextRenderer;

import java.util.Random;

public class GhostModeHud {

    private static final Random RANDOM = new Random();

    // Fake coords state
    private static int fakeX = 0, fakeY = 64, fakeZ = 0;
    private static long lastCoordsUpdate = 0;

    // Action bar yield state
    private static long lastActionBarMessage = 0;
    private static final long ACTION_BAR_YIELD_MS = 4000; // yield for 4 seconds after a real message

    public static int getFakeX() { return fakeX; }
    public static int getFakeY() { return fakeY; }
    public static int getFakeZ() { return fakeZ; }

    public static void tickFakeCoords() {
        GhostModeConfig cfg = GhostModeConfig.get();
        if (cfg == null || !cfg.enabled || !cfg.fakeCoords) return;
        long now = System.currentTimeMillis();
        long intervalMs = cfg.fakeCoordsInterval * 1000L;
        if (now - lastCoordsUpdate >= intervalMs) {
            fakeX = RANDOM.nextInt(2000001) - 1000000;
            fakeY = RANDOM.nextInt(321);
            fakeZ = RANDOM.nextInt(2000001) - 1000000;
            lastCoordsUpdate = now;
        }
    }

    public static void notifyActionBarMessage() {
        lastActionBarMessage = System.currentTimeMillis();
    }

    public static void register() {
        HudRenderCallback.EVENT.register((drawContext, tickDelta) -> {
            GhostModeConfig cfg = GhostModeConfig.get();
            if (cfg == null || !cfg.enabled || !cfg.watermarkEnabled) return;

            MinecraftClient mc = MinecraftClient.getInstance();
            if (mc.player == null || mc.currentScreen != null) return;

            String text = (cfg.watermarkText == null || cfg.watermarkText.isBlank()) ? "YourChannel" : cfg.watermarkText;
            TextRenderer tr = mc.textRenderer;
            int textWidth = tr.getWidth(text);
            int screenW = mc.getWindow().getScaledWidth();
            int screenH = mc.getWindow().getScaledHeight();

            // Build color with opacity
            int opacity = Math.max(0, Math.min(255, cfg.watermarkOpacity));
            int color = (opacity << 24) | (cfg.watermarkColor.getRGB() & 0xFFFFFF);

            int x, y;
            String pos = cfg.watermarkPosition == null ? "ACTION_BAR" : cfg.watermarkPosition;

            switch (pos) {
                case "TOP_LEFT" -> { x = 4; y = 4; }
                case "TOP_RIGHT" -> { x = screenW - textWidth - 4; y = 4; }
                case "BOTTOM_LEFT" -> { x = 4; y = screenH - 20; }
                case "BOTTOM_RIGHT" -> { x = screenW - textWidth - 4; y = screenH - 20; }
                default -> {
                    // ACTION_BAR — yield if a real action bar message came in recently
                    long now = System.currentTimeMillis();
                    if (now - lastActionBarMessage < ACTION_BAR_YIELD_MS) return;
                    x = (screenW - textWidth) / 2;
                    y = screenH - 49;
                }
            }

            drawContext.drawTextWithShadow(tr, text, x, y, color);
        });
    }
}