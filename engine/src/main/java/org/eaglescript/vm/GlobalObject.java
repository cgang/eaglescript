package org.eaglescript.vm;

import org.eaglescript.Bindings;
import org.eaglescript.JavaAdapter;
import org.eaglescript.ScriptObject;
import org.eaglescript.ScriptString;

import java.io.Serializable;
import java.util.Map;

public class GlobalObject implements ScriptObject, Serializable {
    private static final long serialVersionUID = 4003630082749681697L;

    private Map<String, ScriptObject> properties;

    public GlobalObject(Bindings bindings) {
        this.properties = bindings.toScriptObjects();
    }

    @Override
    public Object get(Object key) {
        if (ScriptString.isString(key)) {
            return getProperty(key.toString());
        }
        return null;
    }

    @Override
    public void set(Object key, Object value) {
        if (ScriptString.isString(key)) {
            setProperty(key.toString(), JavaAdapter.adapt(value));
        }
    }

    public Object getProperty(String identifier) {
        return properties.get(identifier);
    }

    public void setProperty(String identifier, ScriptObject object) {
        properties.put(identifier, object);
    }
}
