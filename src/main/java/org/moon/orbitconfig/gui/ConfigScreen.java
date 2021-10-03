package org.moon.orbitconfig.gui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.TranslatableText;
import org.moon.orbitconfig.config.ConfigManager;
import org.moon.orbitconfig.config.ConfigObject;

public abstract class ConfigScreen extends Screen {

    public final Screen parentScreen;

    protected final ConfigObject config;

    protected ConfigListWidget configListWidget;

    protected ConfigScreen(Screen parentScreen, ConfigObject config) {
        super(config.makeText("configTitle"));
        this.parentScreen = parentScreen;
        this.config = config;
        ConfigManager.backupValues(config, false);
    }

    @Override
    protected void init() {
        super.init();

        this.addDrawableChild(new ButtonWidget(this.width / 2 - 154, this.height - 29, 150, 20, new TranslatableText("gui.cancel"), (buttonWidgetx) -> {
            ConfigManager.revertChanges(config);
            this.client.setScreen(parentScreen);
        }));

        this.addDrawableChild(new ButtonWidget(this.width / 2 + 4, this.height - 29, 150, 20, new TranslatableText("gui.done"), (buttonWidgetx) -> {
            ConfigManager.save(config);
            this.client.setScreen(parentScreen);
        }));

        this.configListWidget = new ConfigListWidget(this, config);
        this.addSelectableChild(this.configListWidget);
    }

    @Override
    public void onClose() {
        ConfigManager.save(config);
        this.client.setScreen(parentScreen);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float delta) {
        this.renderBackgroundTexture(0);

        this.configListWidget.render(matrixStack, mouseX, mouseY, delta);

        super.render(matrixStack, mouseX, mouseY, delta);

        drawCenteredText(matrixStack, this.textRenderer, this.title, this.width/2, 12, 16777215);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (configListWidget.focusedBinding != null) {
            configListWidget.focusedBinding.setBoundKey(InputUtil.Type.MOUSE.createFromCode(button));
            configListWidget.focusedBinding = null;

            KeyBinding.updateKeysByCode();

            return true;
        } else {
            return super.mouseClicked(mouseX, mouseY, button);
        }
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (configListWidget.focusedBinding != null) {
            configListWidget.focusedBinding.setBoundKey(keyCode == 256 ? InputUtil.UNKNOWN_KEY: InputUtil.fromKeyCode(keyCode, scanCode));
            configListWidget.focusedBinding = null;

            KeyBinding.updateKeysByCode();

            return true;
        }
        else {
            return super.keyPressed(keyCode, scanCode, modifiers);
        }
    }

    public MinecraftClient getClient() {
        return this.client;
    }
}
