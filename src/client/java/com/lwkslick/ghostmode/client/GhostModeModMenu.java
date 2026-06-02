package com.lwkslick.ghostmode.client;

import com.lwkslick.ghostmode.client.config.GhostModeConfigScreen;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

public class GhostModeModMenu implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return GhostModeConfigScreen::create;
    }
}