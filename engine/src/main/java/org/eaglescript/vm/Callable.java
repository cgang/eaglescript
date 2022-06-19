package org.eaglescript.vm;

public interface Callable {
    /**
     * Invoke this callable object with specified arguments.
     * @param arguments arguments for this invocation.
     * @throws ScriptAwareException if error occurs while invoking.
     */
    Object invoke(Object[] arguments) throws ScriptAwareException;
}
