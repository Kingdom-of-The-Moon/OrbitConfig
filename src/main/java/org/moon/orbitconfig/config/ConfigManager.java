package org.moon.orbitconfig.config;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.stream.JsonWriter;
import net.fabricmc.loader.api.FabricLoader;
import org.moon.orbitconfig.OrbitConfigMod;
import org.moon.orbitconfig.config.annotation.OrbitConfig;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

public class ConfigManager {

    private static final HashMap<Object, Type> CONFIG_CLASSES = new HashMap<>();

    /**
     * Registers your config object and type for your mod
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T>T register(Object config, Class<T> clazz) {
        CONFIG_CLASSES.put(config, clazz);
        return (T) load(config);
    }

    /**
     * Saves the provided config object to file
     * @param configObject
     */
    public static void save(Object configObject) {
        ConfigObject config = getConfig(configObject);
        try {
            Gson gson = new Gson();
            JsonWriter writer = new JsonWriter(Files.newBufferedWriter(getPath(config)));
            writer.setIndent("  ");
            gson.toJson(gson.toJsonTree(configObject, CONFIG_CLASSES.get(configObject)), writer);
            writer.close();
        } catch (IOException e) {
            OrbitConfigMod.LOGGER.error("Failed to save config \"%s\"!".formatted(config.config.filename()));
            OrbitConfigMod.LOGGER.error(e.getMessage());
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
            Gson gson = new Gson();
            Path configPath = getPath(config);
            if (!Files.exists(configPath)) {
                save(configObject);
            }
            return gson.fromJson(Files.newBufferedReader(configPath), CONFIG_CLASSES.get(configObject));
        } catch (IOException e) {
            OrbitConfigMod.LOGGER.error("Failed to load config \"%s\"!".formatted(config.config.filename()));
            OrbitConfigMod.LOGGER.error(e.getMessage());
        }
        return configObject;
    }

    /**
     * doesnt work yet :<
     * @param configObject
     * @param <T>
     * @return
     */
    public static <T>T restore(T configObject) {
        Gson gson = new Gson();
        return configObject;
        //configObject = gson.fromJson(CONFIG_BACKUPS.get(configObject), CONFIG_CLASSES.get((configObject)));
    }


    /**
     * Gets the file path for your config
     * @param config
     * @return
     */
    public static Path getPath(ConfigObject config) {
        return FabricLoader.getInstance().getConfigDir().resolve(config.config.filename()+".json");
    }

    public static ConfigObject getConfig(Object configObject) {
        return new ConfigObject(configObject);
    }
}
