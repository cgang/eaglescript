package org.eaglescript.vm;

import java.util.HashMap;
import java.util.Map;

class LexicalEnvContext implements ExecutionContext {
    /**
     * Variables for this execution context.
     */
    private Map<String, Object> variables = new HashMap<>();

    private ExecutionContext parent;

    LexicalEnvContext(ExecutionContext parent) {
        this.parent = parent;
    }

    @Override
    public Object get(String identifier) {
        if (variables.containsKey(identifier)) {
            return variables.get(identifier);
        } else if (parent != null) {
            return parent.get(identifier);
        } else {
            // TODO throw exception?
            return null;
        }
    }

    @Override
    public void put(String identifier, Object object) {
        variables.put(identifier, object);
    }
}
