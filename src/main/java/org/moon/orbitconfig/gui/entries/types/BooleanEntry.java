package org.moon.orbitconfig.gui.entries.types;

import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import org.moon.orbitconfig.config.ConfigObject;
import org.moon.orbitconfig.gui.ConfigScreen;
import org.moon.orbitconfig.gui.entries.TypedEntry;
import org.moon.orbitconfig.gui.widgets.RightClickableButtonWidget;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public class BooleanEntry extends TypedEntry<Boolean> {

    private final Text textEnabled;
    private final Text textDisabled;

    private final ButtonWidget toggle;

    public BooleanEntry(ConfigScreen parent, ConfigObject config, Text display, Text tooltip, Field configField) {
        super(parent, config, configField, display, tooltip);

        //toggle button
        this.toggle = new RightClickableButtonWidget(0, 0, 75, 20, this.display, (button) -> {
            entry.setBoolean(!entry.getBoolean());
        }, (button) -> {
            entry.setBoolean(!entry.getBoolean());
        });
        this.textEnabled  = config.makeText(String.format("%s.disabled", entry.getName()));
        this.textDisabled = config.makeText(String.format("%s.enabled", entry.getName()));


    }

    public void render(MatrixStack matrices, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
        //toggle button
        this.toggle.x = x + 165;
        this.toggle.y = y;
        this.toggle.setMessage(!entry.getBoolean() ? textEnabled : textDisabled);

        //if setting is changed
        if (entry.getBoolean() != this.initialValue)
            this.toggle.setMessage(this.toggle.getMessage().shallowCopy().formatted(Formatting.AQUA));

        this.toggle.render(matrices, mouseX, mouseY, tickDelta);

        super.render(matrices, index, y, x, entryWidth, entryHeight, mouseX, mouseY, hovered, tickDelta);
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