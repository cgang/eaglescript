package org.eaglescript.compiler;

import org.eaglescript.lang.EagleScriptParser;
import org.eaglescript.lang.EagleScriptParserBaseVisitor;

import java.util.ArrayList;
import java.util.List;

public class ProgramVisitor extends EagleScriptParserBaseVisitor<CompilingResult> {
    private final List<LexicalEnvironment> envStack = new ArrayList<>();

    ProgramVisitor(ModuleEnvironment env) {
        envStack.add(env);
    }

    @Override
    public CompilingResult visitProgram(EagleScriptParser.ProgramContext ctx) {
        return super.visitProgram(ctx);
    }
}
