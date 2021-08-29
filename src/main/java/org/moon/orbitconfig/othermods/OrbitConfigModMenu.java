package org.moon.orbitconfig.othermods;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import org.moon.orbitconfig.test.TestConfigScreen;

public class OrbitConfigModMenu implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return TestConfigScreen::new;
    }
}
