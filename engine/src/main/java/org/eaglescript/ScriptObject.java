package org.eaglescript;

import java.io.Serializable;

/**
 * A {@link ScriptObject} represents base class for all object type.
 */
public abstract class ScriptObject implements Serializable {
    private static final long serialVersionUID = -2523504278396839733L;

    public Object get(Object key) {
        return null;
    }

    public void set(Object key, Object value) {

    }

}
