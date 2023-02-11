package org.eaglescript.compiler;

import org.eaglescript.vm.CompiledScript;

import java.nio.ByteBuffer;

class ModuleEnvironment extends VariableEnvironment {
    CompiledScript assemble(CompilingResult result) {
        return new CompiledScript(result.toCodeSegment());
    }
}
