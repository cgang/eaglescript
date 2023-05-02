package org.eaglescript.vm;

public class CompiledFunction {
    private final CodeSegment code;
    private final int argc;

    public CompiledFunction(CodeSegment code, int argc) {
        this.code = code;
        this.argc = argc;
    }

    public CodeSegment getCode() {
        return code;
    }

    public int getArgCount() {
        return argc;
    }
}
