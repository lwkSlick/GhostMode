package com.lwkslick.ghostmode.client.config;

import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.List;
import java.util.stream.Collectors;

public class GhostModeConfigScreen {

    public static Screen create(Screen parent) {
        GhostModeConfig cfg = GhostModeConfig.get();
        GhostModeConfig.Profile p = cfg.getActiveProfile();

        return YetAnotherConfigLib.createBuilder()
                .title(Text.literal("GhostMode"))

                // ══════════════════════════════════════════════════════════
                // TAB 1 — STREAM MODE
                // ══════════════════════════════════════════════════════════
                .category(ConfigCategory.createBuilder()
                        .name(Text.literal("▶ Stream Mode"))
                        .tooltip(Text.literal("The master switch. Turn this on before you go live."))

                        .option(Option.<Boolean>createBuilder()
                                .name(Text.literal("⚡  STREAM MODE  ⚡"))
                                .description(OptionDescription.of(Text.literal("Master privacy switch. When OFF, GhostMode does nothing. Turn ON before streaming.\n\nPanic shortcut: press K in-game to enable everything instantly.")))
                                .binding(false, () -> p.streamModeEnabled, v -> { p.streamModeEnabled = v; cfg.save(); })
                                .controller(TickBoxControllerBuilder::create)
                                .build())

                        .option(Option.<Boolean>createBuilder()
                                .name(Text.literal("Auto-Enable on Public Servers"))
                                .description(OptionDescription.of(Text.literal("Automatically turns Stream Mode on when you join any public server (not LAN/localhost).")))
                                .binding(false, () -> p.autoEnablePublic, v -> { p.autoEnablePublic = v; cfg.save(); })
                                .controller(TickBoxControllerBuilder::create)
                                .build())

                        .option(Option.<Boolean>createBuilder()
                                .name(Text.literal("Peek Mode (Hold to Reveal)"))
                                .description(OptionDescription.of(Text.literal("When on, you hold a key combo to temporarily see real names/coords.")))
                                .binding(true, () -> p.peekMode, v -> { p.peekMode = v; cfg.save(); })
                                .controller(TickBoxControllerBuilder::create)
                                .build())

                        .build())

                // ══════════════════════════════════════════════════════════
                // TAB 2 — PROTECTION
                // ══════════════════════════════════════════════════════════
                .category(ConfigCategory.createBuilder()
                        .name(Text.literal("Protection"))
                        .tooltip(Text.literal("Everything that hides or replaces sensitive info. Requires Stream Mode ON."))

                        .group(OptionGroup.createBuilder()
                                .name(Text.literal("Sentinel — Auto Name Detection"))
                                .description(OptionDescription.of(Text.literal("Watches for your IGN and watchlist names, hides or replaces them everywhere.")))

                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.literal("Enable Sentinel"))
                                        .binding(true, () -> p.sentinelEnabled, v -> { p.sentinelEnabled = v; cfg.save(); })
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())

                                .option(Option.<Integer>createBuilder()
                                        .name(Text.literal("Match Sensitivity"))
                                        .description(OptionDescription.of(Text.literal("1 = Strict (exact only)  2 = Normal (recommended)  3 = Loose (catches typos)")))
                                        .binding(2, () -> p.sentinelSensitivity, v -> { p.sentinelSensitivity = v; cfg.save(); })
                                        .controller(opt -> IntegerSliderControllerBuilder.create(opt).range(1, 3).step(1))
                                        .build())

                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.literal("Cover Chat"))
                                        .binding(true, () -> p.sentinelChat, v -> { p.sentinelChat = v; cfg.save(); })
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())

                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.literal("Cover Tab List"))
                                        .binding(true, () -> p.sentinelTabList, v -> { p.sentinelTabList = v; cfg.save(); })
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())

                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.literal("Cover Scoreboard"))
                                        .binding(true, () -> p.sentinelScoreboard, v -> { p.sentinelScoreboard = v; cfg.save(); })
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())

                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.literal("Cover Nametags"))
                                        .binding(true, () -> p.sentinelNametags, v -> { p.sentinelNametags = v; cfg.save(); })
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())

                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.literal("Cover F3 Screen"))
                                        .binding(true, () -> p.sentinelF3, v -> { p.sentinelF3 = v; cfg.save(); })
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())

                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.literal("Cover Advancements"))
                                        .binding(true, () -> p.sentinelAdvancements, v -> { p.sentinelAdvancements = v; cfg.save(); })
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())

                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.literal("Cover Death Screen"))
                                        .binding(true, () -> p.sentinelDeathScreen, v -> { p.sentinelDeathScreen = v; cfg.save(); })
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())

                                .build())

                        .group(OptionGroup.createBuilder()
                                .name(Text.literal("Tab List"))

                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.literal("Mask Own Tab Entry"))
                                        .description(OptionDescription.of(Text.literal("Replaces YOUR entry in the tab list with your alias.")))
                                        .binding(true, () -> p.maskOwnTabEntry, v -> { p.maskOwnTabEntry = v; cfg.save(); })
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())

                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.literal("Hide Tab List Entirely"))
                                        .description(OptionDescription.of(Text.literal("Nothing shows when you hold Tab.")))
                                        .binding(false, () -> p.hideTabList, v -> { p.hideTabList = v; cfg.save(); })
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())

                                .build())

                        .group(OptionGroup.createBuilder()
                                .name(Text.literal("Scoreboard"))

                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.literal("Hide Scoreboard Entirely"))
                                        .binding(false, () -> p.hideScoreboard, v -> { p.hideScoreboard = v; cfg.save(); })
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())

                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.literal("Mask Names on Scoreboard"))
                                        .description(OptionDescription.of(Text.literal("Replaces Sentinel-matched names on the scoreboard with aliases.")))
                                        .binding(true, () -> p.maskScoreboardNames, v -> { p.maskScoreboardNames = v; cfg.save(); })
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())

                                .build())

                        .group(OptionGroup.createBuilder()
                                .name(Text.literal("Chat"))

                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.literal("Suppress Incoming DMs"))
                                        .description(OptionDescription.of(Text.literal("Silently drops direct messages and whispers.")))
                                        .binding(false, () -> p.suppressDMs, v -> { p.suppressDMs = v; cfg.save(); })
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())

                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.literal("Remove Mention Highlight"))
                                        .description(OptionDescription.of(Text.literal("Prevents your name flashing when someone mentions you in chat.")))
                                        .binding(true, () -> p.removeMentionPing, v -> { p.removeMentionPing = v; cfg.save(); })
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())

                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.literal("Disable Chat Log to Disk"))
                                        .binding(false, () -> p.disableChatLog, v -> { p.disableChatLog = v; cfg.save(); })
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())

                                .build())

                        .group(OptionGroup.createBuilder()
                                .name(Text.literal("Screenshots"))

                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.literal("Screenshot Guard"))
                                        .description(OptionDescription.of(Text.literal("Post-processes F2 screenshots to blur Sentinel-matched names before saving.")))
                                        .binding(true, () -> p.screenshotGuard, v -> { p.screenshotGuard = v; cfg.save(); })
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())

                                .build())

                        .build())

                // ══════════════════════════════════════════════════════════
                // TAB 3 — IDENTITY & NETWORK
                // ══════════════════════════════════════════════════════════
                .category(ConfigCategory.createBuilder()
                        .name(Text.literal("Identity & Network"))
                        .tooltip(Text.literal("Your alias, watermark, coordinates, and server IP masking."))

                        .group(OptionGroup.createBuilder()
                                .name(Text.literal("Your Identity"))

                                .option(Option.<String>createBuilder()
                                        .name(Text.literal("Alias (Your Stream Name)"))
                                        .description(OptionDescription.of(Text.literal("Replaces your real IGN everywhere when Stream Mode is on.")))
                                        .binding("Player", () -> p.alias, v -> { p.alias = v.isBlank() ? "Player" : v; cfg.save(); })
                                        .controller(StringControllerBuilder::create)
                                        .build())

                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.literal("Use Alias (Replace Name)"))
                                        .binding(true, () -> p.useAlias, v -> { p.useAlias = v; cfg.save(); })
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())

                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.literal("Hide Own Nametag"))
                                        .description(OptionDescription.of(Text.literal("Hides your nametag in F5 third-person view.")))
                                        .binding(false, () -> p.hideOwnNametag, v -> { p.hideOwnNametag = v; cfg.save(); })
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())

                                .build())

                        .group(OptionGroup.createBuilder()
                                .name(Text.literal("Watermark"))

                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.literal("Show Watermark"))
                                        .description(OptionDescription.of(Text.literal("Overlays your channel name in a corner of the screen.")))
                                        .binding(false, () -> p.showWatermark, v -> { p.showWatermark = v; cfg.save(); })
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())

                                .option(Option.<String>createBuilder()
                                        .name(Text.literal("Watermark Text"))
                                        .binding("", () -> p.watermarkText, v -> { p.watermarkText = v; cfg.save(); })
                                        .controller(StringControllerBuilder::create)
                                        .build())

                                .option(Option.<String>createBuilder()
                                        .name(Text.literal("Watermark Position"))
                                        .description(OptionDescription.of(Text.literal("Where the watermark appears on screen.\nOptions: TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT")))
                                        .binding("TOP_RIGHT", () -> p.watermarkPosition, v -> {
                                            String upper = v.toUpperCase().trim();
                                            if (upper.equals("TOP_LEFT") || upper.equals("TOP_RIGHT") ||
                                                    upper.equals("BOTTOM_LEFT") || upper.equals("BOTTOM_RIGHT")) {
                                                p.watermarkPosition = upper;
                                                cfg.save();
                                            }
                                        })
                                        .controller(opt -> CyclingListControllerBuilder.create(opt)
                                                .values(java.util.List.of("TOP_LEFT", "TOP_RIGHT", "BOTTOM_LEFT", "BOTTOM_RIGHT"))
                                                .valueFormatter(v -> Text.literal(v)))
                                        .build())

                                .option(Option.<Double>createBuilder()
                                        .name(Text.literal("Watermark Opacity"))
                                        .description(OptionDescription.of(Text.literal("How visible the watermark is. 1.0 = fully visible, 0.1 = almost invisible.")))
                                        .binding(1.0, () -> (double) p.watermarkOpacity, v -> { p.watermarkOpacity = v.floatValue(); cfg.save(); })
                                        .controller(opt -> DoubleSliderControllerBuilder.create(opt).range(0.1, 1.0).step(0.05))
                                        .build())

                                .build())

                        .group(OptionGroup.createBuilder()
                                .name(Text.literal("Coordinates (F3)"))

                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.literal("Hide Coordinates"))
                                        .binding(false, () -> p.hideCoordinates, v -> { p.hideCoordinates = v; cfg.save(); })
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())

                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.literal("Show Fake Coordinates"))
                                        .binding(false, () -> p.fakeCoordinates, v -> { p.fakeCoordinates = v; cfg.save(); })
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())

                                .build())

                        .group(OptionGroup.createBuilder()
                                .name(Text.literal("Network & Server IPs"))

                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.literal("Hide Server IP in Server List"))
                                        .binding(true, () -> p.hideServerIp, v -> { p.hideServerIp = v; cfg.save(); })
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())

                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.literal("Hide Direct Connect History"))
                                        .binding(true, () -> p.hideDirectConnect, v -> { p.hideDirectConnect = v; cfg.save(); })
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())

                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.literal("Hide IP in F3"))
                                        .binding(true, () -> p.hideF3Ip, v -> { p.hideF3Ip = v; cfg.save(); })
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())

                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.literal("Wipe Session Log on Disconnect"))
                                        .binding(false, () -> p.wipeSessionLog, v -> { p.wipeSessionLog = v; cfg.save(); })
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())

                                .build())

                        .build())

                // ══════════════════════════════════════════════════════════
                // TAB 4 — PROFILES
                // ══════════════════════════════════════════════════════════
                .category(ConfigCategory.createBuilder()
                        .name(Text.literal("Profiles"))
                        .tooltip(Text.literal("Switch between saved setting presets. Changes take effect immediately — the screen will reopen on the new profile."))

                        .group(OptionGroup.createBuilder()
                                .name(Text.literal("Active Profile"))
                                .description(OptionDescription.of(Text.literal("Current: " + cfg.activeProfileName + "\n\nTo switch profiles, select one below and click Apply. The config screen will reopen with that profile loaded.")))

                                .option(Option.<String>createBuilder()
                                        .name(Text.literal("Switch To Profile"))
                                        .description(OptionDescription.of(Text.literal("Type the exact name of a profile to switch to it. Existing profiles: " +
                                                cfg.profiles.stream().map(pr -> pr.name).collect(Collectors.joining(", ")))))
                                        .binding(cfg.activeProfileName,
                                                () -> cfg.activeProfileName,
                                                v -> {
                                                    boolean exists = cfg.profiles.stream().anyMatch(pr -> pr.name.equals(v));
                                                    if (exists) {
                                                        cfg.setActiveProfile(v);
                                                        // Reopen screen so p reference refreshes
                                                        MinecraftClient mc = MinecraftClient.getInstance();
                                                        mc.setScreen(GhostModeConfigScreen.create(null));
                                                    }
                                                })
                                        .controller(StringControllerBuilder::create)
                                        .build())

                                .build())

                        .group(OptionGroup.createBuilder()
                                .name(Text.literal("Create / Duplicate"))
                                .description(OptionDescription.of(Text.literal("Save your current settings as a new profile. Type a name and click Apply.")))

                                .option(Option.<String>createBuilder()
                                        .name(Text.literal("Save Current As..."))
                                        .description(OptionDescription.of(Text.literal("Creates a new profile with all current settings copied into it, then switches to it.")))
                                        .binding("",
                                                () -> "",
                                                v -> {
                                                    if (!v.isBlank()) {
                                                        cfg.saveCurrentAsProfile(v.trim());
                                                        MinecraftClient mc = MinecraftClient.getInstance();
                                                        mc.setScreen(GhostModeConfigScreen.create(null));
                                                    }
                                                })
                                        .controller(StringControllerBuilder::create)
                                        .build())

                                .build())

                        .group(OptionGroup.createBuilder()
                                .name(Text.literal("Delete Profile"))
                                .description(OptionDescription.of(Text.literal("Type a profile name to delete it. Cannot delete Default.")))

                                .option(Option.<String>createBuilder()
                                        .name(Text.literal("Delete Profile Named..."))
                                        .description(OptionDescription.of(Text.literal("Type the exact profile name and click Apply. Default cannot be deleted.")))
                                        .binding("",
                                                () -> "",
                                                v -> {
                                                    if (!v.isBlank() && !v.equals("Default")) {
                                                        cfg.deleteProfile(v.trim());
                                                        MinecraftClient mc = MinecraftClient.getInstance();
                                                        mc.setScreen(GhostModeConfigScreen.create(null));
                                                    }
                                                })
                                        .controller(StringControllerBuilder::create)
                                        .build())

                                .build())

                        .build())

                .save(cfg::save)
                .build()
                .generateScreen(parent);
    }
}