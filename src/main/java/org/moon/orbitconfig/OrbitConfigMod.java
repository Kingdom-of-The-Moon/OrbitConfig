package org.moon.orbitconfig;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.moon.orbitconfig.config.ConfigManager;
import org.moon.orbitconfig.test.TestConfig;
import org.moon.orbitconfig.test.TestConfigScreen;

public class OrbitConfigMod implements ModInitializer {

	public static final Logger LOGGER = LogManager.getLogger();

	public static boolean modmenu = false;

	public static TestConfig CONFIG;

	@Override
	public void onInitialize() {
		CONFIG = ConfigManager.register(new TestConfig(),TestConfig.class);

		System.out.println(CONFIG.hello);

		modmenu = FabricLoader.getInstance().isModLoaded("modmenu");
	}
}
