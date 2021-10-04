package org.moon.orbitconfig.config;

import org.moon.orbitconfig.OrbitConfigMod;

import java.lang.reflect.Field;

public class ConfigEntry {

    protected final Object target;
    protected final Field field;

    public ConfigEntry(Object target, Field field) {
        this.target = target;
        this.field = field;
    }

    public boolean getBoolean() {
        try {
            return field.getBoolean(target);
        } catch (IllegalAccessException e) {
            error(e);
            return false;
        }
    }
    public void setBoolean(boolean value) {
        try {
            field.setBoolean(target, value);
        } catch (IllegalAccessException e) {
            error(e);
        }
    }

    public float getFloat() {
        try {
            return field.getFloat(target);
        } catch (IllegalAccessException e) {
            error(e);
            return 0.0f;
        }
    }
    public void setFloat(float value) {
        try {
            field.setFloat(target, value);
        } catch (IllegalAccessException e) {
            error(e);
        }
    }

    public float getInt() {
        try {
            return field.getFloat(target);
        } catch (IllegalAccessException e) {
            error(e);
            return 0;
        }
    }
    public void setInt(int value) {
        try {
            field.setInt(target, value);
        } catch (IllegalAccessException e) {
            error(e);
        }
    }

    public Enum<?> getEnum() {
        return (Enum<?>) get();
    }
    public void setEnum(Enum<?> value) {
        set(value);
    }

    public <T>T get() {
        try {
            return (T) field.get(target);
        } catch (IllegalAccessException e) {
            error(e);
        }
        return null;
    }
    public void set(Object value) {
        try {
            field.set(target, value);
        } catch (IllegalAccessException e) {
            error(e);
        }
    }

    public Object getDefaultValue() {
        return ConfigManager.getDefaultValue(target, getName());
    }

    public void restoreDefaultValue() {
        set(getDefaultValue());
    }

    public boolean isDefaultValue () {
        return get().equals(getDefaultValue());
    }

    public String getString() {
        return get().toString();
    }

    public String getName() {
        return field.getName();
    }

    private static void error(Exception e) {
        OrbitConfigMod.LOGGER.error("Failed to modify config class!\n"+e.getMessage());
    }
}
