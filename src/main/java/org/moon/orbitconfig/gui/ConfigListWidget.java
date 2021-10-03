package org.moon.orbitconfig.gui;

import net.minecraft.client.gui.widget.ElementListWidget;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.text.Text;
import org.moon.orbitconfig.config.ConfigObject;
import org.moon.orbitconfig.config.annotation.Category;
import org.moon.orbitconfig.config.annotation.Tooltip;
import org.moon.orbitconfig.gui.entries.types.BooleanEntry;
import org.moon.orbitconfig.gui.entries.misc.CategoryEntry;
import org.moon.orbitconfig.gui.entries.types.EnumEntry;
import org.moon.orbitconfig.gui.entries.types.InputEntry;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Objects;

public class ConfigListWidget extends ElementListWidget {

    public KeyBinding focusedBinding;

    private final ConfigScreen parent;
    private final ConfigObject config;

    public ConfigListWidget(ConfigScreen parent, ConfigObject config) {
        super(parent.getClient(), parent.width + 45, parent.height, 43, parent.height - 32, 20);
        this.parent = parent;
        this.config = config;

        Field[] fields = config.object.getClass().getFields();
        for (Field f : fields) {
            Annotation[] fieldAnnotations = f.getAnnotations();
            boolean hasTooltip = false;
            Category category = null;

            for (Annotation a : fieldAnnotations) {
                if (a instanceof Category c) {
                    category = c;
                } else if (a instanceof Tooltip) {
                    hasTooltip = true;
                }
            }

            try {
                Object field      = f.get(config.object);
                Text name    = config.makeText(f.getName());
                Text tooltip = hasTooltip ? config.makeText(String.format("%s.tooltip", f.getName())) : null;

                if (category != null) {
                    Text categoryName = config.makeText(String.format("category.%s", category.value()));
                    boolean categoryHasTooltip = !category.tooltip().isBlank();
                    Text categoryTooltip = categoryHasTooltip ? config.makeText(String.format("category.%s.tooltip", category.value(), category.tooltip())) : null;
                    this.addEntry(new CategoryEntry(parent, categoryName, categoryTooltip));
                }

                if (field instanceof Boolean) {
                    this.addEntry(new BooleanEntry(parent, config, name, tooltip, f));
                } else if (field instanceof String) {
                    this.addEntry(new InputEntry(parent, config, name, tooltip, f, Objects::nonNull));
                } else if (field instanceof Enum) {
                    this.addEntry(new EnumEntry(parent, config, name, tooltip, f));
                }

            } catch (IllegalAccessException e) {}
        }
    }

    @Override
    protected int getScrollbarPositionX() {
        return super.getScrollbarPositionX() + 15;
    }

    @Override
    public int getRowWidth() {
        return super.getRowWidth() + 150;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        this.children().forEach(entry -> {
            if (entry instanceof InputEntry) {
                ((InputEntry) entry).updateFocus(mouseX, mouseY);
            }
        });
        return super.mouseClicked(mouseX, mouseY, button);
    }
}
