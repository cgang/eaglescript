package org.eaglescript.compiler;

import org.eaglescript.vm.CodeSegment;
import org.eaglescript.vm.CompiledFunction;

class FunctionEnvironment extends VariableEnvironment {
    int argCount;
    boolean varArgs;
    CompilingResult result = new CompilingResult();

    public FunctionEnvironment(int argc, boolean varArgs) {
        this.argCount = argc;
        this.varArgs = varArgs;
    }

    void merge(CompilingResult result) {
        if (result != null) {
            this.result.append(result);
        }
    }

    CompiledFunction assemble(ProgramVisitor visitor) {
        CodeSegment segment = new CodeSegment(result.toCode(), visitor.getConstants(), visitor.getIdentifiers());
        // TODO add support for var args
        return new CompiledFunction(segment, argCount);
    }
}
