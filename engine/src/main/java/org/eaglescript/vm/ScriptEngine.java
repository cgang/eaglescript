package org.eaglescript.vm;

import org.eaglescript.Bindings;

import java.io.IOException;

/**
 * The {@link ScriptEngine} provides entry point to run Eagle Script.
 */
public interface ScriptEngine {
    CompiledScript compile(String name) throws IOException;
    
    EagleThread start(CompiledScript script, Object[] arguments, Bindings bindings);

    void resume(EagleThread thread, Bindings bindings);
}
