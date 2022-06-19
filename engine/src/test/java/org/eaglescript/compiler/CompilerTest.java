package org.eaglescript.compiler;

import org.eaglescript.vm.CompiledScript;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class CompilerTest {
    /**
     * Compile specified resource.
     * @param name a resource name.
     * @return a compiled script.
     */
    static CompiledScript compile(String name) throws IOException, CompilationException {
        InputStream input = CompilerTest.class.getResourceAsStream(name);
        Reader reader = new InputStreamReader(input, StandardCharsets.UTF_8);

        Compiler compiler = new Compiler();
        return compiler.compile(reader, name);
    }

    @Test
    public void testSimple() throws CompilationException, IOException {
        CompiledScript s = compile("simple.egs");
        assertNotNull(s);
    }
}