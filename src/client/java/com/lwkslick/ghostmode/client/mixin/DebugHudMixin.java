package com.lwkslick.ghostmode.client.mixin;

import com.lwkslick.ghostmode.client.config.GhostModeConfig;
import net.minecraft.client.gui.hud.debug.*;
import net.minecraft.world.World;
import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({
        PlayerPositionDebugHudEntry.class,
        PlayerSectionPositionDebugHudEntry.class,
        ChunkRenderStatsDebugHudEntry.class,
        BiomeDebugHudEntry.class,
        LookingAtBlockDebugHudEntry.class,
        LookingAtEntityDebugHudEntry.class,
        LookingAtFluidDebugHudEntry.class,
        MemoryDebugHudEntry.class,
        SystemSpecsDebugHudEntry.class
})
public abstract class DebugHudMixin {

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private void ghostmode$suppress(DebugHudLines lines, World world, WorldChunk clientChunk, WorldChunk chunk, CallbackInfo ci) {
        GhostModeConfig cfg = GhostModeConfig.get();
        if (!cfg.enabled || !cfg.hideF3) return;

        Object self = (Object) this;

        if (cfg.hideF3Location && (
                self instanceof PlayerPositionDebugHudEntry ||
                        self instanceof PlayerSectionPositionDebugHudEntry ||
                        self instanceof ChunkRenderStatsDebugHudEntry)) {
            ci.cancel(); return;
        }
        if (cfg.hideF3Biome && self instanceof BiomeDebugHudEntry) {
            ci.cancel(); return;
        }
        if (cfg.hideF3LookingAt && (
                self instanceof LookingAtBlockDebugHudEntry ||
                        self instanceof LookingAtEntityDebugHudEntry ||
                        self instanceof LookingAtFluidDebugHudEntry)) {
            ci.cancel(); return;
        }
        if (cfg.hideF3Memory && self instanceof MemoryDebugHudEntry) {
            ci.cancel(); return;
        }
        if (cfg.hideF3SystemSpecs && self instanceof SystemSpecsDebugHudEntry) {
            ci.cancel(); return;
        }
    }
}