package org.eaglescript.compiler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.eaglescript.lang.EagleScriptParser;
import org.eaglescript.lang.EagleScriptParserBaseVisitor;
import org.eaglescript.vm.OpCode;

import java.util.LinkedList;
import java.util.List;

import static org.eaglescript.compiler.ConstantTable.NULL;

class ProgramVisitor extends EagleScriptParserBaseVisitor<CompilingResult> {
    private static ObjectMapper objectMapper = new ObjectMapper();

    private final Compiler compiler;
    private final LinkedList<LexicalEnvironment> envStack = new LinkedList<>();
    private ModuleEnvironment module;

    private IdentifierTable identifierTable = new IdentifierTable();
    ConstantTable constantTable = new ConstantTable();


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
        CompilingResult result = super.visitProgram(ctx);
        result.add(OpCode.RETURN);
        return result;
    }

    @Override
    public CompilingResult visitVariableDeclarationList(EagleScriptParser.VariableDeclarationListContext ctx) {
        DeclarationVisitor visitor = DeclarationVisitor.of(this, peek(), ctx.varModifier());
        return visitor.visitVariableDeclarationList(ctx);
    }

    @Override
    public CompilingResult visitAssignmentExpression(EagleScriptParser.AssignmentExpressionContext ctx) {
        AssignmentVisitor visitor = new AssignmentVisitor(this, peek());
        return visitor.visitAssignmentExpression(ctx);
    }

    @Override
    public CompilingResult visitVariableDeclaration(EagleScriptParser.VariableDeclarationContext ctx) {
        throw new IllegalStateException("not implemented");
    }

    @Override
    public CompilingResult visitFunctionDeclaration(EagleScriptParser.FunctionDeclarationContext ctx) {
        EagleScriptParser.FormalParameterListContext paramList = ctx.formalParameterList();

        LexicalEnvironment env = peek();
        FunctionEnvironment funcEnv = new FunctionEnvironment(paramList.formalParameterArg().size(), paramList.lastFormalParameterArg() != null);
        envStack.push(funcEnv);

        funcEnv.merge(paramList.accept(this));
        funcEnv.merge(ctx.functionBody().accept(this));

        short idx = module.addFunction(funcEnv);
        envStack.pop();

        String identifier = ctx.identifier().getText();
        env.declareVariable(identifier);
        CompilingResult result = new CompilingResult();
        result.add(OpCode.LOAD_FUNC, idx);
        result.add(OpCode.STORE, this.id(identifier));
        return result;
    }

    @Override
    public CompilingResult visitFormalParameterList(EagleScriptParser.FormalParameterListContext ctx) {
        LexicalEnvironment env = peek();
        DeclarationVisitor visitor = DeclarationVisitor.forParameter(this, env);

        CompilingResult result = defaultResult();
        for (EagleScriptParser.FormalParameterArgContext argCtx : ctx.formalParameterArg()) {
            result.append(visitor.visitFormalParameterArg(argCtx));
        }
        EagleScriptParser.LastFormalParameterArgContext lastArg = ctx.lastFormalParameterArg();
        if (lastArg != null) {
            result.append(visitor.visitLastFormalParameterArg(lastArg));
        }

        return result;
    }

    @Override
    public CompilingResult visitFunctionBody(EagleScriptParser.FunctionBodyContext ctx) {
        return super.visitFunctionBody(ctx);
    }

    @Override
    public CompilingResult visitReturnStatement(EagleScriptParser.ReturnStatementContext ctx) {
        CompilingResult result = ctx.expressionSequence().accept(this);
        result.add(OpCode.RETURN);
        return result;
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
    public CompilingResult visitUnaryMinusExpression(EagleScriptParser.UnaryMinusExpressionContext ctx) {
        CompilingResult result = ctx.singleExpression().accept(this);
        return result.add(OpCode.NEG);
    }

    @Override
    public CompilingResult visitUnaryPlusExpression(EagleScriptParser.UnaryPlusExpressionContext ctx) {
        return super.visitUnaryPlusExpression(ctx);
    }

    @Override
    public CompilingResult visitBitAndExpression(EagleScriptParser.BitAndExpressionContext ctx) {
        CompilingResult result = super.visitBitAndExpression(ctx);
        return result.add(OpCode.BIT_AND);
    }

    @Override
    public CompilingResult visitBitOrExpression(EagleScriptParser.BitOrExpressionContext ctx) {
        CompilingResult result = super.visitBitOrExpression(ctx);
        return result.add(OpCode.BIT_OR);
    }

    @Override
    public CompilingResult visitBitXOrExpression(EagleScriptParser.BitXOrExpressionContext ctx) {
        CompilingResult result = super.visitBitXOrExpression(ctx);
        return result.add(OpCode.BIT_XOR);
    }

    @Override
    public CompilingResult visitBitNotExpression(EagleScriptParser.BitNotExpressionContext ctx) {
        CompilingResult result = ctx.singleExpression().accept(this);
        return result.add(OpCode.BIT_NOT);
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
            String value = unescape(node.getText());
            return defaultResult().add(OpCode.LOAD_CONST, constantTable.put(value));
        } else {
            return super.visitChildren(ctx);
        }
    }

    private static String unescape(String text) {
        try {
            return objectMapper.readValue(text, String.class);
        } catch (JsonProcessingException e) {
            throw new CompilationException("Malformed text: " + text);
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
    public CompilingResult visitLogicalAndExpression(EagleScriptParser.LogicalAndExpressionContext ctx) {
        // TODO support operator precedence
        PlaceHolder no = new PlaceHolder("no");
        PlaceHolder end = new PlaceHolder("end");
        return ctx.singleExpression(0).accept(this)
                .addRef(OpCode.IF_FALSE, no)
                .append(ctx.singleExpression(1).accept(this))
                .addRef(OpCode.IF_FALSE, no)
                .add(OpCode.LOAD_CONST, ConstantTable.TRUE)
                .addRef(OpCode.GOTO, end)
                .add(no)
                .add(OpCode.LOAD_CONST, ConstantTable.FALSE)
                .add(end);
    }

    @Override
    public CompilingResult visitLogicalOrExpression(EagleScriptParser.LogicalOrExpressionContext ctx) {
        // TODO support operator precedence
        PlaceHolder yes = new PlaceHolder("yes");
        PlaceHolder end = new PlaceHolder("end");

        return ctx.singleExpression(0).accept(this)
                .addRef(OpCode.IF_TRUE, yes)
                .append(ctx.singleExpression(1).accept(this))
                .addRef(OpCode.IF_TRUE, yes)
                .add(OpCode.LOAD_CONST, ConstantTable.FALSE)
                .addRef(OpCode.GOTO, end)
                .add(yes)
                .add(OpCode.LOAD_CONST, ConstantTable.TRUE)
                .add(end);
    }

    @Override
    public CompilingResult visitRelationalExpression(EagleScriptParser.RelationalExpressionContext ctx) {
        CompilingResult result = super.visitRelationalExpression(ctx);
        if (ctx.LessThan() != null) {
            return result.add(OpCode.CMP_LT);
        } else if (ctx.MoreThan() != null) {
            return result.add(OpCode.CMP_GT);
        } else if (ctx.LessThanEquals() != null) {
            return result.add(OpCode.CMP_LE);
        } else if (ctx.GreaterThanEquals() != null) {
            return result.add(OpCode.CMP_GE);
        } else {
            throw new RuntimeException("Unsupported relational expression: " + ctx.getText());
        }
    }

    @Override
    public CompilingResult visitEqualityExpression(EagleScriptParser.EqualityExpressionContext ctx) {
        CompilingResult result = super.visitEqualityExpression(ctx);
        if (ctx.Equals_() != null) {
            return result.add(OpCode.EQUAL);
        } else if (ctx.NotEquals() != null) {
            return result.add(OpCode.NOT_EQUAL);
        } else if (ctx.IdentityEquals() != null) {
            return result.add(OpCode.S_EQUAL);
        } else if (ctx.IdentityNotEquals() != null) {
            return result.add(OpCode.S_NOT_EQUAL);
        } else {
            throw new RuntimeException("Unsupported equality expression: " + ctx.getText());
        }
    }

    @Override
    public CompilingResult visitIfStatement(EagleScriptParser.IfStatementContext ctx) {
        CompilingResult result = ctx.expressionSequence().accept(this);
        EagleScriptParser.StatementContext ifClause = ctx.statement(0);
        EagleScriptParser.StatementContext elseClause = ctx.statement(1);

        PlaceHolder end = new PlaceHolder("end");
        if (elseClause == null) {
            result.addRef(OpCode.IF_FALSE, end);
            result = this.aggregateResult(result, ifClause.accept(this));
        } else {
            PlaceHolder otherwise = new PlaceHolder("else");
            result.addRef(OpCode.IF_FALSE, otherwise);
            result = this.aggregateResult(result, ifClause.accept(this));
            result.addRef(OpCode.GOTO, end)
                    .add(otherwise);
            result = this.aggregateResult(result, elseClause.accept(this));
        }

        return result.add(end);
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
