package com.lwkslick.ghostmode.client;

import com.lwkslick.ghostmode.client.config.GhostModeConfig;
import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.lwkslick.ghostmode.client.chat.ChatSentinel;
import com.lwkslick.ghostmode.client.config.GhostModeConfigScreen;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import com.lwkslick.ghostmode.client.hud.WatermarkHud;
import com.lwkslick.ghostmode.client.network.SessionManager;

public class GhostModeClient implements ClientModInitializer {

	public static final String MOD_ID = "ghostmode";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static KeyBinding openConfigKey;
	public static KeyBinding panicKey;
	public static KeyBinding peekKey;
	public static KeyBinding profileCycleKey;

	// Peek mode state — true while peek key is held
	public static boolean isPeeking = false;

	@Override
	public void onInitializeClient() {
		KeyBinding.Category ghostCategory = new KeyBinding.Category(net.minecraft.util.Identifier.of("ghostmode", "keycategory"));
		GhostModeConfig.load();
		ChatSentinel.register();
		WatermarkHud.register();
		SessionManager.register();

		openConfigKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
				"key.ghostmode.open_config",
				InputUtil.Type.KEYSYM,
				GLFW.GLFW_KEY_J,
				ghostCategory
		));

		panicKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
				"key.ghostmode.panic",
				InputUtil.Type.KEYSYM,
				GLFW.GLFW_KEY_K,
				ghostCategory
		));

		peekKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
				"key.ghostmode.peek",
				InputUtil.Type.KEYSYM,
				GLFW.GLFW_KEY_LEFT_ALT,
				ghostCategory
		));

		profileCycleKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
				"key.ghostmode.cycle_profile",
				InputUtil.Type.KEYSYM,
				GLFW.GLFW_KEY_UNKNOWN,
				ghostCategory
		));

		ClientTickEvents.END_CLIENT_TICK.register(client -> {

			// ── Config key ────────────────────────────────────────────────
			while (openConfigKey.wasPressed()) {
				if (client.currentScreen == null) {
					client.setScreen(GhostModeConfigScreen.create(null));
				}
			}

			// ── Panic key ─────────────────────────────────────────────────
			while (panicKey.wasPressed()) {
				GhostModeConfig.Profile p = GhostModeConfig.get().getActiveProfile();
				p.streamModeEnabled = true;
				p.sentinelEnabled = true;
				p.sentinelChat = true;
				p.sentinelTabList = true;
				p.sentinelNametags = true;
				p.sentinelScoreboard = true;
				p.sentinelF3 = true;
				p.maskOwnTabEntry = true;
				p.hideServerIp = true;
				p.hideF3Ip = true;
				p.useAlias = true;
				GhostModeConfig.get().save();
			}

			// ── Peek mode — held key temporarily disables all hiding ──────
			GhostModeConfig.Profile p = GhostModeConfig.get().getActiveProfile();
			if (p.peekMode && p.streamModeEnabled) {
				boolean held = InputUtil.isKeyPressed(
						client.getWindow(),
						peekKey.getDefaultKey().getCode()
				);
				isPeeking = held;
			} else {
				isPeeking = false;
			}

			// ── Profile cycle key ─────────────────────────────────────────
			while (profileCycleKey.wasPressed()) {
				GhostModeConfig cfg = GhostModeConfig.get();
				if (cfg.profiles.size() <= 1) return;
				int current = 0;
				for (int i = 0; i < cfg.profiles.size(); i++) {
					if (cfg.profiles.get(i).name.equals(cfg.activeProfileName)) {
						current = i;
						break;
					}
				}
				int next = (current + 1) % cfg.profiles.size();
				cfg.setActiveProfile(cfg.profiles.get(next).name);
				if (client.player != null) {
					client.player.sendMessage(
							net.minecraft.text.Text.literal("§8[GhostMode] §7Profile: §f" + cfg.activeProfileName),
							true
					);
				}
			}
		});

		LOGGER.info("GhostMode client initialized. Active profile: "
				+ GhostModeConfig.get().activeProfileName);
	}
}