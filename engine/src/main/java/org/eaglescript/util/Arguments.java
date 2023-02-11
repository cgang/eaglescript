package org.eaglescript.util;

import org.eaglescript.JavaAdapter;
import org.eaglescript.ScriptString;
import org.eaglescript.vm.ScriptAwareException;

/**
 * The {@link Arguments} provides a utility to access arguments of invocation.
 */
public class Arguments {
    private Object[] args;
    private int offset;

    /**
     * Construct from an array of arguments.
     *
     * @param args an array of arguments.
     */
    public Arguments(Object[] args) {
        this.args = args;
    }

    public Object[] toArray() {
        return this.args;
    }

    /**
     * Get next argument and increase the offset.
     *
     * @return next argument.
     */
    public Object next() {
        return this.args[offset++];
    }

    /**
     * Check if there is more argument.
     * @return true if there is more argument.
     */
    public boolean hasNext() {
        return offset < args.length;
    }

    /**
     * Get next optional argument.
     * @return next argument, null if unavailable.
     */
    public Object nextOptional() {
        return offset < args.length ? next() : null;
    }

    /**
     * Get next argument with specified type.
     * @param type expected type of argument.
     * @return an object of specified type.
     * @throws ScriptAwareException if next argument is not available or incompatible type.
     */
    public Object next(Class<?> type) throws ScriptAwareException {
        return JavaAdapter.toJava(next(), type);
    }

    /**
     * Get next argument as string and increase the offset.
     * @return next string argument.
     * @throws ScriptAwareException if next argument is not string.
     */
    public String nextString() throws ScriptAwareException {
        Object arg = next();
        if (arg == null) {
            return null;
        }

        if (ScriptString.isString(arg)) {
            return String.valueOf(arg);
        }

        throw new ScriptAwareException("Next argument is not a string.");
    }
}
