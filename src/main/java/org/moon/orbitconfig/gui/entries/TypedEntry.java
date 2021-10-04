package org.moon.orbitconfig.gui.entries;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import org.moon.orbitconfig.config.ConfigEntry;
import org.moon.orbitconfig.config.ConfigObject;
import org.moon.orbitconfig.gui.ConfigScreen;

import java.lang.reflect.Field;

@Environment(EnvType.CLIENT)
public abstract class TypedEntry<T> extends Entry {

    protected final ConfigObject config;
    protected final ConfigEntry entry;
    protected final T initialValue;

    protected final ButtonWidget reset;

    public TypedEntry(ConfigScreen parent, ConfigObject config, Field f, Text display, Text tooltip) {
        super(parent, display, tooltip);
        this.config = config;
        this.entry = this.config.getEntry(f);
        this.initialValue = (T) this.entry.get();

        this.reset = new ButtonWidget(0, 0, 50, 20, new TranslatableText("controls.reset"), this::onReset);
    }

    @Override
    public void render(MatrixStack matrices, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
        renderReset(matrices, x, y, mouseX, mouseY, tickDelta);
        super.render(matrices, index, y, x, entryWidth, entryHeight, mouseX, mouseY, hovered, tickDelta);
    }

    protected void renderReset(MatrixStack matrices, int x, int y, int mouseX, int mouseY, float tickDelta) {
        this.reset.x = x + 250;
        this.reset.y = y;
        this.reset.active = !entry.isDefaultValue();
        this.reset.render(matrices, mouseX, mouseY, tickDelta);
    }

    protected void onReset(ButtonWidget button) {
        entry.restoreDefaultValue();
    }
}
