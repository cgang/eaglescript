package org.eaglescript.compiler;

import org.eaglescript.lang.EagleScriptParser;
import org.eaglescript.lang.EagleScriptParserBaseVisitor;
import org.eaglescript.vm.OpCode;

class AssignmentExpressionVisitor extends EagleScriptParserBaseVisitor<CompilingResult> {
    private final ProgramVisitor visitor;
    private final LexicalEnvironment env;

    public AssignmentExpressionVisitor(ProgramVisitor visitor, LexicalEnvironment env) {
        this.visitor = visitor;
        this.env = env;
    }

    @Override
    public CompilingResult visitIdentifierExpression(EagleScriptParser.IdentifierExpressionContext ctx) {
        String identifier = ctx.identifier().getText();
        return new CompilingResult().add(OpCode.STORE, visitor.id(identifier));
    }

    @Override
    public CompilingResult visitMemberDotExpression(EagleScriptParser.MemberDotExpressionContext ctx) {
        CompilingResult result = ctx.singleExpression().accept(visitor);
        String identifier = ctx.identifierName().identifier().getText();
        result.add(OpCode.LOAD_CONST, visitor.constantTable.put(identifier));
        return result.add(OpCode.SET);
    }

    @Override
    public CompilingResult visitMemberIndexExpression(EagleScriptParser.MemberIndexExpressionContext ctx) {
        CompilingResult result = ctx.singleExpression().accept(visitor);
        result.append(ctx.expressionSequence().accept(visitor));
        return result.add(OpCode.SET);
    }

    @Override
    public CompilingResult visitAssignmentExpression(EagleScriptParser.AssignmentExpressionContext ctx) {
        CompilingResult result = ctx.singleExpression(1).accept(this.visitor);
        EagleScriptParser.SingleExpressionContext target = ctx.singleExpression(0);
        return visitor.aggregateResult(result, target.accept(this));
    }
}
