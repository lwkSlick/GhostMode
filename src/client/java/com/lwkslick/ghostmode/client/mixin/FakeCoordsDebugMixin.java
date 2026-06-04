package com.lwkslick.ghostmode.client.mixin;

import com.lwkslick.ghostmode.client.GhostModeHud;
import com.lwkslick.ghostmode.client.config.GhostModeConfig;
import net.minecraft.client.gui.hud.debug.DebugHudLines;
import net.minecraft.client.gui.hud.debug.PlayerPositionDebugHudEntry;
import net.minecraft.client.gui.hud.debug.PlayerSectionPositionDebugHudEntry;
import net.minecraft.world.World;
import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({PlayerPositionDebugHudEntry.class, PlayerSectionPositionDebugHudEntry.class})
public class FakeCoordsDebugMixin {

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private void ghostmode$fakeCoords(DebugHudLines lines, World world, WorldChunk clientChunk, WorldChunk chunk, CallbackInfo ci) {
        GhostModeConfig cfg = GhostModeConfig.get();
        if (cfg == null || !cfg.enabled || !cfg.fakeCoords) return;

        int fx = GhostModeHud.getFakeX();
        int fy = GhostModeHud.getFakeY();
        int fz = GhostModeHud.getFakeZ();

        Object self = (Object) this;

        if (self instanceof PlayerPositionDebugHudEntry) {
            // Fake XYZ, Block, and a plausible Facing
            lines.addLine(String.format("XYZ: %d / %d / %d", fx, fy, fz));
            lines.addLine(String.format("Block: %d %d %d", fx, fy, fz));
            lines.addLine("Facing: north (Towards negative Z)");
        } else {
            // PlayerSectionPositionDebugHudEntry — fake chunk coords
            int cx = Math.floorDiv(fx, 16);
            int cy = Math.floorDiv(fy, 16);
            int cz = Math.floorDiv(fz, 16);
            int lx = Math.floorMod(fx, 16);
            int ly = Math.floorMod(fy, 16);
            int lz = Math.floorMod(fz, 16);
            lines.addLine(String.format("Chunk: %d %d %d in %d %d %d", lx, ly, lz, cx, cy, cz));
        }

        ci.cancel();
    }
}