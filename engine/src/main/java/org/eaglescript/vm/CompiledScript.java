package org.eaglescript.vm;

import java.io.PrintStream;

public class CompiledScript {
    private String[] identifiers;

    private CodeSegment mainCode;

    public CompiledScript(CodeSegment mainCode) {
        this.mainCode = mainCode;
    }

    /**
     * Resolve an index to identifier.
     * @param index the index of identifier.
     * @return an identifier
     * @throws IllegalStateException if the index is not valid.
     */
    public String resolve(short index) {
        return identifiers[index & 0xFFFF];
    }

    public void dump(PrintStream out) {
        mainCode.dump(out);
    }
}
