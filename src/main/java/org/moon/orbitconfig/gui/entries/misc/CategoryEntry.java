package org.moon.orbitconfig.gui.entries.misc;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.moon.orbitconfig.gui.ConfigScreen;
import org.moon.orbitconfig.gui.entries.Entry;

import java.util.Collections;
import java.util.List;

@Environment(EnvType.CLIENT)
public class CategoryEntry extends Entry {
    public CategoryEntry(ConfigScreen parent, Text display, Text tooltip) {
        super(parent, display, tooltip);
    }

    @Override
    public void renderEntryName(MatrixStack matrices, int y, int x, int entryHeight) {
        TextRenderer textRenderer = this.client.textRenderer;
        int textWidth = this.client.textRenderer.getWidth(this.display);
        float xPos = (float)(this.client.currentScreen.width / 2 - textWidth / 2);
        int yPos = y + entryHeight;
        textRenderer.draw(matrices, this.display, xPos, (float)(yPos - 9 - 1), 16777215);
    }

    @Override
    public boolean changeFocus(boolean lookForwards) {
        return false;
    }

    @Override
    public List<? extends Element> children() {
        return Collections.emptyList();
    }

    @Override
    public List<? extends Selectable> selectableChildren() {
        return Collections.emptyList();
    }
}
