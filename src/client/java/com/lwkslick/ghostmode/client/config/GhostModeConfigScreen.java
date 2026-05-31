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
                        .tooltip(Text.literal("Sentinel automatically detects your IGN and every name on your watchlist, then hides or replaces them anywhere they appear in-game. This is your main leak prevention system."))

                        .option(Option.<Boolean>createBuilder()
                                .name(Text.literal("Enable Sentinel"))
                                .description(OptionDescription.of(Text.literal("Master toggle for the Sentinel name detection system. When on, your IGN and all watchlist names are automatically scanned and replaced everywhere they could appear on stream.")))
                                .binding(true, () -> p.sentinelEnabled, v -> { p.sentinelEnabled = v; cfg.save(); })
                                .controller(TickBoxControllerBuilder::create)
                                .build())

                        .option(Option.<Integer>createBuilder()
                                .name(Text.literal("Fuzzy Match Sensitivity"))
                                .description(OptionDescription.of(Text.literal("Controls how aggressively Sentinel matches your name.\n\n1 = Strict: exact matches only.\n2 = Normal: catches common variants and spacing.\n3 = Loose: catches typos, leet speak, and partial matches like 'wkslick' or 'lwk5l1ck'.\n\nRecommended: 2 for daily use, 3 if you've had leaks before.")))
                                .binding(2, () -> p.sentinelSensitivity, v -> { p.sentinelSensitivity = v; cfg.save(); })
                                .controller(opt -> IntegerSliderControllerBuilder.create(opt).range(1, 3).step(1))
                                .build())

                        .option(Option.<Boolean>createBuilder()
                                .name(Text.literal("Cover Chat"))
                                .description(OptionDescription.of(Text.literal("Replaces your name and watchlist names in all chat messages. Catches both your own messages and messages from other players that mention you.")))
                                .binding(true, () -> p.sentinelChat, v -> { p.sentinelChat = v; cfg.save(); })
                                .controller(TickBoxControllerBuilder::create)
                                .build())

                        .option(Option.<Boolean>createBuilder()
                                .name(Text.literal("Cover Scoreboard"))
                                .description(OptionDescription.of(Text.literal("Replaces matched names on server scoreboards. Many servers show player IGNs on the sidebar — this prevents your name from appearing there on stream.")))
                                .binding(true, () -> p.sentinelScoreboard, v -> { p.sentinelScoreboard = v; cfg.save(); })
                                .controller(TickBoxControllerBuilder::create)
                                .build())

                        .option(Option.<Boolean>createBuilder()
                                .name(Text.literal("Cover Tab List"))
                                .description(OptionDescription.of(Text.literal("Replaces matched names in the player tab list. Your own entry and any watchlist entries will show their aliases instead of real IGNs.")))
                                .binding(true, () -> p.sentinelTabList, v -> { p.sentinelTabList = v; cfg.save(); })
                                .controller(TickBoxControllerBuilder::create)
                                .build())

                        .option(Option.<Boolean>createBuilder()
                                .name(Text.literal("Cover Nametags"))
                                .description(OptionDescription.of(Text.literal("Replaces matched names on player nametags floating above heads. Useful when other players on your watchlist are nearby and visible on screen.")))
                                .binding(true, () -> p.sentinelNametags, v -> { p.sentinelNametags = v; cfg.save(); })
                                .controller(TickBoxControllerBuilder::create)
                                .build())

                        .option(Option.<Boolean>createBuilder()
                                .name(Text.literal("Cover F3 Debug Screen"))
                                .description(OptionDescription.of(Text.literal("Scrubs matched names from the F3 debug overlay. The F3 screen can expose your IGN and other sensitive info — this hides it.")))
                                .binding(true, () -> p.sentinelF3, v -> { p.sentinelF3 = v; cfg.save(); })
                                .controller(TickBoxControllerBuilder::create)
                                .build())

                        .option(Option.<Boolean>createBuilder()
                                .name(Text.literal("Cover Advancements"))
                                .description(OptionDescription.of(Text.literal("Suppresses or replaces your name in advancement popups. By default Minecraft broadcasts '[YourName] has made the advancement [X]' to all players — this catches that.")))
                                .binding(true, () -> p.sentinelAdvancements, v -> { p.sentinelAdvancements = v; cfg.save(); })
                                .controller(TickBoxControllerBuilder::create)
                                .build())

                        .option(Option.<Boolean>createBuilder()
                                .name(Text.literal("Cover Death Screen"))
                                .description(OptionDescription.of(Text.literal("Replaces your name on the death screen. The death screen displays your IGN in large text — this swaps it for your alias.")))
                                .binding(true, () -> p.sentinelDeathScreen, v -> { p.sentinelDeathScreen = v; cfg.save(); })
                                .controller(TickBoxControllerBuilder::create)
                                .build())

                        .option(Option.<Boolean>createBuilder()
                                .name(Text.literal("Screenshot Guard"))
                                .description(OptionDescription.of(Text.literal("When you take a screenshot (F2), GhostMode post-processes the image and blurs any Sentinel-matched names before saving to disk. Prevents leaks when sharing screenshots to Discord mid-stream.")))
                                .binding(true, () -> p.screenshotGuard, v -> { p.screenshotGuard = v; cfg.save(); })
                                .controller(TickBoxControllerBuilder::create)
                                .build())

                        .build())

                // ── IDENTITY ──────────────────────────────────────────────
                .category(ConfigCategory.createBuilder()
                        .name(Text.literal("Identity"))
                        .tooltip(Text.literal("Control how your name and brand appear on stream. Set an alias so clips from your alt still look like yours."))

                        .option(Option.<Boolean>createBuilder()
                                .name(Text.literal("Use Alias"))
                                .description(OptionDescription.of(Text.literal("When enabled, your real IGN is replaced with the alias below everywhere it appears. Turn this off if you want to temporarily show your real name without disabling Sentinel.")))
                                .binding(true, () -> p.useAlias, v -> { p.useAlias = v; cfg.save(); })
                                .controller(TickBoxControllerBuilder::create)
                                .build())

                        .option(Option.<String>createBuilder()
                                .name(Text.literal("Alias Name"))
                                .description(OptionDescription.of(Text.literal("The name that replaces your IGN everywhere on stream. Pick something neutral like 'Player' or 'AltAccount' — or anything you want viewers to see instead.")))
                                .binding("Player", () -> p.alias, v -> { p.alias = v; cfg.save(); })
                                .controller(StringControllerBuilder::create)
                                .build())

                        .option(Option.<Boolean>createBuilder()
                                .name(Text.literal("Hide Own Nametag"))
                                .description(OptionDescription.of(Text.literal("Hides your own floating nametag when in F5 third-person view. Prevents your IGN from appearing above your head in clips and stream footage.")))
                                .binding(false, () -> p.hideOwnNametag, v -> { p.hideOwnNametag = v; cfg.save(); })
                                .controller(TickBoxControllerBuilder::create)
                                .build())

                        .option(Option.<Boolean>createBuilder()
                                .name(Text.literal("Show Watermark"))
                                .description(OptionDescription.of(Text.literal("Overlays your main channel name in a corner of the screen. Even when playing on an alt, clips will show your brand. Great for when you want content attributed back to your main.")))
                                .binding(false, () -> p.showWatermark, v -> { p.showWatermark = v; cfg.save(); })
                                .controller(TickBoxControllerBuilder::create)
                                .build())

                        .option(Option.<String>createBuilder()
                                .name(Text.literal("Watermark Text"))
                                .description(OptionDescription.of(Text.literal("The text shown in the watermark overlay. Usually your main channel name or brand handle.")))
                                .binding("", () -> p.watermarkText, v -> { p.watermarkText = v; cfg.save(); })
                                .controller(StringControllerBuilder::create)
                                .build())

                        .build())

                // ── NETWORK ───────────────────────────────────────────────
                .category(ConfigCategory.createBuilder()
                        .name(Text.literal("Network"))
                        .tooltip(Text.literal("Prevent server IPs and connection info from appearing on stream. IPs shown on screen can expose private servers and get you targeted."))

                        .option(Option.<Boolean>createBuilder()
                                .name(Text.literal("Hide Server IP in List"))
                                .description(OptionDescription.of(Text.literal("Replaces server IPs in the multiplayer server list with •••••••••• so they never appear on stream. The connection still works normally — only the display is masked.")))
                                .binding(true, () -> p.hideServerIp, v -> { p.hideServerIp = v; cfg.save(); })
                                .controller(TickBoxControllerBuilder::create)
                                .build())

                        .option(Option.<Integer>createBuilder()
                                .name(Text.literal("IP Reveal Key"))
                                .description(OptionDescription.of(Text.literal("The key to hold to temporarily reveal masked server IPs. Default: Left Alt.")))
                                .binding(org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT_ALT, () -> p.ipRevealKey, v -> { p.ipRevealKey = v; cfg.save(); })
                                .controller(opt -> IntegerSliderControllerBuilder.create(opt).range(256, 348).step(1))
                                .build())

                        .option(Option.<Boolean>createBuilder()
                                .name(Text.literal("Hide Direct Connect History"))
                                .description(OptionDescription.of(Text.literal("Masks the last-used address in the Direct Connect screen with ••••••••••. Prevents accidentally flashing a private server IP when opening the menu on stream.")))
                                .binding(true, () -> p.hideDirectConnect, v -> { p.hideDirectConnect = v; cfg.save(); })
                                .controller(TickBoxControllerBuilder::create)
                                .build())

                        .option(Option.<Boolean>createBuilder()
                                .name(Text.literal("Hide IP in F3"))
                                .description(OptionDescription.of(Text.literal("Removes your local IP address and network info from the F3 debug overlay. The F3 screen shows your connection details by default — this blanks those fields.")))
                                .binding(true, () -> p.hideF3Ip, v -> { p.hideF3Ip = v; cfg.save(); })
                                .controller(TickBoxControllerBuilder::create)
                                .build())

                        .option(Option.<Boolean>createBuilder()
                                .name(Text.literal("Wipe Session Log on Disconnect"))
                                .description(OptionDescription.of(Text.literal("Clears relevant entries from latest.log when you leave a server. latest.log stores your IGN, server IPs, and session info — this prevents that data from sitting on disk after a session.")))
                                .binding(false, () -> p.wipeSessionLog, v -> { p.wipeSessionLog = v; cfg.save(); })
                                .controller(TickBoxControllerBuilder::create)
                                .build())

                        .build())

                // ── COORDINATES ───────────────────────────────────────────
                .category(ConfigCategory.createBuilder()
                        .name(Text.literal("Coordinates"))
                        .tooltip(Text.literal("Hide or spoof your XYZ coordinates in F3. Your real position can reveal base locations on survival servers."))

                        .option(Option.<Boolean>createBuilder()
                                .name(Text.literal("Hide Coordinates in F3"))
                                .description(OptionDescription.of(Text.literal("Completely removes XYZ coordinates from the F3 debug screen. Use this if you don't need fake coords, just want them gone entirely.")))
                                .binding(false, () -> p.hideCoordinates, v -> { p.hideCoordinates = v; cfg.save(); })
                                .controller(TickBoxControllerBuilder::create)
                                .build())

                        .option(Option.<Boolean>createBuilder()
                                .name(Text.literal("Show Fake Coordinates"))
                                .description(OptionDescription.of(Text.literal("Displays custom coordinates in F3 instead of your real XYZ. Your actual position is unchanged — only the display is spoofed. Set the fake values below.")))
                                .binding(false, () -> p.fakeCoordinates, v -> { p.fakeCoordinates = v; cfg.save(); })
                                .controller(TickBoxControllerBuilder::create)
                                .build())

                        .build())

                // ── TAB LIST ──────────────────────────────────────────────
                .category(ConfigCategory.createBuilder()
                        .name(Text.literal("Tab List"))
                        .tooltip(Text.literal("Control what the player tab list shows. Prevent your IGN from being visible when holding Tab on stream."))

                        .option(Option.<Boolean>createBuilder()
                                .name(Text.literal("Mask Own Entry"))
                                .description(OptionDescription.of(Text.literal("Replaces your own entry in the tab list with your alias. Other players' names are unaffected — only yours is swapped.")))
                                .binding(true, () -> p.maskOwnTabEntry, v -> { p.maskOwnTabEntry = v; cfg.save(); })
                                .controller(TickBoxControllerBuilder::create)
                                .build())

                        .option(Option.<Boolean>createBuilder()
                                .name(Text.literal("Player Count Only Mode"))
                                .description(OptionDescription.of(Text.literal("Replaces the full tab list with just a player count, e.g. '24 players online'. No names are shown at all. Useful on large servers where the tab list is full of IGNs.")))
                                .binding(false, () -> p.playerCountOnly, v -> { p.playerCountOnly = v; cfg.save(); })
                                .controller(TickBoxControllerBuilder::create)
                                .build())

                        .option(Option.<Boolean>createBuilder()
                                .name(Text.literal("Hide Tab List Entirely"))
                                .description(OptionDescription.of(Text.literal("Completely prevents the tab list from rendering when you hold Tab. Nothing is shown at all.")))
                                .binding(false, () -> p.hideTabList, v -> { p.hideTabList = v; cfg.save(); })
                                .controller(TickBoxControllerBuilder::create)
                                .build())

                        .build())

                // ── SCOREBOARD ────────────────────────────────────────────
                .category(ConfigCategory.createBuilder()
                        .name(Text.literal("Scoreboard"))
                        .tooltip(Text.literal("Control the sidebar scoreboard. Many servers use it to display player names, stats, or other info that could identify you."))

                        .option(Option.<Boolean>createBuilder()
                                .name(Text.literal("Hide Scoreboard"))
                                .description(OptionDescription.of(Text.literal("Completely hides the sidebar scoreboard from rendering. Nothing is shown — not names, not scores, nothing.")))
                                .binding(false, () -> p.hideScoreboard, v -> { p.hideScoreboard = v; cfg.save(); })
                                .controller(TickBoxControllerBuilder::create)
                                .build())

                        .option(Option.<Boolean>createBuilder()
                                .name(Text.literal("Mask Names on Scoreboard"))
                                .description(OptionDescription.of(Text.literal("Replaces Sentinel-matched names on the scoreboard with aliases. Scores and other info remain visible — only matched IGNs are swapped out.")))
                                .binding(true, () -> p.maskScoreboardNames, v -> { p.maskScoreboardNames = v; cfg.save(); })
                                .controller(TickBoxControllerBuilder::create)
                                .build())

                        .build())

                // ── CHAT ──────────────────────────────────────────────────
                .category(ConfigCategory.createBuilder()
                        .name(Text.literal("Chat"))
                        .tooltip(Text.literal("Protect your chat from leaking your identity. Suppress DMs, remove name highlights, and stop chat from being written to disk."))

                        .option(Option.<Boolean>createBuilder()
                                .name(Text.literal("Suppress Incoming DMs"))
                                .description(OptionDescription.of(Text.literal("Silently drops direct messages and whispers while active. They won't appear in chat at all on stream. Note: you will miss these messages — turn off when you're not live.")))
                                .binding(false, () -> p.suppressDMs, v -> { p.suppressDMs = v; cfg.save(); })
                                .controller(TickBoxControllerBuilder::create)
                                .build())

                        .option(Option.<Boolean>createBuilder()
                                .name(Text.literal("Remove Mention Highlight"))
                                .description(OptionDescription.of(Text.literal("Stops your name from flashing, playing a sound, or highlighting when someone mentions you in chat. Prevents a visible reaction on stream that could confirm your identity.")))
                                .binding(true, () -> p.removeMentionPing, v -> { p.removeMentionPing = v; cfg.save(); })
                                .controller(TickBoxControllerBuilder::create)
                                .build())

                        .option(Option.<Boolean>createBuilder()
                                .name(Text.literal("Disable Chat Log to Disk"))
                                .description(OptionDescription.of(Text.literal("Prevents chat messages from being written to the chat log file on disk. Your IGN and server conversations won't persist in logs after the session ends.")))
                                .binding(false, () -> p.disableChatLog, v -> { p.disableChatLog = v; cfg.save(); })
                                .controller(TickBoxControllerBuilder::create)
                                .build())

                        .build())

                // ── STREAM MODE ───────────────────────────────────────────
                .category(ConfigCategory.createBuilder()
                        .name(Text.literal("Stream Mode"))
                        .tooltip(Text.literal("Stream Mode is the master privacy switch. One toggle activates everything at once. Use a safe key combo so you never accidentally turn it off mid-stream."))

                        .option(Option.<Boolean>createBuilder()
                                .name(Text.literal("Stream Mode Active"))
                                .description(OptionDescription.of(Text.literal("The master privacy toggle. When on, all active privacy features in your current profile are enforced. Turn this off only when you're not streaming or recording.")))
                                .binding(false, () -> p.streamModeEnabled, v -> { p.streamModeEnabled = v; cfg.save(); })
                                .controller(TickBoxControllerBuilder::create)
                                .build())

                        .option(Option.<Boolean>createBuilder()
                                .name(Text.literal("Auto-Enable on Public Servers"))
                                .description(OptionDescription.of(Text.literal("Automatically activates Stream Mode whenever you join a public server (any non-LAN, non-localhost address). You'll never forget to turn it on before going live.")))
                                .binding(false, () -> p.autoEnablePublic, v -> { p.autoEnablePublic = v; cfg.save(); })
                                .controller(TickBoxControllerBuilder::create)
                                .build())

                        .option(Option.<Boolean>createBuilder()
                                .name(Text.literal("Peek Mode (Hold to Reveal)"))
                                .description(OptionDescription.of(Text.literal("Instead of a single toggle, you must hold a key combo to temporarily reveal hidden info. Everything re-hides the moment you release. Prevents accidental leaks from a misclick — recommended for live streaming.")))
                                .binding(true, () -> p.peekMode, v -> { p.peekMode = v; cfg.save(); })
                                .controller(TickBoxControllerBuilder::create)
                                .build())

                        .build())

                .save(cfg::save)
                .build()
                .generateScreen(parent);
    }
}