package com.lwkslick.ghostmode.client.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class GhostModeConfig {

    // ── Singleton ──────────────────────────────────────────────────────────
    private static GhostModeConfig INSTANCE;
    public static GhostModeConfig get() { return INSTANCE; }

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH =
            FabricLoader.getInstance().getConfigDir().resolve("ghostmode.json");

    // ── Profile system ─────────────────────────────────────────────────────
    public List<Profile> profiles = new ArrayList<>();
    public String activeProfileName = "Default";

    public Profile getActiveProfile() {
        return profiles.stream()
                .filter(p -> p.name.equals(activeProfileName))
                .findFirst()
                .orElseGet(() -> {
                    Profile def = new Profile("Default");
                    profiles.add(def);
                    return def;
                });
    }

    public void setActiveProfile(String name) {
        activeProfileName = name;
        save();
    }

    public void saveCurrentAsProfile(String name) {
        profiles.removeIf(p -> p.name.equals(name));
        Profile copy = getActiveProfile().copyAs(name);
        profiles.add(copy);
        activeProfileName = name;
        save();
    }

    public void renameProfile(String oldName, String newName) {
        if (oldName.equals("Default") || newName.isBlank()) return;
        if (profiles.stream().anyMatch(pr -> pr.name.equals(newName))) return; // name taken
        profiles.stream().filter(pr -> pr.name.equals(oldName)).findFirst()
                .ifPresent(pr -> pr.name = newName);
        if (activeProfileName.equals(oldName)) activeProfileName = newName;
        save();
    }

    public void deleteProfile(String name) {
        if (name.equals("Default")) return;
        profiles.removeIf(p -> p.name.equals(name));
        if (activeProfileName.equals(name)) activeProfileName = "Default";
        save();
    }

    // ── Load / Save ────────────────────────────────────────────────────────
    public static void load() {
        if (Files.exists(CONFIG_PATH)) {
            try (Reader r = Files.newBufferedReader(CONFIG_PATH)) {
                INSTANCE = GSON.fromJson(r, GhostModeConfig.class);
                if (INSTANCE == null) INSTANCE = new GhostModeConfig();
            } catch (Exception e) {
                INSTANCE = new GhostModeConfig();
            }
        } else {
            INSTANCE = new GhostModeConfig();
        }
        if (INSTANCE.profiles.isEmpty()) {
            INSTANCE.profiles.add(new Profile("Default"));
        }
        INSTANCE.save();
    }

    public void save() {
        try (Writer w = Files.newBufferedWriter(CONFIG_PATH)) {
            GSON.toJson(this, w);
        } catch (Exception ignored) {}
    }

    // ══════════════════════════════════════════════════════════════════════
    // Profile — every single toggle lives here
    // ══════════════════════════════════════════════════════════════════════
    public static class Profile {
        public String name;

        // ── Sentinel ──────────────────────────────────────────────────────
        public boolean sentinelEnabled     = true;
        public int     sentinelSensitivity = 2;        // 1=strict 2=normal 3=loose
        public String  blurStyle           = "ALIAS";  // ALIAS | PIXELATE | BLACKBAR
        public boolean sentinelChat        = true;
        public boolean sentinelScoreboard  = true;
        public boolean sentinelTabList     = true;
        public boolean sentinelNametags    = true;
        public boolean sentinelF3          = true;
        public boolean sentinelAdvancements= true;
        public boolean sentinelDeathScreen = true;
        public boolean sentinelScreenshots = true;

        // ── Watchlist (extra names to protect) ───────────────────────────
        public List<WatchlistEntry> watchlist = new ArrayList<>();

        // ── Identity ──────────────────────────────────────────────────────
        public boolean useAlias            = true;
        public String  alias               = "Player";
        public boolean hideOwnNametag      = false;
        public boolean showWatermark       = false;
        public String  watermarkText       = "";
        public String  watermarkPosition   = "TOP_RIGHT"; // TOP_LEFT | TOP_RIGHT | BOTTOM_LEFT | BOTTOM_RIGHT
        public float   watermarkOpacity    = 1.0f;

        // ── Network ───────────────────────────────────────────────────────
        public boolean hideServerIp        = true;
        public int ipRevealKey             = org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT_ALT;
        public boolean hideDirectConnect   = true;
        public boolean hideF3Ip            = true;
        public boolean wipeSessionLog      = false;

        // ── Coordinates ───────────────────────────────────────────────────
        public boolean hideCoordinates     = false;
        public boolean fakeCoordinates     = false;
        public int     fakeX               = 0;
        public int     fakeY               = 64;
        public int     fakeZ               = 0;

        // ── Tab List ──────────────────────────────────────────────────────
        public boolean maskOwnTabEntry     = true;
        public boolean playerCountOnly     = false;
        public boolean hideTabList         = false;

        // ── Scoreboard ────────────────────────────────────────────────────
        public boolean hideScoreboard      = false;
        public boolean blurScoreboard      = false;
        public boolean maskScoreboardNames = true;

        // ── Chat ──────────────────────────────────────────────────────────
        public boolean suppressDMs         = false;
        public boolean removeMentionPing   = true;
        public boolean disableChatLog      = false;

        // ── Screen ────────────────────────────────────────────────────────
        public boolean screenshotGuard     = true;
        public boolean manualBlurEnabled   = true;
        public float   manualBlurOpacity   = 0.7f;

        // ── Stream Mode ───────────────────────────────────────────────────
        public boolean streamModeEnabled   = false;
        public boolean autoEnablePublic    = false;

        // ── Hotkeys ───────────────────────────────────────────────────────
        public String  hotkeyMode          = "COMBO"; // SINGLE | COMBO
        public boolean peekMode            = true;

        // ── Constructor ───────────────────────────────────────────────────
        public Profile(String name) {
            this.name = name;
        }

        /** True when stream mode is active AND sentinel is enabled. */
        public boolean sentinelActive() {
            return streamModeEnabled && sentinelEnabled;
        }

        public Profile copyAs(String newName) {
            Gson g = new Gson();
            Profile copy = g.fromJson(g.toJson(this), Profile.class);
            copy.name = newName;
            return copy;
        }
    }

    // ── Watchlist entry ────────────────────────────────────────────────────
    public static class WatchlistEntry {
        public String ign;
        public String alias;
        public boolean enabled;

        public WatchlistEntry(String ign, String alias) {
            this.ign     = ign;
            this.alias   = alias;
            this.enabled = true;
        }
    }
}