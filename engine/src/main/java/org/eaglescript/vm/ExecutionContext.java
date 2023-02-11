package org.eaglescript.vm;

/**
 * A {@link ExecutionContext} represents an execution context of ECMAScript.
 *
 * @see <a href="https://262.ecma-international.org/6.0/#sec-execution-contexts">Execution Contexts</a>
 */
public interface ExecutionContext {

    /**
     * Resolve a binding with specified identifier.
     *
     * @param identifier an identifier.
     * @return an object, null if not found.
     */
    Object get(String identifier);

    /**
     * Put a binding.
     *
     * @param identifier an identifier.
     * @param object     an object.
     */
    void put(String identifier, Object object);
}
