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

                // ══════════════════════════════════════════════════════════
                // TAB 1 — STREAM MODE  (the panic tab — open this first)
                // ══════════════════════════════════════════════════════════
                .category(ConfigCategory.createBuilder()
                        .name(Text.literal("▶ Stream Mode"))
                        .tooltip(Text.literal("The master switch. Turn this on before you go live. Everything else only activates when this is on."))

                        .option(Option.<Boolean>createBuilder()
                                .name(Text.literal("⚡  STREAM MODE  ⚡"))
                                .description(OptionDescription.of(Text.literal("Master privacy switch. When OFF, GhostMode does nothing — your name and info are visible as normal. Turn this ON before streaming or recording. All the options in Protection and Identity only activate when this is on.\n\nIf you need to panic-enable mid-stream: open config (J), this tab is first, this toggle is first.")))
                                .binding(false, () -> p.streamModeEnabled, v -> { p.streamModeEnabled = v; cfg.save(); })
                                .controller(TickBoxControllerBuilder::create)
                                .build())

                        .option(Option.<Boolean>createBuilder()
                                .name(Text.literal("Auto-Enable on Public Servers"))
                                .description(OptionDescription.of(Text.literal("Automatically turns Stream Mode on when you join any public server (not LAN/localhost). Means you never forget before going live.")))
                                .binding(false, () -> p.autoEnablePublic, v -> { p.autoEnablePublic = v; cfg.save(); })
                                .controller(TickBoxControllerBuilder::create)
                                .build())

                        .option(Option.<Boolean>createBuilder()
                                .name(Text.literal("Peek Mode (Hold to Reveal)"))
                                .description(OptionDescription.of(Text.literal("When on, you hold a key combo to temporarily see real names/coords — everything re-hides when you release. Prevents accidental reveals from a misclick. Recommended if streaming live.")))
                                .binding(true, () -> p.peekMode, v -> { p.peekMode = v; cfg.save(); })
                                .controller(TickBoxControllerBuilder::create)
                                .build())

                        .build())

                // ══════════════════════════════════════════════════════════
                // TAB 2 — PROTECTION  (what gets hidden)
                // ══════════════════════════════════════════════════════════
                .category(ConfigCategory.createBuilder()
                        .name(Text.literal("Protection"))
                        .tooltip(Text.literal("Everything that hides or replaces sensitive info on screen. Requires Stream Mode to be ON."))

                        // ── Sentinel group ────────────────────────────────
                        .group(OptionGroup.createBuilder()
                                .name(Text.literal("Sentinel — Auto Name Detection"))
                                .description(OptionDescription.of(Text.literal("Sentinel watches for your IGN and every name on your watchlist, then hides or replaces them anywhere they appear. This is your main leak prevention.")))

                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.literal("Enable Sentinel"))
                                        .description(OptionDescription.of(Text.literal("Turns on automatic name detection and replacement. When on, your IGN and watchlist names are scanned and replaced everywhere they appear.\n\nRequires Stream Mode to be on.")))
                                        .binding(true, () -> p.sentinelEnabled, v -> { p.sentinelEnabled = v; cfg.save(); })
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())

                                .option(Option.<Integer>createBuilder()
                                        .name(Text.literal("Match Sensitivity"))
                                        .description(OptionDescription.of(Text.literal("How aggressively Sentinel matches your name.\n\n1 = Strict: exact matches only\n2 = Normal: catches common variants (recommended)\n3 = Loose: catches typos, leet speak, partials like 'wkslick'")))
                                        .binding(2, () -> p.sentinelSensitivity, v -> { p.sentinelSensitivity = v; cfg.save(); })
                                        .controller(opt -> IntegerSliderControllerBuilder.create(opt).range(1, 3).step(1))
                                        .build())

                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.literal("Cover Chat"))
                                        .description(OptionDescription.of(Text.literal("Replaces matched names in all chat messages — your own messages and messages from others that mention you.")))
                                        .binding(true, () -> p.sentinelChat, v -> { p.sentinelChat = v; cfg.save(); })
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())

                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.literal("Cover Tab List"))
                                        .description(OptionDescription.of(Text.literal("Replaces watchlist names in the tab list. Your own entry is separately controlled by 'Mask Own Tab Entry' below.")))
                                        .binding(true, () -> p.sentinelTabList, v -> { p.sentinelTabList = v; cfg.save(); })
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())

                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.literal("Cover Scoreboard"))
                                        .description(OptionDescription.of(Text.literal("Replaces matched names on the sidebar scoreboard. Many servers show player IGNs there.")))
                                        .binding(true, () -> p.sentinelScoreboard, v -> { p.sentinelScoreboard = v; cfg.save(); })
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())

                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.literal("Cover Nametags"))
                                        .description(OptionDescription.of(Text.literal("Replaces matched names on player nametags above heads. Useful when watchlist players are nearby.")))
                                        .binding(true, () -> p.sentinelNametags, v -> { p.sentinelNametags = v; cfg.save(); })
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())

                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.literal("Cover F3 Screen"))
                                        .description(OptionDescription.of(Text.literal("Scrubs matched names from the F3 debug overlay. F3 can expose your IGN and session data.")))
                                        .binding(true, () -> p.sentinelF3, v -> { p.sentinelF3 = v; cfg.save(); })
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())

                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.literal("Cover Advancements"))
                                        .description(OptionDescription.of(Text.literal("Replaces your name in advancement popups. Minecraft broadcasts '[YourName] has made the advancement [X]' to all players — this catches that.")))
                                        .binding(true, () -> p.sentinelAdvancements, v -> { p.sentinelAdvancements = v; cfg.save(); })
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())

                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.literal("Cover Death Screen"))
                                        .description(OptionDescription.of(Text.literal("Replaces your name on the death screen, where Minecraft displays your IGN in large text.")))
                                        .binding(true, () -> p.sentinelDeathScreen, v -> { p.sentinelDeathScreen = v; cfg.save(); })
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())

                                .build())

                        // ── Tab List group ────────────────────────────────
                        .group(OptionGroup.createBuilder()
                                .name(Text.literal("Tab List"))
                                .description(OptionDescription.of(Text.literal("Controls what you see when you hold Tab. Your own entry is handled separately from other players.")))

                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.literal("Mask Own Tab Entry"))
                                        .description(OptionDescription.of(Text.literal("Replaces YOUR entry in the tab list with your alias. Other players' names are not affected by this toggle — only yours.")))
                                        .binding(true, () -> p.maskOwnTabEntry, v -> { p.maskOwnTabEntry = v; cfg.save(); })
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())

                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.literal("Hide Tab List Entirely"))
                                        .description(OptionDescription.of(Text.literal("Completely prevents the tab list from rendering. Nothing shows when you hold Tab.")))
                                        .binding(false, () -> p.hideTabList, v -> { p.hideTabList = v; cfg.save(); })
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())

                                .build())

                        // ── Scoreboard group ──────────────────────────────
                        .group(OptionGroup.createBuilder()
                                .name(Text.literal("Scoreboard"))
                                .description(OptionDescription.of(Text.literal("Controls the sidebar scoreboard.")))

                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.literal("Hide Scoreboard Entirely"))
                                        .description(OptionDescription.of(Text.literal("Completely hides the sidebar scoreboard. Nothing is shown.")))
                                        .binding(false, () -> p.hideScoreboard, v -> { p.hideScoreboard = v; cfg.save(); })
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())

                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.literal("Mask Names on Scoreboard"))
                                        .description(OptionDescription.of(Text.literal("Replaces Sentinel-matched names on the scoreboard with aliases. Scores remain visible.")))
                                        .binding(true, () -> p.maskScoreboardNames, v -> { p.maskScoreboardNames = v; cfg.save(); })
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())

                                .build())

                        // ── Chat group ────────────────────────────────────
                        .group(OptionGroup.createBuilder()
                                .name(Text.literal("Chat"))
                                .description(OptionDescription.of(Text.literal("Extra chat privacy options beyond Sentinel name coverage.")))

                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.literal("Suppress Incoming DMs"))
                                        .description(OptionDescription.of(Text.literal("Silently drops direct messages and whispers. They won't appear on stream at all. Warning: you will miss these messages — turn off when not live.")))
                                        .binding(false, () -> p.suppressDMs, v -> { p.suppressDMs = v; cfg.save(); })
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())

                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.literal("Remove Mention Highlight"))
                                        .description(OptionDescription.of(Text.literal("Prevents your name flashing or pinging when someone mentions you in chat. Stops a visible reaction that could confirm your identity on stream.")))
                                        .binding(true, () -> p.removeMentionPing, v -> { p.removeMentionPing = v; cfg.save(); })
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())

                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.literal("Disable Chat Log to Disk"))
                                        .description(OptionDescription.of(Text.literal("Stops chat from being written to the chat log file. Your IGN and conversations won't persist in logs after the session.")))
                                        .binding(false, () -> p.disableChatLog, v -> { p.disableChatLog = v; cfg.save(); })
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())

                                .build())

                        // ── Screenshots group ─────────────────────────────
                        .group(OptionGroup.createBuilder()
                                .name(Text.literal("Screenshots"))
                                .description(OptionDescription.of(Text.literal("Protect screenshots taken while streaming.")))

                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.literal("Screenshot Guard"))
                                        .description(OptionDescription.of(Text.literal("When you press F2, GhostMode post-processes the screenshot and blurs Sentinel-matched names before saving to disk. Prevents leaks when sharing screenshots to Discord mid-stream.")))
                                        .binding(true, () -> p.screenshotGuard, v -> { p.screenshotGuard = v; cfg.save(); })
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())

                                .build())

                        .build())

                // ══════════════════════════════════════════════════════════
                // TAB 3 — IDENTITY & NETWORK  (how you appear + server info)
                // ══════════════════════════════════════════════════════════
                .category(ConfigCategory.createBuilder()
                        .name(Text.literal("Identity & Network"))
                        .tooltip(Text.literal("Your alias, watermark, coordinates, and server IP masking."))

                        // ── Identity group ────────────────────────────────
                        .group(OptionGroup.createBuilder()
                                .name(Text.literal("Your Identity"))
                                .description(OptionDescription.of(Text.literal("How your name appears on stream. Set your alias here.")))

                                .option(Option.<String>createBuilder()
                                        .name(Text.literal("Alias (Your Stream Name)"))
                                        .description(OptionDescription.of(Text.literal("This replaces your real IGN everywhere when Stream Mode is on. Set it to whatever you want viewers to see — e.g. 'AltAccount', your main channel name, or just 'Player'.\n\nLeave blank to show ??? instead.")))
                                        .binding("Player", () -> p.alias, v -> { p.alias = v.isBlank() ? "Player" : v; cfg.save(); })
                                        .controller(StringControllerBuilder::create)
                                        .build())

                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.literal("Use Alias (Replace Name)"))
                                        .description(OptionDescription.of(Text.literal("When on, your IGN is replaced with the alias above. Turn off temporarily to show your real name without disabling Stream Mode entirely.")))
                                        .binding(true, () -> p.useAlias, v -> { p.useAlias = v; cfg.save(); })
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())

                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.literal("Hide Own Nametag"))
                                        .description(OptionDescription.of(Text.literal("Hides your nametag in F5 third-person view. Prevents your IGN appearing above your head in clips.")))
                                        .binding(false, () -> p.hideOwnNametag, v -> { p.hideOwnNametag = v; cfg.save(); })
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())

                                .build())

                        // ── Watermark group ───────────────────────────────
                        .group(OptionGroup.createBuilder()
                                .name(Text.literal("Watermark"))
                                .description(OptionDescription.of(Text.literal("Show your main channel name in a corner of the screen. Even on an alt, clips will be attributed to you.")))

                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.literal("Show Watermark"))
                                        .description(OptionDescription.of(Text.literal("Overlays your channel name in a corner of the screen. Great for branding clips from an alt back to your main.")))
                                        .binding(false, () -> p.showWatermark, v -> { p.showWatermark = v; cfg.save(); })
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())

                                .option(Option.<String>createBuilder()
                                        .name(Text.literal("Watermark Text"))
                                        .description(OptionDescription.of(Text.literal("The text shown in the watermark. Usually your main channel name or brand handle.")))
                                        .binding("", () -> p.watermarkText, v -> { p.watermarkText = v; cfg.save(); })
                                        .controller(StringControllerBuilder::create)
                                        .build())

                                .build())

                        // ── Coordinates group ─────────────────────────────
                        .group(OptionGroup.createBuilder()
                                .name(Text.literal("Coordinates (F3)"))
                                .description(OptionDescription.of(Text.literal("Hide or spoof your XYZ in F3. Real coords can reveal base locations on survival servers.")))

                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.literal("Hide Coordinates"))
                                        .description(OptionDescription.of(Text.literal("Completely removes XYZ from the F3 screen. Use this if you just want them gone.")))
                                        .binding(false, () -> p.hideCoordinates, v -> { p.hideCoordinates = v; cfg.save(); })
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())

                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.literal("Show Fake Coordinates"))
                                        .description(OptionDescription.of(Text.literal("Shows fake XYZ values in F3 instead of your real position. Your actual movement is unaffected — only the display is spoofed.")))
                                        .binding(false, () -> p.fakeCoordinates, v -> { p.fakeCoordinates = v; cfg.save(); })
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())

                                .build())

                        // ── Network group ─────────────────────────────────
                        .group(OptionGroup.createBuilder()
                                .name(Text.literal("Network & Server IPs"))
                                .description(OptionDescription.of(Text.literal("Mask server IPs and connection info so they can't be seen on stream.")))

                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.literal("Hide Server IP in Server List"))
                                        .description(OptionDescription.of(Text.literal("Replaces server IPs in the multiplayer list with ••••••••••. The connection works normally — only the display is masked.")))
                                        .binding(true, () -> p.hideServerIp, v -> { p.hideServerIp = v; cfg.save(); })
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())

                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.literal("Hide Direct Connect History"))
                                        .description(OptionDescription.of(Text.literal("Masks the last-used address in the Direct Connect screen. Prevents accidentally flashing a private IP when opening the menu on stream.")))
                                        .binding(true, () -> p.hideDirectConnect, v -> { p.hideDirectConnect = v; cfg.save(); })
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())

                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.literal("Hide IP in F3"))
                                        .description(OptionDescription.of(Text.literal("Removes your local IP and network info from the F3 debug screen.")))
                                        .binding(true, () -> p.hideF3Ip, v -> { p.hideF3Ip = v; cfg.save(); })
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())

                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.literal("Wipe Session Log on Disconnect"))
                                        .description(OptionDescription.of(Text.literal("Clears your IGN, IPs, and session info from latest.log when you leave a server. Prevents that data from sitting on disk after a session.")))
                                        .binding(false, () -> p.wipeSessionLog, v -> { p.wipeSessionLog = v; cfg.save(); })
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())

                                .build())

                        .build())

                .save(cfg::save)
                .build()
                .generateScreen(parent);
    }
}