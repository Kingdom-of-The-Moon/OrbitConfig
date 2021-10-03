package org.moon.orbitconfig.gui.entries;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.widget.ElementListWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.moon.orbitconfig.config.ConfigObject;
import org.moon.orbitconfig.gui.ConfigScreen;

public abstract class Entry extends ElementListWidget.Entry<Entry> {
    protected final MinecraftClient client;
    protected final ConfigScreen parent;
    protected final ConfigObject config;
    protected final Text tooltip;
    protected final Text display;

    public Entry(ConfigScreen parent, ConfigObject config, Text display, Text tooltip) {
        this.parent = parent;
        this.client = parent.getClient();
        this.config = config;
        this.tooltip = tooltip;
        this.display = display;
    }

    @Override
    public void render(MatrixStack matrices, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
        renderEntryName(matrices, y, x, entryHeight);
        renderEntryTooltip(matrices, x, mouseX, mouseY);
    }

    public void renderEntryName(MatrixStack matrices, int y, int x, int entryHeight) {
        TextRenderer textRenderer = this.client.textRenderer;
        int posY = y + entryHeight / 2;
        textRenderer.draw(matrices, this.display, (float) x, (float) (posY - 9 / 2), 16777215);
    }

    public void renderEntryTooltip(MatrixStack matrices, int x, int mouseX, int mouseY) {
        if (isMouseOver(mouseX, mouseY) && mouseX < x + 165 && this.tooltip != null) {
            matrices.push();
            matrices.translate(0, 0, 599);
            parent.renderTooltip(matrices, this.tooltip, mouseX, mouseY);
            matrices.pop();
        }
    }
}