package org.moon.orbitconfig.gui.entries;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import org.moon.orbitconfig.OrbitConfigMod;
import org.moon.orbitconfig.config.ConfigEntry;
import org.moon.orbitconfig.config.ConfigObject;
import org.moon.orbitconfig.gui.ConfigScreen;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public class BooleanEntry extends Entry {
    //entry
    private final ConfigEntry entry;

    //values
    private final Text display;
    private final Text tooltip;
    private final boolean initValue;

    private final Text textEnabled;
    private final Text textDisabled;

    //buttons
    private final ButtonWidget toggle;
    private final ButtonWidget reset;

    public BooleanEntry(ConfigScreen parent, Text display, Text tooltip, Field configField, ConfigObject config) throws IllegalAccessException {
        super(parent);
        this.display = display;
        this.tooltip = tooltip;
        this.entry = config.getEntry(configField);
        this.initValue = configField.getBoolean(config.object);

        //toggle button
        this.toggle = new ButtonWidget(0, 0, 75, 20, this.display, (button) -> {
            entry.setBoolean(!entry.getBoolean());
        });
        this.textEnabled  = config.makeText(String.format("config.%s.disabled", entry.getName()));
        this.textDisabled = config.makeText(String.format("config.%s.enabled", entry.getName()));

        //reset button
        this.reset = new ButtonWidget(0, 0, 50, 20, new TranslatableText("controls.reset"), (button) -> {
            entry.set(entry.getDefaultValue());
        });
    }

    public void render(MatrixStack matrices, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
        //text
        TextRenderer textRenderer = this.client.textRenderer;
        int posY = y + entryHeight / 2;
        textRenderer.draw(matrices, this.display, (float) x, (float) (posY - 9 / 2), 16777215);

        //reset button
        this.reset.x = x + 250;
        this.reset.y = y;
        this.reset.active = entry.get() != entry.getDefaultValue();
        this.reset.render(matrices, mouseX, mouseY, tickDelta);

        //toggle button
        this.toggle.x = x + 165;
        this.toggle.y = y;
        this.toggle.setMessage(!entry.getBoolean() ? textEnabled : textDisabled);

        //if setting is changed
        if (entry.getBoolean() != this.initValue)
            this.toggle.setMessage(this.toggle.getMessage().shallowCopy().formatted(Formatting.AQUA));

        this.toggle.render(matrices, mouseX, mouseY, tickDelta);

        //overlay text
        if (isMouseOver(mouseX, mouseY) && mouseX < x + 165 && this.tooltip != null) {
            matrices.push();
            matrices.translate(0, 0, 599);
            parent.renderTooltip(matrices, this.tooltip, mouseX, mouseY);
            matrices.pop();
        }
    }

    @Override
    public List<? extends Element> children() {
        return Arrays.asList(this.toggle, this.reset);
    }

    @Override
    public List<? extends Selectable> selectableChildren() {
        return Arrays.asList(this.toggle, this.reset);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return this.toggle.mouseClicked(mouseX, mouseY, button) || this.reset.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        return this.toggle.mouseReleased(mouseX, mouseY, button) || this.reset.mouseReleased(mouseX, mouseY, button);
    }

}