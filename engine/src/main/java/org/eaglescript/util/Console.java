package org.eaglescript.util;

import org.eaglescript.Exposed;

import java.util.StringJoiner;

/**
 * This {@link Console} provides a built-in object to support console function.
 * It's intended to be exposed as 'console' object to script.
 */
public class Console {
    @Exposed
    public void log(Object[] arguments) {
        StringJoiner sj = new StringJoiner(" ");
        for (Object arg : arguments) {
            sj.add(String.valueOf(arg));
        }
        System.out.println(sj);
    }
}
