package org.moon.orbitconfig.gui.entries;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ElementListWidget;
import org.moon.orbitconfig.gui.ConfigScreen;

public abstract class Entry extends ElementListWidget.Entry<Entry> {
    protected final MinecraftClient client;
    protected final ConfigScreen parent;
    public Entry(ConfigScreen parent) {
        this.parent = parent;
        this.client = parent.getClient();

    }
}