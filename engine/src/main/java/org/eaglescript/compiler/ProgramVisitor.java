package org.eaglescript.compiler;

import org.antlr.v4.runtime.tree.TerminalNode;
import org.eaglescript.lang.EagleScriptParser;
import org.eaglescript.lang.EagleScriptParserBaseVisitor;
import org.eaglescript.vm.OpCode;

import java.util.LinkedList;
import java.util.List;

import static org.eaglescript.compiler.ConstantTable.NULL;

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

    Object[] getConstants() {
        return constantTable.toArray();
    }

    String[] getIdentifiers() {
        return identifierTable.toArray();
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
    public CompilingResult visitIdentifierExpression(EagleScriptParser.IdentifierExpressionContext ctx) {
        String identifier = ctx.identifier().getText();
        return defaultResult().add(OpCode.LOAD, id(identifier));
    }

    @Override
    public CompilingResult visitMemberDotExpression(EagleScriptParser.MemberDotExpressionContext ctx) {
        CompilingResult result = ctx.singleExpression().accept(this);
        String identifier = ctx.identifierName().identifier().getText();
        return result.add(OpCode.LOAD_CONST, constantTable.put(identifier))
                .add(OpCode.GET);
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
    public CompilingResult visitLiteral(EagleScriptParser.LiteralContext ctx) {
        TerminalNode node;
        if (ctx.NullLiteral() != null) {
            return defaultResult().add(OpCode.LOAD_CONST, NULL);
        } else if ((node = ctx.BooleanLiteral()) != null) {
            boolean value = Boolean.parseBoolean(node.getText());
            return defaultResult().add(OpCode.LOAD_CONST, constantTable.put(value));
        } else if ((node = ctx.StringLiteral()) != null) {
            String value = node.getText();
            return defaultResult().add(OpCode.LOAD_CONST, constantTable.put(value));
        } else {
            return super.visitChildren(ctx);
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
    public CompilingResult visitArgumentsExpression(EagleScriptParser.ArgumentsExpressionContext ctx) {
        CompilingResult result = ctx.singleExpression().accept(this);
        return result.append(this.visitArguments(ctx.arguments()));
    }

    @Override
    public CompilingResult visitArguments(EagleScriptParser.ArgumentsContext ctx) {
        CompilingResult result = defaultResult();
        List<EagleScriptParser.ArgumentContext> arguments = ctx.argument();
        for (EagleScriptParser.ArgumentContext arg : arguments) {
            result.append(arg.accept(this));
        }

        return result.add(OpCode.INVOKE, (short) arguments.size());
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
