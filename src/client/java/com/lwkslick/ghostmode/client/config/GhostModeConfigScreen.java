package com.lwkslick.ghostmode.client.config;

import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.*;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class GhostModeConfigScreen {

    public static Screen create(Screen parent) {
        GhostModeConfig cfg = GhostModeConfig.get();
        GhostModeConfig.Profile p = cfg.getActiveProfile();

        return YetAnotherConfigLib.createBuilder()
                .title(Text.literal("GhostMode"))

                // ── SENTINEL ──────────────────────────────────────────────
                .category(ConfigCategory.createBuilder()
                        .name(Text.literal("Sentinel"))
                        .tooltip(Text.literal("Auto-detects and hides your name everywhere"))

                        .option(Option.<Boolean>createBuilder()
                                .name(Text.literal("Enable Sentinel"))
                                .description(OptionDescription.of(Text.literal("Scans for your IGN and watchlist names everywhere and replaces them.")))
                                .binding(true, () -> p.sentinelEnabled, v -> { p.sentinelEnabled = v; cfg.save(); })
                                .controller(TickBoxControllerBuilder::create)
                                .build())

                        .option(Option.<Integer>createBuilder()
                                .name(Text.literal("Fuzzy Match Sensitivity"))
                                .description(OptionDescription.of(Text.literal("1 = strict (exact only), 2 = normal, 3 = loose (catches typos and variants)")))
                                .binding(2, () -> p.sentinelSensitivity, v -> { p.sentinelSensitivity = v; cfg.save(); })
                                .controller(opt -> IntegerSliderControllerBuilder.create(opt).range(1, 3).step(1))
                                .build())

                        .option(Option.<Boolean>createBuilder()
                                .name(Text.literal("Cover Chat"))
                                .binding(true, () -> p.sentinelChat, v -> { p.sentinelChat = v; cfg.save(); })
                                .controller(TickBoxControllerBuilder::create)
                                .build())

                        .option(Option.<Boolean>createBuilder()
                                .name(Text.literal("Cover Scoreboard"))
                                .binding(true, () -> p.sentinelScoreboard, v -> { p.sentinelScoreboard = v; cfg.save(); })
                                .controller(TickBoxControllerBuilder::create)
                                .build())

                        .option(Option.<Boolean>createBuilder()
                                .name(Text.literal("Cover Tab List"))
                                .binding(true, () -> p.sentinelTabList, v -> { p.sentinelTabList = v; cfg.save(); })
                                .controller(TickBoxControllerBuilder::create)
                                .build())

                        .option(Option.<Boolean>createBuilder()
                                .name(Text.literal("Cover Nametags"))
                                .binding(true, () -> p.sentinelNametags, v -> { p.sentinelNametags = v; cfg.save(); })
                                .controller(TickBoxControllerBuilder::create)
                                .build())

                        .option(Option.<Boolean>createBuilder()
                                .name(Text.literal("Cover F3 Debug Screen"))
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

                        .option(Option.<Boolean>createBuilder()
                                .name(Text.literal("Screenshot Guard"))
                                .description(OptionDescription.of(Text.literal("Blurs matched names before saving screenshots to disk.")))
                                .binding(true, () -> p.screenshotGuard, v -> { p.screenshotGuard = v; cfg.save(); })
                                .controller(TickBoxControllerBuilder::create)
                                .build())

                        .build())

                // ── IDENTITY ──────────────────────────────────────────────
                .category(ConfigCategory.createBuilder()
                        .name(Text.literal("Identity"))

                        .option(Option.<Boolean>createBuilder()
                                .name(Text.literal("Use Alias"))
                                .description(OptionDescription.of(Text.literal("Replaces your name with the alias below everywhere it appears.")))
                                .binding(true, () -> p.useAlias, v -> { p.useAlias = v; cfg.save(); })
                                .controller(TickBoxControllerBuilder::create)
                                .build())

                        .option(Option.<String>createBuilder()
                                .name(Text.literal("Alias Name"))
                                .binding("Player", () -> p.alias, v -> { p.alias = v; cfg.save(); })
                                .controller(StringControllerBuilder::create)
                                .build())

                        .option(Option.<Boolean>createBuilder()
                                .name(Text.literal("Hide Own Nametag"))
                                .description(OptionDescription.of(Text.literal("Hides your nametag in F5 third-person view.")))
                                .binding(false, () -> p.hideOwnNametag, v -> { p.hideOwnNametag = v; cfg.save(); })
                                .controller(TickBoxControllerBuilder::create)
                                .build())

                        .option(Option.<Boolean>createBuilder()
                                .name(Text.literal("Show Watermark"))
                                .description(OptionDescription.of(Text.literal("Overlays your main channel name on screen so alt clips get attributed to you.")))
                                .binding(false, () -> p.showWatermark, v -> { p.showWatermark = v; cfg.save(); })
                                .controller(TickBoxControllerBuilder::create)
                                .build())

                        .option(Option.<String>createBuilder()
                                .name(Text.literal("Watermark Text"))
                                .binding("", () -> p.watermarkText, v -> { p.watermarkText = v; cfg.save(); })
                                .controller(StringControllerBuilder::create)
                                .build())

                        .build())

                // ── NETWORK ───────────────────────────────────────────────
                .category(ConfigCategory.createBuilder()
                        .name(Text.literal("Network"))

                        .option(Option.<Boolean>createBuilder()
                                .name(Text.literal("Hide Server IP"))
                                .description(OptionDescription.of(Text.literal("Replaces server IP with •••••••••• in the server list.")))
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
                                .description(OptionDescription.of(Text.literal("Clears latest.log entries on server disconnect so your IGN and IPs don't sit on disk.")))
                                .binding(false, () -> p.wipeSessionLog, v -> { p.wipeSessionLog = v; cfg.save(); })
                                .controller(TickBoxControllerBuilder::create)
                                .build())

                        .build())

                // ── COORDINATES ───────────────────────────────────────────
                .category(ConfigCategory.createBuilder()
                        .name(Text.literal("Coordinates"))

                        .option(Option.<Boolean>createBuilder()
                                .name(Text.literal("Hide Coordinates in F3"))
                                .binding(false, () -> p.hideCoordinates, v -> { p.hideCoordinates = v; cfg.save(); })
                                .controller(TickBoxControllerBuilder::create)
                                .build())

                        .option(Option.<Boolean>createBuilder()
                                .name(Text.literal("Show Fake Coordinates"))
                                .description(OptionDescription.of(Text.literal("Displays custom coordinates in F3 instead of your real position.")))
                                .binding(false, () -> p.fakeCoordinates, v -> { p.fakeCoordinates = v; cfg.save(); })
                                .controller(TickBoxControllerBuilder::create)
                                .build())

                        .build())

                // ── TAB LIST ──────────────────────────────────────────────
                .category(ConfigCategory.createBuilder()
                        .name(Text.literal("Tab List"))

                        .option(Option.<Boolean>createBuilder()
                                .name(Text.literal("Mask Own Entry"))
                                .description(OptionDescription.of(Text.literal("Replaces your name in the tab list with your alias.")))
                                .binding(true, () -> p.maskOwnTabEntry, v -> { p.maskOwnTabEntry = v; cfg.save(); })
                                .controller(TickBoxControllerBuilder::create)
                                .build())

                        .option(Option.<Boolean>createBuilder()
                                .name(Text.literal("Player Count Only Mode"))
                                .description(OptionDescription.of(Text.literal("Shows only the player count instead of the full tab list.")))
                                .binding(false, () -> p.playerCountOnly, v -> { p.playerCountOnly = v; cfg.save(); })
                                .controller(TickBoxControllerBuilder::create)
                                .build())

                        .option(Option.<Boolean>createBuilder()
                                .name(Text.literal("Hide Tab List Entirely"))
                                .binding(false, () -> p.hideTabList, v -> { p.hideTabList = v; cfg.save(); })
                                .controller(TickBoxControllerBuilder::create)
                                .build())

                        .option(Option.<Boolean>createBuilder()
                                .name(Text.literal("Fake Ping"))
                                .description(OptionDescription.of(Text.literal("Shows a static fake ping value in the tab list.")))
                                .binding(false, () -> p.fakePing, v -> { p.fakePing = v; cfg.save(); })
                                .controller(TickBoxControllerBuilder::create)
                                .build())

                        .option(Option.<Integer>createBuilder()
                                .name(Text.literal("Fake Ping Value (ms)"))
                                .binding(20, () -> p.fakePingValue, v -> { p.fakePingValue = v; cfg.save(); })
                                .controller(opt -> IntegerSliderControllerBuilder.create(opt).range(1, 999).step(1))
                                .build())

                        .build())

                // ── SCOREBOARD ────────────────────────────────────────────
                .category(ConfigCategory.createBuilder()
                        .name(Text.literal("Scoreboard"))

                        .option(Option.<Boolean>createBuilder()
                                .name(Text.literal("Hide Scoreboard"))
                                .binding(false, () -> p.hideScoreboard, v -> { p.hideScoreboard = v; cfg.save(); })
                                .controller(TickBoxControllerBuilder::create)
                                .build())

                        .option(Option.<Boolean>createBuilder()
                                .name(Text.literal("Mask Names on Scoreboard"))
                                .binding(true, () -> p.maskScoreboardNames, v -> { p.maskScoreboardNames = v; cfg.save(); })
                                .controller(TickBoxControllerBuilder::create)
                                .build())

                        .build())

                // ── CHAT ──────────────────────────────────────────────────
                .category(ConfigCategory.createBuilder()
                        .name(Text.literal("Chat"))

                        .option(Option.<Boolean>createBuilder()
                                .name(Text.literal("Suppress Incoming DMs"))
                                .description(OptionDescription.of(Text.literal("Silently drops direct messages while streaming.")))
                                .binding(false, () -> p.suppressDMs, v -> { p.suppressDMs = v; cfg.save(); })
                                .controller(TickBoxControllerBuilder::create)
                                .build())

                        .option(Option.<Boolean>createBuilder()
                                .name(Text.literal("Remove Mention Highlight"))
                                .description(OptionDescription.of(Text.literal("Stops your name from flashing or pinging in chat.")))
                                .binding(true, () -> p.removeMentionPing, v -> { p.removeMentionPing = v; cfg.save(); })
                                .controller(TickBoxControllerBuilder::create)
                                .build())

                        .option(Option.<Boolean>createBuilder()
                                .name(Text.literal("Disable Chat Log to Disk"))
                                .binding(false, () -> p.disableChatLog, v -> { p.disableChatLog = v; cfg.save(); })
                                .controller(TickBoxControllerBuilder::create)
                                .build())

                        .build())

                // ── STREAM MODE ───────────────────────────────────────────
                .category(ConfigCategory.createBuilder()
                        .name(Text.literal("Stream Mode"))

                        .option(Option.<Boolean>createBuilder()
                                .name(Text.literal("Stream Mode Active"))
                                .description(OptionDescription.of(Text.literal("Master toggle. Activates all privacy features at once.")))
                                .binding(false, () -> p.streamModeEnabled, v -> { p.streamModeEnabled = v; cfg.save(); })
                                .controller(TickBoxControllerBuilder::create)
                                .build())

                        .option(Option.<Boolean>createBuilder()
                                .name(Text.literal("Auto-Enable on Public Servers"))
                                .description(OptionDescription.of(Text.literal("Automatically activates stream mode when joining any non-LAN server.")))
                                .binding(false, () -> p.autoEnablePublic, v -> { p.autoEnablePublic = v; cfg.save(); })
                                .controller(TickBoxControllerBuilder::create)
                                .build())

                        .option(Option.<Boolean>createBuilder()
                                .name(Text.literal("Safe Combo (Peek) Mode"))
                                .description(OptionDescription.of(Text.literal("Hold a key combo to temporarily reveal — releases when you let go. Prevents accidental leaks.")))
                                .binding(true, () -> p.peekMode, v -> { p.peekMode = v; cfg.save(); })
                                .controller(TickBoxControllerBuilder::create)
                                .build())

                        .build())

                .save(cfg::save)
                .build()
                .generateScreen(parent);
    }
}