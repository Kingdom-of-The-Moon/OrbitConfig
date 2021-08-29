package org.moon.orbitconfig.test;

import org.moon.orbitconfig.config.annotation.Category;
import org.moon.orbitconfig.config.annotation.Tooltip;
import org.moon.orbitconfig.config.annotation.OrbitConfig;

@OrbitConfig(modID = "test", filename = "test_config")
public class TestConfig {
    @Category("greetings")
    public boolean hello = false;

    @Category("youre_mom")
    public boolean owned = true;
    public boolean hi = true;
}
