package org.eaglescript.vm;

import java.io.PrintStream;

public class CompiledScript {
    private String[] identifiers;

    private final CodeSegment mainCode;

    public CompiledScript(CodeSegment mainCode, String[] identifiers) {
        this.mainCode = mainCode;
        this.identifiers = identifiers;
    }

    public CodeSegment code() {
        return mainCode;
    }

    /**
     * Resolve an index to identifier.
     *
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
