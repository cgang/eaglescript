package org.eaglescript.compiler;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

public abstract class LexicalEnvironment {
    protected Set<String> declared = new LinkedHashSet<>();
    protected Set<String> visited = new HashSet<>();

    void declareLexical(String name) throws CompilationException {
        if (visited.add(name)) {
            if (declared.add(name)) {
                return;
            } else {
                throw new CompilationException(name + " has already been declared");
            }
        } else {
            throw new CompilationException(name + " has already been used or declared");
        }
    }

    boolean resolve(String name) throws CompilationException {
        visited.add(name);
        return declared.contains(name);
    }

    abstract void declareVariable(String name) throws CompilationException;

    void checkDeclared(String name) throws CompilationException {
        if (declared.contains(name)) {
            throw new CompilationException(name + " was already declared.");
        }
    }
}
