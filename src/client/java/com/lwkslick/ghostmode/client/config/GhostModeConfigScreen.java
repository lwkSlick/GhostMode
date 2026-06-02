package com.lwkslick.ghostmode.client.config;

import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class GhostModeConfigScreen {

    public static Screen create(Screen parent) {
        GhostModeConfig cfg = GhostModeConfig.get();

        return YetAnotherConfigLib.createBuilder()
                .title(Text.literal("GhostMode"))
                .category(ConfigCategory.createBuilder()
                        .name(Text.literal("General"))
                        .option(Option.<Boolean>createBuilder()
                                .name(Text.literal("Enable GhostMode"))
                                .description(OptionDescription.of(Text.literal("Master on/off switch. Disables all features when off.")))
                                .binding(true, () -> cfg.enabled, val -> { cfg.enabled = val; cfg.save(); })
                                .controller(TickBoxControllerBuilder::create)
                                .build())
                        .option(Option.<Boolean>createBuilder()
                                .name(Text.literal("Hide Server IP"))
                                .description(OptionDescription.of(Text.literal("Masks the IP field in Add Server and Direct Connect screens.")))
                                .binding(true, () -> cfg.hideServerIp, val -> { cfg.hideServerIp = val; cfg.save(); })
                                .controller(TickBoxControllerBuilder::create)
                                .build())
                        .option(Option.<Boolean>createBuilder()
                                .name(Text.literal("Hide Direct Connect IP"))
                                .description(OptionDescription.of(Text.literal("Masks the IP field in the Direct Connect screen.")))
                                .binding(true, () -> cfg.hideDirectConnect, val -> { cfg.hideDirectConnect = val; cfg.save(); })
                                .controller(TickBoxControllerBuilder::create)
                                .build())
                        .build())
                .save(cfg::save)
                .build()
                .generateScreen(parent);
    }
}