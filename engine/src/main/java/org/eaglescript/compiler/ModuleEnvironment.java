package org.eaglescript.compiler;

import org.eaglescript.vm.CodeSegment;
import org.eaglescript.vm.CompiledFunction;
import org.eaglescript.vm.CompiledScript;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

class ModuleEnvironment extends VariableEnvironment {
    private List<FunctionEnvironment> funcEnvs = new LinkedList<>();

    short addFunction(FunctionEnvironment env) {
        short index = (short) funcEnvs.size();
        this.funcEnvs.add(env);
        return index;
    }

    CompiledScript assemble(CompilingResult result, ProgramVisitor visitor) {
        List<CompiledFunction> functions = new ArrayList<>();
        for (FunctionEnvironment env : funcEnvs) {
            CompiledFunction func = env.assemble(visitor);
            functions.add(func);
        }

        CodeSegment segment = new CodeSegment(result.toCode(), visitor.getConstants(), visitor.getIdentifiers());
        return new CompiledScript(segment, functions.toArray(new CompiledFunction[0]));
    }
}
