package com.lwkslick.ghostmode.client.mixin;

import com.lwkslick.ghostmode.client.config.GhostModeConfig;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.multiplayer.DirectConnectScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DirectConnectScreen.class)
public abstract class DirectConnectScreenMixin extends Screen {

    protected DirectConnectScreenMixin() { super(Text.empty()); }

    @Shadow private TextFieldWidget addressField;

    @Unique private static final Identifier LOCKED =
            Identifier.of("minecraft", "textures/gui/sprites/widget/locked_button.png");
    @Unique private static final Identifier LOCKED_HOVER =
            Identifier.of("minecraft", "textures/gui/sprites/widget/locked_button_highlighted.png");
    @Unique private static final Identifier UNLOCKED =
            Identifier.of("minecraft", "textures/gui/sprites/widget/unlocked_button.png");
    @Unique private static final Identifier UNLOCKED_HOVER =
            Identifier.of("minecraft", "textures/gui/sprites/widget/unlocked_button_highlighted.png");

    @Unique private boolean ghostmode$revealed = false;
    @Unique private String ghostmode$storedReal = null;
    @Unique private boolean ghostmode$didMask = false;

    @Inject(method = "init", at = @At("TAIL"))
    private void onInit(CallbackInfo ci) {
        ghostmode$revealed = false;
        if (!GhostModeConfig.get().enabled || !GhostModeConfig.get().hideDirectConnect) return;
        if (addressField == null) return;

        int iconSize = addressField.getHeight();
        int iconX = addressField.getX() + addressField.getWidth() + 2;
        int iconY = addressField.getY();

        this.addDrawableChild(ButtonWidget.builder(Text.empty(), btn ->
                        ghostmode$revealed = !ghostmode$revealed)
                .dimensions(iconX, iconY, iconSize, iconSize).build());
    }

    @Inject(method = "render", at = @At("HEAD"))
    private void beforeRender(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (!GhostModeConfig.get().enabled || !GhostModeConfig.get().hideDirectConnect) return;
        if (ghostmode$revealed || addressField == null) return;

        String real = addressField.getText();
        if (!real.isEmpty()) {
            int cursor = addressField.getCursor();
            addressField.setText("•".repeat(real.length()));
            addressField.setCursor(cursor, false);
            ghostmode$storedReal = real;
            ghostmode$didMask = true;
        }
    }

    @Inject(method = "render", at = @At("RETURN"))
    private void afterRender(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (!GhostModeConfig.get().enabled || !GhostModeConfig.get().hideDirectConnect) return;
        if (addressField == null) return;

        if (ghostmode$didMask && ghostmode$storedReal != null) {
            int cursor = addressField.getCursor();
            addressField.setText(ghostmode$storedReal);
            addressField.setCursor(cursor, false);
            ghostmode$storedReal = null;
            ghostmode$didMask = false;
        }

        int iconSize = addressField.getHeight();
        int iconX = addressField.getX() + addressField.getWidth() + 2;
        int iconY = addressField.getY();
        boolean hovered = mouseX >= iconX && mouseX <= iconX + iconSize
                && mouseY >= iconY && mouseY <= iconY + iconSize;

        Identifier icon = ghostmode$revealed
                ? (hovered ? UNLOCKED_HOVER : UNLOCKED)
                : (hovered ? LOCKED_HOVER : LOCKED);

        context.drawTexture(RenderPipelines.GUI_TEXTURED, icon,
                iconX, iconY, 0, 0, iconSize, iconSize, iconSize, iconSize);
    }
}