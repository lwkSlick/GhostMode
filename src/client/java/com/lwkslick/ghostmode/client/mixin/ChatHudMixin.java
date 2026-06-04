package com.lwkslick.ghostmode.client.mixin;

import com.lwkslick.ghostmode.client.UsernameAliasHelper;
import com.lwkslick.ghostmode.client.config.GhostModeConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ChatHud.class)
public class ChatHudMixin {

    @ModifyVariable(method = "addMessage(Lnet/minecraft/text/Text;Lnet/minecraft/network/message/MessageSignatureData;Lnet/minecraft/client/gui/hud/MessageIndicator;)V",
            at = @At("HEAD"), argsOnly = true, index = 1)
    private Text ghostmode$replaceChat(Text message) {
        GhostModeConfig cfg = GhostModeConfig.get();
        if (cfg == null || !cfg.enabled || !cfg.hideUsername) return message;
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null) return message;
        String name = mc.player.getName().getString();
        String alias = (cfg.usernameAlias == null || cfg.usernameAlias.isBlank()) ? "Streamer" : cfg.usernameAlias;
        String raw = message.getString();
        String replaced = UsernameAliasHelper.replace(raw, name, alias, cfg.fuzzyHideUsername);
        return replaced.equals(raw) ? message : Text.literal(replaced);
    }
}