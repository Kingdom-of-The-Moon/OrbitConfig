package org.moon.orbitconfig.gui.entries;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.moon.orbitconfig.gui.ConfigListWidget;
import org.moon.orbitconfig.gui.ConfigScreen;

import java.util.Collections;
import java.util.List;

public class CategoryEntry extends Entry {
    //properties
    private final Text text;

    public CategoryEntry(ConfigScreen parent, Text text) {
        super(parent);
        this.text = text;
    }

    @Override
    public void render(MatrixStack matrices, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
        //render text
        TextRenderer textRenderer = this.client.textRenderer;
        Text text = this.text;
        int textWidth = this.client.textRenderer.getWidth(this.text);
        float xPos = (float)(this.client.currentScreen.width / 2 - textWidth / 2);
        int yPos = y + entryHeight;
        textRenderer.draw(matrices, text, xPos, (float)(yPos - 9 - 1), 16777215);
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
