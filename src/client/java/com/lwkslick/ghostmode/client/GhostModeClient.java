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
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.minecraft.text.Text;
import net.minecraft.client.MinecraftClient;

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

        ClientReceiveMessageEvents.MODIFY_GAME.register((message, overlay) -> {
            GhostModeConfig cfg = GhostModeConfig.get();
            if (!cfg.enabled || !cfg.hideUsername) return message;
            MinecraftClient mc = MinecraftClient.getInstance();
            if (mc.player == null) return message;
            String name = mc.player.getName().getString();
            String alias = (cfg.usernameAlias == null || cfg.usernameAlias.isBlank()) ? "Streamer" : cfg.usernameAlias;
            String raw = message.getString();
            if (!raw.contains(name)) return message;
            return Text.literal(raw.replace(name, alias));
        });

        LOGGER.info("GhostMode client initialized.");
    }
}