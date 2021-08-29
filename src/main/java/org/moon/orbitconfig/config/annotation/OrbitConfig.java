package org.moon.orbitconfig.config.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface OrbitConfig {
    String filename();
    String modID();
}
