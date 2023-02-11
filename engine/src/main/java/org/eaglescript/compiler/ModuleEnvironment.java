package org.eaglescript.compiler;

import org.eaglescript.vm.CodeSegment;
import org.eaglescript.vm.CompiledScript;

class ModuleEnvironment extends VariableEnvironment {
    CompiledScript assemble(CompilingResult result, ProgramVisitor visitor) {
        CodeSegment segment = new CodeSegment(result.toCode(), visitor.getConstants());
        return new CompiledScript(segment, visitor.getIdentifiers());
    }
}
