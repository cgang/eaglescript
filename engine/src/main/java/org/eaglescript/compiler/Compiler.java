package org.eaglescript.compiler;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.eaglescript.lang.EagleScriptLexer;
import org.eaglescript.lang.EagleScriptParser;
import org.eaglescript.vm.CompiledScript;

import java.io.IOException;
import java.io.Reader;

public class Compiler {
    /**
     * Compile a script from a reader and source name.
     * @param reader the reader for the source script.
     * @param sourceName the source name for the script.
     * @return a compiled script.
     * @throws IOException if error occurs while reading from the reader.
     * @throws CompilationException if the source code cannot be compiled successfully.
     */
    public CompiledScript compile(Reader reader, String sourceName) throws IOException, CompilationException {
        CharStream stream = CharStreams.fromReader(reader, sourceName);
        EagleScriptLexer lexer = new EagleScriptLexer(stream);
        EagleScriptParser parser = new EagleScriptParser(new CommonTokenStream(lexer));

        ModuleEnvironment moduleEnv = new ModuleEnvironment();
        ProgramVisitor visitor = new ProgramVisitor(this, moduleEnv);
        EagleScriptParser.ProgramContext program = parser.program();
        CompilingResult result = visitor.visitProgram(program);
        return moduleEnv.assemble(result);
    }
}
