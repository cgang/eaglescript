package org.eaglescript.vm;

import java.util.HashMap;
import java.util.Map;

/**
 * A {@link ExecutionContext} represents an execution context of ECMAScript.
 * @see <a href="https://262.ecma-international.org/6.0/#sec-execution-contexts">Execution Contexts</a>
 */
public class ExecutionContext {
    /**
     * Variables for this execution context.
     */
    private Map<String, Object> variables = new HashMap<>();

    /**
     * Resolve a binding with specified identifier.
     * @param identifier an identifier.
     * @return an object, null if not found.
     */
    public Object get(String identifier) {
        return variables.get(identifier);
    }

    /**
     * Put a binding.
     * @param identifier an identifier.
     * @param object an object.
     */
    public void put(String identifier, Object object) {
        variables.put(identifier, object);
    }
}
