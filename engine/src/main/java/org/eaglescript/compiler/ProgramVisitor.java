package org.eaglescript.compiler;

import org.antlr.v4.runtime.tree.TerminalNode;
import org.eaglescript.lang.EagleScriptParser;
import org.eaglescript.lang.EagleScriptParserBaseVisitor;
import org.eaglescript.vm.OpCode;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

class ProgramVisitor extends EagleScriptParserBaseVisitor<CompilingResult> {
    private final Compiler compiler;
    private final LinkedList<LexicalEnvironment> envStack = new LinkedList<>();
    private ModuleEnvironment module;

    private IdentifierTable identifierTable = new IdentifierTable();
    private ConstantTable constantTable = new ConstantTable();

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
    public CompilingResult visitMultiplicativeExpression(EagleScriptParser.MultiplicativeExpressionContext ctx) {
        CompilingResult result = super.visitMultiplicativeExpression(ctx);
        if (ctx.Multiply() != null) {
            return result.add(OpCode.MUL);
        } else if (ctx.Divide() != null) {
            return result.add(OpCode.DIV);
        } else if (ctx.Modulus() != null) {
            return result.add(OpCode.MOD);
        } else {
            throw new CompilationException("Unsupported multiplicative expression: " + ctx.getText());
        }
    }

    @Override
    public CompilingResult visitAdditiveExpression(EagleScriptParser.AdditiveExpressionContext ctx) {
        CompilingResult result = super.visitAdditiveExpression(ctx);
        if (ctx.Plus() != null) {
            return result.add(OpCode.PLUS);
        } else if (ctx.Minus() != null) {
            return result.add(OpCode.SUB);
        } else {
            throw new CompilationException("Unsupported additive expression: " + ctx.getText());
        }
    }

    @Override
    public CompilingResult visitNumericLiteral(EagleScriptParser.NumericLiteralContext ctx) {
        CompilingResult result = defaultResult();
        TerminalNode node;
        if ((node = ctx.DecimalLiteral()) != null) {
            double value = Double.parseDouble(node.getText().replace("_", ""));
            return result.add(OpCode.LOAD_CONST, constantTable.put(value));
        } else if ((node = ctx.HexIntegerLiteral()) != null) {
            double value = Long.parseLong(node.getText().substring(2).replace("_", ""), 16);
            return result.add(OpCode.LOAD_CONST, constantTable.put(value));
        } else if ((node = ctx.OctalIntegerLiteral()) != null) {
            double value = Long.parseLong(node.getText().substring(2).replace("_", ""), 8);
            return result.add(OpCode.LOAD_CONST, constantTable.put(value));
        } else if ((node = ctx.BinaryIntegerLiteral()) != null) {
            double value = Long.parseLong(node.getText().substring(2).replace("_", ""), 2);
            return result.add(OpCode.LOAD_CONST, constantTable.put(value));
        } else {
            throw new CompilationException("Unsupported numeric literal: " + ctx.getText());
        }
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
