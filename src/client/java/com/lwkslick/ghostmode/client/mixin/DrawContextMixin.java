package com.lwkslick.ghostmode.client.mixin;

import com.lwkslick.ghostmode.client.UsernameAliasHelper;
import com.lwkslick.ghostmode.client.config.GhostModeConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(DrawContext.class)
public class DrawContextMixin {

    private String ghostmode$alias(String raw) {
        GhostModeConfig cfg = GhostModeConfig.get();
        if (cfg == null || !cfg.enabled || !cfg.hideUsername) return null;
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null) return null;
        String name = mc.player.getName().getString();
        String alias = (cfg.usernameAlias == null || cfg.usernameAlias.isBlank()) ? "Streamer" : cfg.usernameAlias;
        String result = UsernameAliasHelper.replace(raw, name, alias, cfg.fuzzyHideUsername);
        return result.equals(raw) ? null : result;
    }

    @ModifyVariable(method = "drawText(Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;IIIZ)V", at = @At("HEAD"), argsOnly = true, index = 2)
    private String ghostmode$str(String t) { String r = ghostmode$alias(t); return r != null ? r : t; }

    @ModifyVariable(method = "drawText(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/Text;IIIZ)V", at = @At("HEAD"), argsOnly = true, index = 2)
    private Text ghostmode$text(Text t) { String r = ghostmode$alias(t.getString()); return r != null ? Text.literal(r) : t; }

    @ModifyVariable(method = "drawTextWithShadow(Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;III)V", at = @At("HEAD"), argsOnly = true, index = 2)
    private String ghostmode$shadowStr(String t) { String r = ghostmode$alias(t); return r != null ? r : t; }

    @ModifyVariable(method = "drawTextWithShadow(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/Text;III)V", at = @At("HEAD"), argsOnly = true, index = 2)
    private Text ghostmode$shadowText(Text t) { String r = ghostmode$alias(t.getString()); return r != null ? Text.literal(r) : t; }

    @ModifyVariable(method = "drawTextWithShadow(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/OrderedText;III)V", at = @At("HEAD"), argsOnly = true, index = 2)
    private OrderedText ghostmode$shadowOrdered(OrderedText t) {
        GhostModeConfig cfg = GhostModeConfig.get();
        if (cfg == null || !cfg.enabled || !cfg.hideUsername) return t;
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null) return t;
        String name = mc.player.getName().getString();
        String alias = (cfg.usernameAlias == null || cfg.usernameAlias.isBlank()) ? "Streamer" : cfg.usernameAlias;
        StringBuilder sb = new StringBuilder();
        t.accept((index, style, codePoint) -> { sb.appendCodePoint(codePoint); return true; });
        String raw = sb.toString();
        String replaced = UsernameAliasHelper.replace(raw, name, alias, cfg.fuzzyHideUsername);
        return replaced.equals(raw) ? t : Text.literal(replaced).asOrderedText();
    }
}