package com.lwkslick.ghostmode.client.sentinel;

import com.lwkslick.ghostmode.client.config.GhostModeConfig;
import net.minecraft.client.MinecraftClient;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class SentinelManager {

    // ── Protected name list ────────────────────────────────────────────────

    public static List<String> getProtectedNames() {
        List<String> names = new ArrayList<>();
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.getSession() != null) {
            String ign = mc.getSession().getUsername();
            if (ign != null && !ign.isBlank()) names.add(ign);
        }
        GhostModeConfig.Profile p = GhostModeConfig.get().getActiveProfile();
        for (GhostModeConfig.WatchlistEntry e : p.watchlist) {
            if (e.enabled && e.ign != null && !e.ign.isBlank())
                names.add(e.ign);
        }
        return names;
    }

    // ── Public API ─────────────────────────────────────────────────────────

    /**
     * True if `input` (a single word / name token) matches any protected name
     * at the current sensitivity level.
     */
    public static boolean isProtected(String input) {
        if (input == null || input.isBlank()) return false;
        GhostModeConfig.Profile p = GhostModeConfig.get().getActiveProfile();
        if (!p.sentinelEnabled) return false;
        String lower = input.toLowerCase();
        for (String name : getProtectedNames()) {
            if (name == null || name.isBlank()) continue;
            if (tokenMatch(lower, name.toLowerCase(), p.sentinelSensitivity))
                return true;
        }
        return false;
    }

    /**
     * True if the full sentence contains any protected name token.
     * Use this for chat lines, scoreboard rows, etc.
     */
    public static boolean sentenceContainsProtected(String sentence) {
        if (sentence == null || sentence.isBlank()) return false;
        GhostModeConfig.Profile p = GhostModeConfig.get().getActiveProfile();
        if (!p.sentinelEnabled) return false;
        String[] words = sentence.split("[\\s\\W]+");
        for (String word : words) {
            if (isProtected(word)) return true;
        }
        return false;
    }

    /**
     * Replace every occurrence of each protected name in `text` with the
     * correct alias. Case-insensitive, whole-word boundary aware.
     */
    public static String scrub(String text) {
        if (text == null) return null;
        GhostModeConfig.Profile p = GhostModeConfig.get().getActiveProfile();
        if (!p.sentinelEnabled) return text;
        for (String name : getProtectedNames()) {
            if (name == null || name.isBlank()) continue;
            String alias = getAliasForName(name);
            // \b won't work for MC names with underscores — use lookaround instead
            String pattern = "(?i)(?<![\\w])(" + Pattern.quote(name) + ")(?![\\w])";
            text = text.replaceAll(pattern, alias);
        }
        return text;
    }

    /**
     * Returns the alias for a given protected name.
     * Checks watchlist per-entry alias first, falls back to global alias.
     */
    public static String getAliasForName(String name) {
        GhostModeConfig.Profile p = GhostModeConfig.get().getActiveProfile();
        for (GhostModeConfig.WatchlistEntry e : p.watchlist) {
            if (e.enabled && name.equalsIgnoreCase(e.ign))
                return (e.alias != null && !e.alias.isBlank()) ? e.alias : p.alias;
        }
        return p.alias;
    }

    // ── Matching logic ─────────────────────────────────────────────────────

    /**
     * Match a single token against a protected name.
     *
     * Sensitivity levels:
     *   1 = STRICT   — exact match only (case-insensitive)
     *   2 = NORMAL   — exact OR Levenshtein distance ≤ 1 (catches one typo / substitution)
     *   3 = LOOSE    — exact OR Levenshtein distance ≤ 2 (catches obfuscation like Lwk5lick)
     */
    public static boolean tokenMatch(String token, String name, int sensitivity) {
        if (token.equals(name)) return true;
        return switch (sensitivity) {
            case 1 -> false; // strict: exact only
            case 2 -> editDistance(token, name) <= 1;
            case 3 -> editDistance(token, name) <= 2;
            default -> false;
        };
    }

    // ── Levenshtein ────────────────────────────────────────────────────────

    public static int editDistance(String a, String b) {
        // Short-circuit: length difference already exceeds max threshold
        if (Math.abs(a.length() - b.length()) > 3) return 99;
        int[][] dp = new int[a.length() + 1][b.length() + 1];
        for (int i = 0; i <= a.length(); i++) dp[i][0] = i;
        for (int j = 0; j <= b.length(); j++) dp[0][j] = j;
        for (int i = 1; i <= a.length(); i++)
            for (int j = 1; j <= b.length(); j++)
                dp[i][j] = a.charAt(i - 1) == b.charAt(j - 1)
                        ? dp[i - 1][j - 1]
                        : 1 + Math.min(dp[i - 1][j - 1], Math.min(dp[i - 1][j], dp[i][j - 1]));
        return dp[a.length()][b.length()];
    }
}