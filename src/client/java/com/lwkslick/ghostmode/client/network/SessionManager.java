package com.lwkslick.ghostmode.client.network;

import com.lwkslick.ghostmode.client.GhostModeClient;
import com.lwkslick.ghostmode.client.config.GhostModeConfig;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ServerAddress;
import net.minecraft.client.network.ServerInfo;

import java.nio.file.*;
import java.io.IOException;

public class SessionManager {

    public static void register() {
        // Session log wipe on disconnect
        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
            if (GhostModeConfig.get().getActiveProfile().wipeSessionLog) {
                wipeSessionLog(client);
            }
        });

        // Stream mode auto-enable on public server join
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            GhostModeConfig.Profile p = GhostModeConfig.get().getActiveProfile();
            if (!p.autoEnablePublic) return;

            ServerInfo info = client.getCurrentServerEntry();
            if (info != null && !isLanServer(info.address)) {
                if (!p.streamModeEnabled) {
                    p.streamModeEnabled = true;
                    GhostModeConfig.get().save();
                    GhostModeClient.LOGGER.info("GhostMode: Stream mode auto-enabled (public server)");
                }
            }
        });
    }

    private static boolean isLanServer(String address) {
        if (address == null) return false;
        String host = address.split(":")[0].trim().toLowerCase();
        return host.equals("localhost")
                || host.startsWith("192.168.")
                || host.startsWith("10.")
                || host.startsWith("172.")
                || host.equals("127.0.0.1");
    }

    private static void wipeSessionLog(MinecraftClient client) {
        try {
            Path logDir = net.fabricmc.loader.api.FabricLoader.getInstance()
                    .getGameDir().resolve("logs");
            Path latest = logDir.resolve("latest.log");
            if (Files.exists(latest)) {
                Files.writeString(latest, "", java.nio.file.StandardOpenOption.TRUNCATE_EXISTING);
                GhostModeClient.LOGGER.info("GhostMode: Session log wiped.");
            }
        } catch (IOException e) {
            GhostModeClient.LOGGER.warn("GhostMode: Could not wipe session log: " + e.getMessage());
        }
    }
}