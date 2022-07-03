package org.eaglescript.compiler;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

class VariableEnvironment extends LexicalEnvironment {
    private Set<String> variables = new LinkedHashSet<>();
    private Set<String> unresolved = new HashSet<>();

    @Override
    boolean resolve(String name) throws CompilationException {
        if (super.resolve(name)) {
            return true;
        } else if (variables.contains(name)) {
            return true;
        } else {
            unresolved.add(name);
            return false;
        }
    }

    @Override
    void declareVariable(String name) throws CompilationException {
        super.checkDeclared(name);

        if (variables.contains(name)) {
            // re-declaration is allowed for var
            return;
        }

        if (visited.add(name) || unresolved.remove(name)) {
            variables.add(name);
        } else {
            throw new CompilationException(name + " declared in module was already used before declared");
        }
    }
}
