package org.eaglescript.vm;

import java.io.PrintStream;

public class CompiledScript {

    private final CodeSegment mainCode;

    private CompiledFunction[] functions;

    public CompiledScript(CodeSegment mainCode, CompiledFunction[] functions) {
        this.mainCode = mainCode;
        this.functions = functions;
    }

    public CodeSegment code() {
        return mainCode;
    }


    CompiledFunction getFunction(short index) {
        return functions[index];
    }

    String resolve(short index) {
        return mainCode.resolve(index);
    }

    public void dump(PrintStream out) {
        out.println("==== main ====");
        mainCode.dump(out);
        for (int i = 0; i < functions.length; i++) {
            out.println("---- function " + i + " ----");
            functions[i].getCode().dump(out);
        }
    }
}
