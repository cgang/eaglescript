package org.eaglescript.util;

import org.eaglescript.ScriptObject;
import org.eaglescript.vm.Callable;
import org.eaglescript.vm.ScriptAwareException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

abstract class ReflectCallable implements Callable {
    static class SimpleCallable extends ReflectCallable {
        SimpleCallable(Object target, Method method) {
            super(target, method);
        }

        @Override
        protected Object[] toParameters(Object[] arguments) throws ScriptAwareException {
            Object[] params = new Object[method.getParameterCount()];
            Class<?>[] types = method.getParameterTypes();
            int len = Math.min(params.length, arguments.length);
            for (int i = 0; i < len; i++) {
                params[i] = ScriptObject.toJava(arguments[i], types[i]);
            }
            return params;
        }
    }

    static class ArgumentsCallable extends ReflectCallable {
        ArgumentsCallable(Object target, Method method) {
            super(target, method);
        }

        @Override
        protected Object[] toParameters(Object[] arguments) throws ScriptAwareException {
            return new Object[] {new Arguments(arguments)};
        }
    }

    protected final Object target;
    protected Method method;

    protected ReflectCallable(Object target, Method method) {
        this.target = target;
        this.method = method;
    }

    /**
     * Convert script arguments to java method call parameters.
     * @param arguments arguments from script.
     * @return parameters for java invocation.
     */
    protected abstract Object[] toParameters(Object[] arguments) throws ScriptAwareException;

    @Override
    public Object invoke(Object[] arguments) throws ScriptAwareException {
        Object[] params = toParameters(arguments);
        try {
            return method.invoke(target, params);
        } catch (IllegalAccessException e) {
            throw new ScriptAwareException("Access denied", e);
        } catch (InvocationTargetException e) {
            throw new ScriptAwareException("Exception occurs while invoking method.", e.getCause());
        }
    }
}
