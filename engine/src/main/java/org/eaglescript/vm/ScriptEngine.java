package org.eaglescript.vm;

import org.eaglescript.Bindings;

import java.io.IOException;

/**
 * The {@link ScriptEngine} provides entry point to run Eagle Script.
 */
public interface ScriptEngine {
    /**
     * Compile a resource into compiled script.
     *
     * @param name resource name.
     * @return a compiled script object.
     * @throws IOException if error occurs while reading resource.
     */
    CompiledScript compile(String name) throws IOException;

    /**
     * Start a new thread with specified script.
     *
     * @param script   the script to be executed.
     * @param bindings bindings to pass external objects.
     * @return a new thread object.
     */
    EagleThread start(CompiledScript script, Bindings bindings);

    /**
     * Resume a paused thread.
     *
     * @param thread   the thread object.
     * @param bindings bindings to pass external objects.
     */
    void resume(EagleThread thread, Bindings bindings);
}
