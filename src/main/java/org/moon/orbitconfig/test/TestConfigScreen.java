package org.moon.orbitconfig.test;

import net.minecraft.client.gui.screen.Screen;
import org.moon.orbitconfig.OrbitConfigMod;
import org.moon.orbitconfig.config.ConfigObject;
import org.moon.orbitconfig.gui.ConfigScreen;

public class TestConfigScreen extends ConfigScreen {
    public TestConfigScreen(Screen parentScreen) {
        super(parentScreen, new ConfigObject(OrbitConfigMod.CONFIG));
    }
}
