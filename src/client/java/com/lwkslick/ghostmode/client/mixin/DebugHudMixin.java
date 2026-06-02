package com.lwkslick.ghostmode.client.mixin;

import com.lwkslick.ghostmode.client.config.GhostModeConfig;
import net.minecraft.client.gui.hud.debug.PlayerPositionDebugHudEntry;
import net.minecraft.client.gui.hud.debug.DebugHudLines;
import net.minecraft.world.World;
import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.jetbrains.annotations.Nullable;

@Mixin(PlayerPositionDebugHudEntry.class)
public class DebugHudMixin {

    // Drift state — advances slowly so fake coords look alive
    private static double driftX = 0;
    private static double driftZ = 0;
    private static double velX   = 0.03;
    private static double velZ   = 0.02;
    private static int    tick   = 0;

    @Inject(method = "render(Lnet/minecraft/client/gui/hud/debug/DebugHudLines;Lnet/minecraft/world/World;Lnet/minecraft/world/chunk/WorldChunk;Lnet/minecraft/world/chunk/WorldChunk;)V",
            at = @At("HEAD"), cancellable = true)
    private void ghostmode$scrubCoords(DebugHudLines lines, @Nullable World world,
                                       @Nullable WorldChunk clientChunk, @Nullable WorldChunk chunk, CallbackInfo ci) {
        GhostModeConfig.Profile p = GhostModeConfig.get().getActiveProfile();
        if (com.lwkslick.ghostmode.client.GhostModeClient.isPeeking) return;
        if (!p.hideCoordinates && !p.fakeCoordinates) return;

        ci.cancel();

        if (p.fakeCoordinates) {
            // Drift: slowly oscillate around the configured base coords
            tick++;
            if (tick % 2 == 0) { // update every 2 render ticks (~10 times/sec)
                driftX += velX;
                driftZ += velZ;
                // Reverse direction every ~8 seconds to keep it bounded
                if (Math.abs(driftX) > 15) velX = -velX;
                if (Math.abs(driftZ) > 12) velZ = -velZ;
            }
            int fx = p.fakeX + (int) driftX;
            int fz = p.fakeZ + (int) driftZ;
            lines.addLine("XYZ: " + fx + " / " + p.fakeY + " / " + fz);
            lines.addLine("Block: " + fx + " " + p.fakeY + " " + fz);
        } else {
            lines.addLine("XYZ: [hidden]");
            lines.addLine("Block: [hidden]");
        }
    }
}