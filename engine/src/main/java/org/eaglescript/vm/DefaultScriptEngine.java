package org.eaglescript.vm;

import org.eaglescript.Bindings;
import org.eaglescript.compiler.Compiler;

import java.io.IOException;

public class DefaultScriptEngine implements ScriptEngine {
    private ResourceLoader loader;

    public DefaultScriptEngine(ResourceLoader loader) {
        this.loader = loader;
    }

    public CompiledScript compile(String name) throws IOException {
        Compiler compiler = new Compiler(loader);
        return compiler.compile(name);
    }

    @Override
    public EagleThread start(CompiledScript script, Bindings bindings) {
        EagleThread thread = new EagleThread(GlobalSequence.nextId(), script, new GlobalObject(bindings));
        ScriptExecutor executor = new ScriptExecutor(thread);
        ScriptFrame frame = thread.start(script);
        executor.execute(frame);

        return thread;
    }

    @Override
    public void resume(EagleThread thread, Bindings bindings) {
        // TODO add implementation
        throw new UnsupportedOperationException("not implemented yet");
    }
}
