package org.eaglescript;

import java.io.Serializable;
import java.util.Objects;

/**
 * A {@link ScriptString} represents a string object.
 */
public class ScriptString implements ScriptObject, Serializable {
    private static final long serialVersionUID = 1041585289831197894L;

    public static boolean isString(Object object) {
        return object instanceof String || object instanceof ScriptString;
    }

    private final String value;

    public ScriptString(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public Object toJavaObject(Class<?> type) {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScriptString that = (ScriptString) o;
        return value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
