package com.lwkslick.ghostmode.client;

import com.lwkslick.ghostmode.client.config.GhostModeConfig;
import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.lwkslick.ghostmode.client.chat.ChatSentinel;

public class GhostModeClient implements ClientModInitializer {

	public static final String MOD_ID = "ghostmode";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitializeClient() {
		GhostModeConfig.load();
		ChatSentinel.register();
		LOGGER.info("GhostMode client initialized. Active profile: "
				+ GhostModeConfig.get().activeProfileName);
	}
}