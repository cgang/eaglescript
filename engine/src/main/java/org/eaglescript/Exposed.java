package org.eaglescript;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A {@link Exposed} denote a method to be exposed to script.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Exposed {
    /**
     * The exposed name available to script.
     * @return a name to be used by script.
     */
    String name() default "";
}
