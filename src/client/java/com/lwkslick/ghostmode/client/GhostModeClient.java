package com.lwkslick.ghostmode.client;

import com.lwkslick.ghostmode.client.config.GhostModeConfig;
import com.lwkslick.ghostmode.client.config.GhostModeConfigScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GhostModeClient implements ClientModInitializer {

    public static final String MOD_ID = "ghostmode";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static KeyBinding openConfigKey;

    @Override
    public void onInitializeClient() {
        GhostModeConfig.load();

        KeyBinding.Category category = new KeyBinding.Category(
                net.minecraft.util.Identifier.of("ghostmode", "keys")
        );

        openConfigKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.ghostmode.open_config",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_J,
                category
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openConfigKey.wasPressed()) {
                if (client.currentScreen == null) {
                    client.setScreen(GhostModeConfigScreen.create(null));
                }
            }
        });

        LOGGER.info("GhostMode client initialized.");
    }
}