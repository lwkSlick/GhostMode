package com.lwkslick.ghostmode.client.config;

import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import dev.isxander.yacl3.api.controller.StringControllerBuilder;

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
                        .option(Option.<Boolean>createBuilder()
                                .name(Text.literal("Hide Username"))
                                .description(OptionDescription.of(Text.literal("Replaces your real username with an alias everywhere on screen.")))
                                .binding(true, () -> cfg.hideUsername, val -> { cfg.hideUsername = val; cfg.save(); })
                                .controller(TickBoxControllerBuilder::create)
                                .build())
                        .option(Option.<String>createBuilder()
                                .name(Text.literal("Username Alias"))
                                .description(OptionDescription.of(Text.literal("The name shown in place of your real username.")))
                                .binding("Streamer", () -> cfg.usernameAlias, val -> { cfg.usernameAlias = val; cfg.save(); })
                                .controller(StringControllerBuilder::create)
                                .build())
                        .build())
                .save(cfg::save)
                .build()
                .generateScreen(parent);
    }
}