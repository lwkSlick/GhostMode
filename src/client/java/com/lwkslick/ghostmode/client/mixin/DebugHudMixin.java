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

    @Inject(method = "render(Lnet/minecraft/client/gui/hud/debug/DebugHudLines;Lnet/minecraft/world/World;Lnet/minecraft/world/chunk/WorldChunk;Lnet/minecraft/world/chunk/WorldChunk;)V", at = @At("HEAD"), cancellable = true)
    private void ghostmode$scrubCoords(DebugHudLines lines, @Nullable World world,
                                       @Nullable WorldChunk clientChunk, @Nullable WorldChunk chunk, CallbackInfo ci) {
        GhostModeConfig.Profile p = GhostModeConfig.get().getActiveProfile();
        if (!p.hideCoordinates && !p.fakeCoordinates) return;

        ci.cancel();

        if (p.fakeCoordinates) {
            lines.addLine("XYZ: " + p.fakeX + " / " + p.fakeY + " / " + p.fakeZ);
            lines.addLine("Block: " + p.fakeX + " " + p.fakeY + " " + p.fakeZ);
        } else {
            lines.addLine("XYZ: [hidden]");
            lines.addLine("Block: [hidden]");
        }
    }
}