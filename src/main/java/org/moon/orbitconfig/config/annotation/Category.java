package org.moon.orbitconfig.config.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Category {
    String value();

    String tooltip() default "";
}
