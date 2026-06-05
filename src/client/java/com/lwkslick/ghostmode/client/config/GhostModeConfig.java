package com.lwkslick.ghostmode.client.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.*;
import java.nio.file.*;

public class GhostModeConfig {

    private static GhostModeConfig INSTANCE;
    public static GhostModeConfig get() { return INSTANCE; }

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH =
            FabricLoader.getInstance().getConfigDir().resolve("ghostmode.json");

    public boolean enabled            = false;
    public boolean hideServerIp       = false;
    public boolean hideDirectConnect  = false;
    public boolean hideUsername       = false;
    public String  usernameAlias      = "Streamer";
    public boolean hideF3             = false;
    public boolean hideF3Location     = false;
    public boolean hideF3Biome        = false;
    public boolean hideF3LookingAt    = false;
    public boolean hideF3Memory       = false;
    public boolean hideF3SystemSpecs  = false;
    public boolean watermarkEnabled   = false;
    public String  watermarkText      = "YourChannel";
    public java.awt.Color watermarkColor = new java.awt.Color(255, 255, 255, 255);
    public int     watermarkOpacity   = 255;
    public String  watermarkPosition  = "ACTION_BAR";
    public boolean fakeCoords         = false;
    public int     fakeCoordsInterval = 1;
    public boolean hideTabList        = false;
    public boolean tabCountOnly       = false;
    public int     fakePing           = -1;

    public static void load() {
        if (Files.exists(CONFIG_PATH)) {
            try (Reader r = Files.newBufferedReader(CONFIG_PATH)) {
                INSTANCE = GSON.fromJson(r, GhostModeConfig.class);
                if (INSTANCE == null) INSTANCE = new GhostModeConfig();
                if (INSTANCE.watermarkPosition == null) INSTANCE.watermarkPosition = "ACTION_BAR";
                if (INSTANCE.usernameAlias == null) INSTANCE.usernameAlias = "Streamer";
                if (INSTANCE.watermarkText == null) INSTANCE.watermarkText = "YourChannel";
                if (INSTANCE.watermarkColor == null) INSTANCE.watermarkColor = new java.awt.Color(255, 255, 255);
            } catch (Exception e) {
                INSTANCE = new GhostModeConfig();
            }
        } else {
            INSTANCE = new GhostModeConfig();
        }
        INSTANCE.save();
    }

    public void save() {
        try (Writer w = Files.newBufferedWriter(CONFIG_PATH)) {
            GSON.toJson(this, w);
        } catch (Exception ignored) {}
    }
}