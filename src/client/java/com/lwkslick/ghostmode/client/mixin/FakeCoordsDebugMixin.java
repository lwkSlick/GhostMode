package com.lwkslick.ghostmode.client.mixin;

import com.lwkslick.ghostmode.client.GhostModeHud;
import com.lwkslick.ghostmode.client.config.GhostModeConfig;
import net.minecraft.client.gui.hud.debug.DebugHudLines;
import net.minecraft.client.gui.hud.debug.PlayerPositionDebugHudEntry;
import net.minecraft.world.World;
import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerPositionDebugHudEntry.class)
public class FakeCoordsDebugMixin {

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private void ghostmode$fakeCoords(DebugHudLines lines, World world, WorldChunk clientChunk, WorldChunk chunk, CallbackInfo ci) {
        GhostModeConfig cfg = GhostModeConfig.get();
        if (cfg == null || !cfg.enabled || !cfg.fakeCoords) return;
        lines.addLine(String.format("XYZ: %d / %d / %d",
                GhostModeHud.getFakeX(),
                GhostModeHud.getFakeY(),
                GhostModeHud.getFakeZ()));
        ci.cancel();
    }
}