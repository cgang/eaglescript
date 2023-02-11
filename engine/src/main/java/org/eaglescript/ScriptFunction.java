package org.eaglescript;

import org.eaglescript.vm.CodeSegment;

public class ScriptFunction {
    private final CodeSegment code;
    private final int argCount;

    public ScriptFunction(CodeSegment code, int argc) {
        this.code = code;
        this.argCount = argc;
    }

    public CodeSegment code() {
        return code;
    }

    /**
     * Get number of declared arguments.
     */
    public int argCount() {
        return argCount;
    }
}
