package org.eaglescript.compiler;

import org.eaglescript.vm.CompiledScript;

class ModuleEnvironment extends VariableEnvironment {
    CompiledScript assemble(CompilingResult result) {
        return new CompiledScript();
    }
}
