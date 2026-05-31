package com.lwkslick.ghostmode.client.hud;

import com.lwkslick.ghostmode.client.GhostModeClient;
import com.lwkslick.ghostmode.client.config.GhostModeConfig;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
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
        int alpha = (int)(p.manualBlurOpacity * 255) << 24;
        context.fill(0, 0, w, h, alpha | 0x00000000);
    }
}