package com.lwkslick.ghostmode.client.chat;

import com.lwkslick.ghostmode.client.config.GhostModeConfig;
import com.lwkslick.ghostmode.client.sentinel.SentinelManager;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class ChatSentinel {

    public static void register() {

        // ── Sentinel scrub (player chat) ──────────────────────────────────
        ClientReceiveMessageEvents.ALLOW_CHAT.register((message, signedMessage, sender, params, receptionTimestamp) -> {
            GhostModeConfig.Profile p = GhostModeConfig.get().getActiveProfile();
            if (!p.sentinelActive() || !p.sentinelChat) return true;

            String raw = message.getString();
            if (p.suppressDMs && isDM(raw)) return false;

            if (SentinelManager.sentenceContainsProtected(raw)) {
                String scrubbed = SentinelManager.scrub(raw);
                MinecraftClient mc = MinecraftClient.getInstance();
                if (mc.inGameHud != null) {
                    mc.inGameHud.getChatHud().addMessage(Text.literal(scrubbed));
                }
                return false;
            }
            return true;
        });

        // ── Sentinel scrub (system/game messages) ─────────────────────────
        ClientReceiveMessageEvents.ALLOW_GAME.register((message, overlay) -> {
            GhostModeConfig.Profile p = GhostModeConfig.get().getActiveProfile();
            if (!p.sentinelActive() || !p.sentinelChat) return true;
            String raw = message.getString();
            if (p.suppressDMs && isDM(raw)) return false;
            return true;
        });

        ClientReceiveMessageEvents.MODIFY_GAME.register((message, overlay) -> {
            GhostModeConfig.Profile p = GhostModeConfig.get().getActiveProfile();
            if (!p.sentinelActive() || !p.sentinelChat) return message;
            String raw = message.getString();
            String scrubbed = SentinelManager.scrub(raw);
            if (scrubbed.equals(raw)) return message;
            return Text.literal(scrubbed);
        });

        // ── Mention interceptor ───────────────────────────────────────────
        ClientReceiveMessageEvents.ALLOW_CHAT.register((message, signedMessage, sender, params, receptionTimestamp) -> {
            GhostModeConfig.Profile p = GhostModeConfig.get().getActiveProfile();
            if (!p.removeMentionPing) return true;
            // If sentinel is active on chat, it already scrubbed and re-injected — don't touch
            if (p.sentinelActive() && p.sentinelChat) return true;
            MinecraftClient mc = MinecraftClient.getInstance();
            if (mc.player == null) return true;
            String raw = message.getString();
            String ign = mc.player.getName().getString().toLowerCase();
            if (raw.toLowerCase().contains(ign)) {
                // Re-add silently without triggering the ping/flash, then block original
                if (mc.inGameHud != null) {
                    mc.inGameHud.getChatHud().addMessage(message);
                }
                return false;
            }
            return true;
        });

        // ── Disable chat log to disk ───────────────────────────────────────
        ClientReceiveMessageEvents.ALLOW_CHAT.register((message, signedMessage, sender, params, receptionTimestamp) -> {
            GhostModeConfig.Profile p = GhostModeConfig.get().getActiveProfile();
            if (!p.disableChatLog) return true;
            MinecraftClient mc = MinecraftClient.getInstance();
            if (mc.inGameHud != null) {
                mc.inGameHud.getChatHud().addMessage(message);
            }
            return false;
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