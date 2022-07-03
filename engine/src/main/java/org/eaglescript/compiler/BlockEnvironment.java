package org.eaglescript.compiler;

public class BlockEnvironment extends LexicalEnvironment {
    private LexicalEnvironment parent;

    @Override
    boolean resolve(String name) throws CompilationException {
        if (super.resolve(name)) {
            return true;
        } else {
            return parent.resolve(name);
        }
    }

    @Override
    void declareVariable(String name) throws CompilationException {
        super.checkDeclared(name);

        visited.add(name);
        parent.declareVariable(name);
    }
}
