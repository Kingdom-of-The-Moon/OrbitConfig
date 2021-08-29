package org.moon.orbitconfig.gui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ElementListWidget;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.text.Text;
import org.moon.orbitconfig.config.ConfigObject;
import org.moon.orbitconfig.config.annotation.Category;
import org.moon.orbitconfig.config.annotation.Tooltip;
import org.moon.orbitconfig.gui.entries.BooleanEntry;
import org.moon.orbitconfig.gui.entries.CategoryEntry;
import org.moon.orbitconfig.gui.entries.InputEntry;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

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

            for (Annotation a : fieldAnnotations) {
                if (a instanceof Category category) {
                    this.addEntry(new CategoryEntry(parent, config.makeText("config.category." + category.value())));
                } else if (a instanceof Tooltip) {
                    hasTooltip = true;
                }
            }

            try {
                Object field      = f.get(config.object);
                String configName = String.format("config.%s", f.getName());
                Text name    = config.makeText(configName);
                Text tooltip = hasTooltip ? config.makeText(String.format("%s.tooltip", configName)) : null;

                // Text display, Text tooltip, Field configField, ConfigObject config
                if (field instanceof Boolean) {
                    this.addEntry(new BooleanEntry(parent, name, tooltip, f, config));
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
