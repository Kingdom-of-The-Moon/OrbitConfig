package org.moon.orbitconfig.gui.entries.types;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import org.moon.orbitconfig.config.ConfigEntry;
import org.moon.orbitconfig.config.ConfigObject;
import org.moon.orbitconfig.gui.ConfigScreen;
import org.moon.orbitconfig.gui.entries.Entry;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class InputEntry extends Entry {
    //entry
    private final ConfigEntry entry;

    //values
    private final String initValue;
    private final Predicate<String> validator;

    //buttons
    private final TextFieldWidget field;
    private final ButtonWidget reset;

    public InputEntry(ConfigScreen parent, ConfigObject config, Text display, Text tooltip, Field configField, Predicate<String> validator) {
        super(parent, config, display, tooltip);
        this.entry = config.getEntry(configField);
        this.initValue = entry.getString();
        this.validator = validator;

        //field
        this.field = new TextFieldWidget(this.client.textRenderer, 0, 0, 70, 16, new LiteralText(entry.getString()));
        this.field.setChangedListener((fieldText) -> {
            if (isTextValid()) {
                entry.set(fieldText);
            }
        });
        this.field.setText(entry.getString());
        this.field.setTextPredicate(validator);

        //reset button
        this.reset = new ButtonWidget(0, 0, 50, 20, new TranslatableText("controls.reset"), (button) -> {
            entry.restoreDefaultValue();
            this.field.setText(entry.getString());
        });
    }

    public void render(MatrixStack matrices, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
        //text
        TextRenderer textRenderer = this.client.textRenderer;
        int posY = y + entryHeight / 2;
        textRenderer.draw(matrices, this.display, (float) x, (float)(posY - 9 / 2), 16777215);

        //reset button
        this.reset.x = x + 250;
        this.reset.y = y;
        this.reset.active = !entry.isDefaultValue();
        this.reset.render(matrices, mouseX, mouseY, tickDelta);

        //text field
        this.field.x = x + 167;
        this.field.y = y + 2;

        //if setting is changed
        if (!this.entry.getString().equals(this.initValue))
            try {
                this.entry.getDefaultValue().getClass().getConstructor(new Class[] {String.class}).newInstance(this.entry.getString());
                this.field.setEditableColor(Formatting.AQUA.getColorValue());
            } catch (Exception e) {
                this.field.setEditableColor(Formatting.RED.getColorValue());
            }
        else
            this.field.setEditableColor(Formatting.WHITE.getColorValue());

        if (!isTextValid()) {
            this.field.setEditableColor(Formatting.RED.getColorValue());
        }

        this.field.render(matrices, mouseX, mouseY, tickDelta);

        super.render(matrices, index, y, x, entryWidth, entryHeight, mouseX, mouseY, hovered, tickDelta);
    }

    public boolean isTextValid() {
        return this.validator.test(this.field.getText());
    }

    @Override
    public List<? extends Element> children() {
        return Arrays.asList(this.field, this.reset);
    }

    @Override
    public List<? extends Selectable> selectableChildren() {
        return Arrays.asList(this.field, this.reset);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return this.field.mouseClicked(mouseX, mouseY, button) || this.reset.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        return this.field.mouseReleased(mouseX, mouseY, button) || this.reset.mouseReleased(mouseX, mouseY, button);
    }

    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return super.keyPressed(keyCode, scanCode, modifiers) || this.field.keyPressed(keyCode, scanCode, modifiers);
    }

    public boolean charTyped(char chr, int modifiers) {
        return this.field.charTyped(chr, modifiers);
    }

    public void setFocused(boolean focused) {
        field.setTextFieldFocused(focused);
    }

    public void updateFocus(double x, double y) {
        setFocused(field.isMouseOver(x, y));
    }
}