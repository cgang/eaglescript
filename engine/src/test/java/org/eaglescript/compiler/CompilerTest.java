package org.eaglescript.compiler;

import org.eaglescript.util.ClassResourceLoader;
import org.eaglescript.vm.CompiledScript;
import org.eaglescript.vm.ResourceLoader;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class CompilerTest {
    /**
     * Compile specified resource.
     * @param name a resource name.
     * @return a compiled script.
     */
    static CompiledScript compile(String name) throws IOException, CompilationException {
        ResourceLoader loader = new ClassResourceLoader(CompilerTest.class);

        Compiler compiler = new Compiler(loader);
        return compiler.compile(name);
    }

    @Test
    public void testSimple() throws CompilationException, IOException {
        CompiledScript s = compile("simple.egs");
        assertNotNull(s);
        s.dump(System.out);
    }
}