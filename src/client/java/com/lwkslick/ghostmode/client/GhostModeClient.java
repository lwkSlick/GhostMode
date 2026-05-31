package com.lwkslick.ghostmode.client;

import com.lwkslick.ghostmode.client.config.GhostModeConfig;
import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.lwkslick.ghostmode.client.chat.ChatSentinel;
import com.lwkslick.ghostmode.client.config.GhostModeConfigScreen;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
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

		net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
			GhostModeConfig.Profile prof = GhostModeConfig.get().getActiveProfile();
			if ((prof.alias.equals("Player") || prof.alias.isBlank()) && client.player != null) {
				String realName = client.player.getName().getString();
				if (!realName.isBlank()) {
					prof.alias = realName;
					GhostModeConfig.get().save();
				}
			}
		});

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			while (openConfigKey.wasPressed()) {
				if (client.currentScreen == null) {
					client.setScreen(GhostModeConfigScreen.create(null));
				}
			}
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
		});

		LOGGER.info("GhostMode client initialized. Active profile: "
				+ GhostModeConfig.get().activeProfileName);
	}
}