package org.moon.orbitconfig.config;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import net.fabricmc.loader.api.FabricLoader;
import org.moon.orbitconfig.OrbitConfigMod;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.nio.file.*;
import java.util.Arrays;
import java.util.HashMap;

public class ConfigManager {

    private static final HashMap<Type, HashMap<String, Object>> CONFIG_DEFAULTS = new HashMap<>();
    private static final HashMap<Type, HashMap<String, Object>> CONFIG_TEMP = new HashMap<>();
    private static final HashMap<Class<?>, Object> TYPE_ADAPTERS = new HashMap<>();

    /**
     * Registers your config object and type for your mod
     * @param <T>
     * @return
     */
    public static <T>T register(Object config) {
        backupValues(config, true);
        return (T) load(config);
    }

    public static void registerTypeAdapter(Class<?> clazz, Object typeAdapter) {
        TYPE_ADAPTERS.putIfAbsent(clazz, typeAdapter);
    }

    /**
     * Saves the provided config object to file
     * @param config
     */
    public static void save(ConfigObject config) {
        try {
            Gson gson = getGson();
            StringWriter w = new StringWriter();
            JsonWriter writer = new JsonWriter(w);
            writer.setIndent("  ");
            gson.toJson(gson.toJsonTree(config.object, config.object.getClass()), writer);
            Files.deleteIfExists(getPath(config));
            var fw = Files.newBufferedWriter(getPath(config), StandardOpenOption.CREATE, StandardOpenOption.WRITE);
            fw.write(w.toString());
            fw.close();
        } catch (Exception e) {
            OrbitConfigMod.LOGGER.error("Failed to save config \"%s\"!".formatted(config.config.filename()));
            OrbitConfigMod.LOGGER.error(e);
        }
    }

    /**
     * Loads your config object from file, with type conversion
     * @param configObject
     * @param <T>
     * @return
     */
    static <T>T load(T configObject) {
        ConfigObject config = getConfig(configObject);
        try {
            Path configPath = getPath(config);
            if (!Files.exists(configPath)) {
                save(config);
            }

            T loadedInstance = getGson().fromJson(Files.newBufferedReader(configPath), (Type) config.object.getClass());
            return loadedInstance;
        } catch (IOException e) {
            OrbitConfigMod.LOGGER.error("Failed to load config \"%s\"!".formatted(config.config.filename()));
            OrbitConfigMod.LOGGER.error(e.getMessage());
        }
        return configObject;
    }

    /**
     * Creates a backup of a config object, so changes can be reverted later
     * @param config
     * @param mode
     */
    public static void backupValues(Object config, boolean mode) {
        HashMap<String, Object> defaults = new HashMap<>() {{
            for (Field f : config.getClass().getFields()) {
                try {
                    put(f.getName(), f.get(config));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }};
        (mode ? CONFIG_DEFAULTS : CONFIG_TEMP).put(config.getClass(), defaults);
    }



    public static Object getDefaultValue(Object object, String name) {
        return CONFIG_DEFAULTS.get(object.getClass()).get(name);
    }

    /**
     * Reverts changes made to a config object
     * @param config
     * @return
     */
    public static void revertChanges(ConfigObject config) {
        restore(config, CONFIG_TEMP);
        CONFIG_TEMP.remove(config.getClass());
    }

    static void restore(ConfigObject object, HashMap<Type, HashMap<String, Object>> map) {
        map.get(object.object.getClass()).entrySet().forEach(entry -> {
            try {
                object.getClass().getField(entry.getKey()).set(object.object, entry.getValue());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        });
    }


    /**
     * Gets the file path for your config
     * @param config
     * @return
     */
    public static Path getPath(ConfigObject config) {
        return FabricLoader.getInstance().getConfigDir().resolve(config.config.filename()+".json");
    }

    /**
     * Alias of getPath(ConfigObject config)
     * @param configObject
     * @return
     */
    public static ConfigObject getConfig(Object configObject) {
        Arrays.stream(configObject.getClass().getAnnotations()).forEach(OrbitConfigMod.LOGGER::warn);
        return new ConfigObject(configObject);
    }

    /**
     * Builds a Gson object with custom type adapters
     * @return
     */
    private static Gson getGson() {
        GsonBuilder builder = new GsonBuilder();
        TYPE_ADAPTERS.forEach((clazz, adapter) -> builder.registerTypeAdapter(clazz, adapter));
        return builder.create();
    }
}
