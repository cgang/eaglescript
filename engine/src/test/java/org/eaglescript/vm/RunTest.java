package org.eaglescript.vm;

import org.eaglescript.util.ClassResourceLoader;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class RunTest {
    @Test
    public void testSimple() throws IOException {
        ResourceLoader loader = new ClassResourceLoader(RunTest.class);

        ScriptEngine engine = new DefaultScriptEngine(loader);

        CompiledScript script = engine.compile("simple.egs");
        engine.start(script, null, null);
    }
}