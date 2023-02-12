package org.eaglescript;

import org.eaglescript.util.ReflectHelper;
import org.eaglescript.vm.ScriptAwareException;

/**
 * The {@link ScriptObject} defines object being accessed in script.
 */
public interface ScriptObject {
    /**
     * Get a property of this script object.
     * @param key a key could be an identifier, index number or symbol.
     * @return the property, null if not found.
     */
    default Object get(Object key) {
        if (key == null) {
            return null;
        }

        return ReflectHelper.of(this.getClass()).get(this, key.toString());
    }

    /**
     * Set a property for this script object.
     * @param key a key could be an identifier, index number or symbol.
     * @param value the property value to be set.
     */
    default void set(Object key, Object value) {
    }

    /**
     * Convert this script object to java object for java invocation.
     * @param type the expected type to be returned.
     * @return an object representing this script object.
     */
    default Object toJavaObject(Class<?> type) throws ScriptAwareException {
        throw new UnsupportedOperationException("Not implemented");
    }
}
