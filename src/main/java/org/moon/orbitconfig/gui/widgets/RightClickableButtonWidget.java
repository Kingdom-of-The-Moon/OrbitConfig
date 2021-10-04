package org.moon.orbitconfig.gui.widgets;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;

@Environment(EnvType.CLIENT)
public class RightClickableButtonWidget extends ButtonWidget {

    private final PressAction onRightPress;

    public RightClickableButtonWidget(int x, int y, int width, int height, Text message, PressAction onPress, PressAction onRightPress) {
        super(x, y, width, height, message, onPress);
        this.onRightPress = onRightPress;
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        super.onClick(mouseX, mouseY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (this.isMouseOver(mouseX, mouseY)) {
            if (button == 1) {
                onRightPress.onPress(this);
                MinecraftClient.getInstance().getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 0.95F));
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }
}
