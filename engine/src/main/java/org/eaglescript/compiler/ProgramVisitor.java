package org.eaglescript.compiler;

import org.eaglescript.lang.EagleScriptParser;
import org.eaglescript.lang.EagleScriptParserBaseVisitor;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

class ProgramVisitor extends EagleScriptParserBaseVisitor<CompilingResult> {
    private final Compiler compiler;
    private final LinkedList<LexicalEnvironment> envStack = new LinkedList<>();
    private ModuleEnvironment module;

    private IdentifierTable identifierTable = new IdentifierTable();

    ProgramVisitor(Compiler compiler, ModuleEnvironment env) {
        this.compiler = compiler;
        this.module = env;
        envStack.add(env);
    }

    short id(String name) {
        return (short) identifierTable.put(name);
    }

    private LexicalEnvironment peek() {
        return envStack.getLast();
    }

    @Override
    public CompilingResult visitProgram(EagleScriptParser.ProgramContext ctx) {
        return super.visitProgram(ctx);
    }

    @Override
    public CompilingResult visitVariableDeclarationList(EagleScriptParser.VariableDeclarationListContext ctx) {
        AssignmentVisitor visitor = AssignmentVisitor.of(this, peek(), ctx.varModifier());
        return visitor.visitVariableDeclarationList(ctx);
    }

    @Override
    public CompilingResult visitVariableDeclaration(EagleScriptParser.VariableDeclarationContext ctx) {
        throw new IllegalStateException("not implemented");
    }

    @Override
    protected CompilingResult defaultResult() {
        return new CompilingResult();
    }

    @Override
    protected CompilingResult aggregateResult(CompilingResult aggregate, CompilingResult nextResult) {
        if (aggregate != null) {
            if (nextResult != null) {
                return aggregate.append(nextResult);
            } else {
                return aggregate;
            }
        } else {
            return nextResult;
        }
    }
}
