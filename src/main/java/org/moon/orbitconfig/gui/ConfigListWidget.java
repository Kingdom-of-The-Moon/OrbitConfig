package org.moon.orbitconfig.gui;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.widget.ElementListWidget;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.text.Text;
import org.moon.orbitconfig.OrbitConfigMod;
import org.moon.orbitconfig.config.ConfigObject;
import org.moon.orbitconfig.config.annotation.Category;
import org.moon.orbitconfig.config.annotation.Regex;
import org.moon.orbitconfig.config.annotation.Tooltip;
import org.moon.orbitconfig.gui.entries.misc.CategoryEntry;
import org.moon.orbitconfig.gui.entries.types.BooleanEntry;
import org.moon.orbitconfig.gui.entries.types.EnumEntry;
import org.moon.orbitconfig.gui.entries.types.InputEntry;
import org.moon.orbitconfig.gui.entries.types.KeybindEntry;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;

@SuppressWarnings("ALL")
@Environment(EnvType.CLIENT)
public class ConfigListWidget extends ElementListWidget {

    private static final Predicate<String> IS_INT = s -> doesNotThrow(() -> Integer.parseInt(s));
    private static final Predicate<String> IS_FLOAT = s -> doesNotThrow(() -> Float.parseFloat(s));
    private static final Predicate<String> IS_DOUBLE = s -> doesNotThrow(() -> Double.parseDouble(s));
    private static final Predicate<String> IS_LONG = s -> doesNotThrow(() -> Long.parseLong(s));
    private static final Predicate<String> IS_BYTE = s -> doesNotThrow(() -> Byte.parseByte(s));
    private static final Predicate<String> IS_SHORT = s -> doesNotThrow(() -> Short.parseShort(s));

    private static boolean doesNotThrow(Runnable code) {
        try {
            code.run();
            return true;
        } catch (Exception ignored) {
            return false;
        }
    }

    public static final HashMap<Predicate<Object>, TypedEntryFactory> TYPED_FACTORIES = new HashMap<>() {{
        put(o -> o instanceof String, (parent, config, name, tooltip, configField)     -> new InputEntry(parent, config, name, tooltip, configField, Objects::nonNull));
        put(o -> o instanceof Enum<?>, (parent, config, name, tooltip, configField)    -> new EnumEntry(parent, config, name, tooltip, configField));
        put(o -> o instanceof KeyBinding, (parent, config, name, tooltip, configField) -> new KeybindEntry(parent, config, name, tooltip, configField));

        // Primitives
        put(o -> o instanceof Boolean, (parent, config, name, tooltip, configField) -> new BooleanEntry(parent, config, name, tooltip, configField));
        put(o -> o instanceof Integer, (parent, config, name, tooltip, configField) -> new InputEntry(parent, config, name, tooltip, configField, IS_INT));
        put(o -> o instanceof Float,   (parent, config, name, tooltip, configField) -> new InputEntry(parent, config, name, tooltip, configField, IS_FLOAT));
        put(o -> o instanceof Double,  (parent, config, name, tooltip, configField) -> new InputEntry(parent, config, name, tooltip, configField, IS_DOUBLE));
        put(o -> o instanceof Long,    (parent, config, name, tooltip, configField) -> new InputEntry(parent, config, name, tooltip, configField, IS_LONG));
        put(o -> o instanceof Byte,    (parent, config, name, tooltip, configField) -> new InputEntry(parent, config, name, tooltip, configField, IS_BYTE));
        put(o -> o instanceof Short,   (parent, config, name, tooltip, configField) -> new InputEntry(parent, config, name, tooltip, configField, IS_SHORT));
    }};

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
            String regex = null;

            for (Annotation a : fieldAnnotations) {
                if (a instanceof Category c) {
                    category = c;
                } else if (a instanceof Tooltip) {
                    hasTooltip = true;
                } else if (a instanceof Regex r) {
                    regex = r.value();
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

                boolean found = false;
                for(Map.Entry<Predicate<Object>, TypedEntryFactory> factory : TYPED_FACTORIES.entrySet()) {
                    if (factory.getKey().test(field)) {
                        Entry<?> configEntry = factory.getValue().create(parent, config, name, tooltip, f);
                        if (regex != null) {
                            if (configEntry instanceof InputEntry inputEntry) {
                                final String jank = regex;
                                inputEntry.validator = (s) -> s.matches(jank);
                            } else {
                                OrbitConfigMod.LOGGER.error("Regex cannot be applied to type: \"%s\"".formatted(configEntry.getClass().getName()));
                            }
                        }
                        this.addEntry(configEntry);
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    OrbitConfigMod.LOGGER.error("No registered factory for type: \"%s\"".formatted(field.getClass().getName()));
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

    public interface TypedEntryFactory<T extends org.moon.orbitconfig.gui.entries.Entry> {
        T create(ConfigScreen parent, ConfigObject object, Text display, Text tooltip, Field configField);
    }
}
