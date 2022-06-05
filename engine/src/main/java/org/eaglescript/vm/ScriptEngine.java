package org.eaglescript.vm;

import org.eaglescript.Bindings;

/**
 * The {@link ScriptEngine} provides entry point to run Eagle Script.
 */
public interface ScriptEngine {
    CompiledScript compile(String script);

    EagleThread start(CompiledScript script, Bindings bindings);

    void resume(EagleThread thread, Bindings bindings);
}
