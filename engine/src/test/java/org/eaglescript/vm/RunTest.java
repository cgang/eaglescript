package org.eaglescript.vm;

import org.eaglescript.Bindings;
import org.eaglescript.util.ClassResourceLoader;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class RunTest {
    private static void runScript(String name) throws IOException {
        ResourceLoader loader = new ClassResourceLoader(RunTest.class);

        ScriptEngine engine = new DefaultScriptEngine(loader);

        CompiledScript script = engine.compile(name);
        engine.start(script, Bindings.newDefault());
    }

    @Test
    public void testSimple() throws IOException {
        runScript("simple.egs");
    }

    @Test
    public void testIfElse() throws IOException {
        runScript("ifelse.egs");
    }

    @Test
    public void testFunction() throws IOException {
        runScript("function.egs");
    }
}
