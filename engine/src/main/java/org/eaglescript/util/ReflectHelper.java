package org.eaglescript.util;

import org.eaglescript.Exposed;
import org.eaglescript.vm.Callable;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;

/**
 * A {@link ReflectHelper} provides implementation support for {@link org.eaglescript.Exposed} annotation.
 */
public class ReflectHelper {
    private static final Map<Class<?>, ReflectHelper> helperCache = new IdentityHashMap<>();

    /**
     * Get a helper instance of specified class.
     * @param clazz the target class.
     * @return a reflection helper for that class.
     */
    public static ReflectHelper of(Class<?> clazz) {
        return helperCache.computeIfAbsent(clazz, ReflectHelper::createHelper);
    }

    static ReflectHelper createHelper(Class<?> clazz) {
        return new ReflectHelper(clazz);
    }


    private Class<?> clazz;
    private Map<String, Method> methods = new HashMap<>();

    ReflectHelper(Class<?> clazz) {
        this.clazz = clazz;
        for (Method method : clazz.getDeclaredMethods()) {
            Exposed exposed = method.getAnnotation(Exposed.class);
            if (exposed == null) {
                continue;
            }

            String name = exposed.name();
            if (name.isEmpty()) {
                name = method.getName();
            }
            int mod = method.getModifiers();
            if (Modifier.isPublic(mod)) {
                methods.put(name, method);
            }
        }
    }

    public Callable get(Object target, String name) {
        Method method = methods.get(name);
        if (method == null) {
            return null;
        }

        Class<?>[] paramTypes = method.getParameterTypes();
        if (paramTypes.length == 1 && paramTypes[0].equals(Arguments.class)) {
            return new ReflectCallable.ArgumentsCallable(target, method);
        } else {
            return new ReflectCallable.SimpleCallable(target, method);
        }
    }
}
