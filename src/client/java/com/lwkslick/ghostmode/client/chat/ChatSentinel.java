package com.lwkslick.ghostmode.client.chat;

import com.lwkslick.ghostmode.client.config.GhostModeConfig;
import com.lwkslick.ghostmode.client.sentinel.SentinelManager;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class ChatSentinel {

    public static void register() {

        // ── Player chat (ALLOW_CHAT + re-inject scrubbed) ──────────────────
        // MODIFY_CHAT does not exist in this API version.
        // Strategy: block the original, inject scrubbed version manually.
        ClientReceiveMessageEvents.ALLOW_CHAT.register((message, signedMessage, sender, params, receptionTimestamp) -> {
            GhostModeConfig.Profile p = GhostModeConfig.get().getActiveProfile();
            if (!p.sentinelEnabled || !p.sentinelChat) return true;

            String raw = message.getString();

            // Suppress DMs entirely
            if (p.suppressDMs && isDM(raw)) return false;

            // If name is present, block original and re-inject scrubbed
            if (SentinelManager.sentenceContainsProtected(raw)) {
                String scrubbed = SentinelManager.scrub(raw);
                MinecraftClient mc = MinecraftClient.getInstance();
                if (mc.inGameHud != null) {
                    mc.inGameHud.getChatHud().addMessage(Text.literal(scrubbed));
                }
                return false; // block the original
            }

            return true;
        });

        // ── System/game messages (ALLOW_GAME + MODIFY_GAME) ───────────────
        ClientReceiveMessageEvents.ALLOW_GAME.register((message, overlay) -> {
            GhostModeConfig.Profile p = GhostModeConfig.get().getActiveProfile();
            if (!p.sentinelEnabled || !p.sentinelChat) return true;

            String raw = message.getString();
            if (p.suppressDMs && isDM(raw)) return false;

            return true;
        });

        ClientReceiveMessageEvents.MODIFY_GAME.register((message, overlay) -> {
            GhostModeConfig.Profile p = GhostModeConfig.get().getActiveProfile();
            if (!p.sentinelEnabled || !p.sentinelChat) return message;

            String raw = message.getString();
            String scrubbed = SentinelManager.scrub(raw);
            if (scrubbed.equals(raw)) return message;

            return Text.literal(scrubbed);
        });

        // ── Mention interceptor — block the chat notification/sound ───────
        ClientReceiveMessageEvents.ALLOW_CHAT.register((message, signedMessage, sender, params, receptionTimestamp) -> {
            GhostModeConfig.Profile p = GhostModeConfig.get().getActiveProfile();
            if (!p.removeMentionPing) return true;

            MinecraftClient mc = MinecraftClient.getInstance();
            if (mc.player == null) return true;

            String raw = message.getString();
            String ign = mc.player.getName().getString().toLowerCase();

            // If message contains our name, re-inject without triggering the ping sound/highlight
            if (raw.toLowerCase().contains(ign)) {
                if (mc.inGameHud != null) {
                    mc.inGameHud.getChatHud().addMessage(message);
                }
                return false; // block the original which would trigger ping
            }
            return true;
        });

        // ── Disable chat log to disk ───────────────────────────────────────
        ClientReceiveMessageEvents.ALLOW_CHAT.register((message, signedMessage, sender, params, receptionTimestamp) -> {
            GhostModeConfig.Profile p = GhostModeConfig.get().getActiveProfile();
            if (!p.disableChatLog) return true;

            // Re-inject message visually but skip the log path by blocking then re-adding
            MinecraftClient mc = MinecraftClient.getInstance();
            if (mc.inGameHud != null) {
                mc.inGameHud.getChatHud().addMessage(message);
            }
            return false; // block original (which would be logged), re-added above without log
        });

        ClientReceiveMessageEvents.ALLOW_GAME.register((message, overlay) -> {
            GhostModeConfig.Profile p = GhostModeConfig.get().getActiveProfile();
            if (!p.disableChatLog || overlay) return true;

            MinecraftClient mc = MinecraftClient.getInstance();
            if (mc.inGameHud != null) {
                mc.inGameHud.getChatHud().addMessage(message);
            }
            return false;
        });
    }

    // ── DM detection ───────────────────────────────────────────────────────
    private static boolean isDM(String message) {
        if (message == null) return false;
        String lower = message.toLowerCase();
        return lower.contains("-> you") ||
                lower.contains("→ you") ||
                lower.contains("whispers") ||
                (lower.contains("from ") && lower.indexOf("from ") < 20) ||
                lower.contains("msg from") ||
                lower.contains("(msg)") ||
                lower.contains("[pm]") ||
                lower.contains("[dm]") ||
                lower.contains("[whisper]");
    }
}