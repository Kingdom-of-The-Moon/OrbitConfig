package org.moon.orbitconfig.config;

import net.minecraft.text.TranslatableText;
import org.moon.orbitconfig.config.annotation.OrbitConfig;

import java.lang.reflect.Field;
import java.util.HashMap;

public class ConfigObject {

    public final Object object;
    public final OrbitConfig config;

    private final HashMap<Field, ConfigEntry> fieldToEntry;

    public ConfigObject(Object object) {
        this.object = object;
        this.config = this.object.getClass().getAnnotation(OrbitConfig.class);
        this.fieldToEntry = new HashMap<>();

        Field[] fields = object.getClass().getFields();
        for(Field f : fields) {
            fieldToEntry.put(f, new ConfigEntry(object, f));
        }
    }

    public ConfigEntry getEntry(Field f) {
        return fieldToEntry.get(f);
    }

    public TranslatableText makeText(String entry) {
        return new TranslatableText(String.format("%s.gui.%s", config.modID(), entry));
    }
}
