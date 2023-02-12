package org.eaglescript;

import org.eaglescript.util.ReflectHelper;
import org.eaglescript.vm.ScriptAwareException;

/**
 * {@link JavaAdapter} provides a default adaptor to wrap an object into script object.
 */
public class JavaAdapter implements ScriptObject {

    /**
     * Adapt an object into script object.
     *
     * @param target target object.
     * @return original object if it's null or already a script object, an adaptor otherwise.
     */
    public static ScriptObject adapt(Object target) {
        if (target == null) {
            return null;
        } else if (target instanceof ScriptObject) {
            return (ScriptObject) target;
        }

        return new JavaAdapter(target, ReflectHelper.of(target.getClass()));
    }

    /**
     * Convert an object to specified type if possible.
     *
     * @param object target object.
     * @param type   desired java type.
     * @return an object of specified type if possible.
     * @throws ScriptAwareException if error occurs.
     */
    public static Object toJava(Object object, Class<?> type) throws ScriptAwareException {
        if (object instanceof Double) {
            return new ScriptNumber((Double) object).toJavaObject(type);
        } else if (object instanceof ScriptObject) {
            return ((ScriptObject) object).toJavaObject(type);
        } else {
            // TODO convert to specified type
            return object;
        }
    }

    private final Object target;
    private final ReflectHelper helper;

    JavaAdapter(Object target, ReflectHelper helper) {
        this.target = target;
        this.helper = helper;
    }

    @Override
    public void set(Object key, Object value) {
        throw new UnsupportedOperationException("set is not supported for java object");
    }

    @Override
    public String toString() {
        return "JavaAdapter{" +
                "target=" + target +
                '}';
    }
}
