package com.lwkslick.ghostmode.client.network;

import com.lwkslick.ghostmode.client.config.GhostModeConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.client.option.ServerList;

import java.util.HashMap;
import java.util.Map;

public class IpMasker {

    public static final String MASK = "••••••••••";
    private static final Map<String, String> originals = new HashMap<>();
    private static boolean masked = false;

    public static void tick(MinecraftClient client) {
        if (client.currentScreen instanceof MultiplayerScreen mp) {
            if (shouldMask()) {
                applyMask(mp.getServerList());
            }
        } else if (masked) {
            // Screen closed — originals are already restored by close logic,
            // but clear state just in case
            originals.clear();
            masked = false;
        }
    }

    private static void applyMask(ServerList list) {
        if (masked) return; // already applied this open session
        for (int i = 0; i < list.size(); i++) {
            ServerInfo info = list.get(i);
            originals.put(info.name, info.address);
            info.address = MASK;
        }
        masked = true;
    }

    public static void onScreenClose(ServerList list) {
        if (!masked) return;
        for (int i = 0; i < list.size(); i++) {
            ServerInfo info = list.get(i);
            String orig = originals.get(info.name);
            if (orig != null) info.address = orig;
        }
        originals.clear();
        masked = false;
    }

    public static boolean shouldMask() {
        return GhostModeConfig.get().getActiveProfile().hideServerIp;
    }
}