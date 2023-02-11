package org.eaglescript.vm;

import org.eaglescript.JavaAdapter;

class GlobalContext implements ExecutionContext {
    private GlobalObject globalObject;

    public GlobalContext(GlobalObject globalObject) {
        this.globalObject = globalObject;
    }

    @Override
    public Object get(String identifier) {
        return globalObject.getProperty(identifier);
    }

    @Override
    public void put(String identifier, Object object) {
        globalObject.setProperty(identifier, JavaAdapter.adapt(object));
    }
}
