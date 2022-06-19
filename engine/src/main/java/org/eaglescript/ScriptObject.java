package org.eaglescript;

import org.eaglescript.vm.ScriptAwareException;

import java.io.Serializable;

/**
 * A {@link ScriptObject} represents base class for all object type.
 */
public abstract class ScriptObject implements Serializable {
    private static final long serialVersionUID = -2523504278396839733L;

    public static Object toJava(Object object, Class<?> type) throws ScriptAwareException {
        if (object instanceof Double) {
            return new ScriptNumber((Double) object).toJavaObject(type);
        } else if (object instanceof ScriptObject) {
            return ((ScriptObject) object).toJavaObject(type);
        } else {
            return object;
        }
    }

    public Object get(Object key) {
        return null;
    }

    public void set(Object key, Object value) {

    }

    /**
     * Convert this script object to java object for java invocation.
     * @param type the expected type to be returned.
     * @return an object representing this script object.
     */
    public Object toJavaObject(Class<?> type) throws ScriptAwareException {
        throw new UnsupportedOperationException("Not implemented");
    }
}
