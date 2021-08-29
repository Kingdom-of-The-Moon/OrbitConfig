package org.moon.orbitconfig.config;

import org.moon.orbitconfig.OrbitConfigMod;

import java.lang.reflect.Field;

public class ConfigEntry {

    protected final Object target;
    protected final Object defaultValue;
    protected final Field field;

    public ConfigEntry(Object target, Field field) {
        this.target = target;
        this.field = field;
        this.defaultValue = get();
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

    public Object get() {
        try {
            return field.get(target);
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
        return defaultValue;
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
