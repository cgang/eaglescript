package org.eaglescript.compiler;

import org.eaglescript.lang.EagleScriptParser;
import org.eaglescript.lang.EagleScriptParserBaseVisitor;
import org.eaglescript.vm.OpCode;

import static org.eaglescript.compiler.ConstantTable.UNDEFINED;

class DeclarationVisitor extends EagleScriptParserBaseVisitor<CompilingResult> {
    static class VariableDeclarationVisitor extends DeclarationVisitor {
        VariableDeclarationVisitor(ProgramVisitor visitor, LexicalEnvironment env) {
            super(visitor, env);
        }

        @Override
        protected short resolve(String name) throws CompilationException {
            env.declareVariable(name);
            return visitor.id(name);
        }
    }


    static class LexicalDeclarationVisitor extends DeclarationVisitor {
        LexicalDeclarationVisitor(ProgramVisitor visitor, LexicalEnvironment env) {
            super(visitor, env);
        }

        @Override
        protected short resolve(String name) throws CompilationException {
            env.declareLexical(name);
            return visitor.id(name);
        }
    }

    static class ConstDeclarationVisitor extends DeclarationVisitor {
        ConstDeclarationVisitor(ProgramVisitor visitor, LexicalEnvironment env) {
            super(visitor, env);
        }

        @Override
        protected short resolve(String name) throws CompilationException {
            // TODO support const
            env.declareLexical(name);
            return visitor.id(name);
        }
    }

    static DeclarationVisitor of(ProgramVisitor visitor, LexicalEnvironment env, EagleScriptParser.VarModifierContext ctx) {
        if (ctx.Var() != null) {
            return new VariableDeclarationVisitor(visitor, env);
        } else if (ctx.let_() != null) {
            return new LexicalDeclarationVisitor(visitor, env);
        } else if (ctx.Const() != null) {
            return new ConstDeclarationVisitor(visitor, env);
        } else {
            throw new CompilationException("Unknown context: " + ctx.getText());
        }
    }

    protected final ProgramVisitor visitor;
    protected final LexicalEnvironment env;

    DeclarationVisitor(ProgramVisitor visitor, LexicalEnvironment env) {
        this.visitor = visitor;
        this.env = env;
    }

    protected short resolve(String name) throws CompilationException {
        env.resolve(name);
        return visitor.id(name);
    }

    @Override
    protected CompilingResult defaultResult() {
        return new CompilingResult();
    }

    @Override
    public CompilingResult visitAssignable(EagleScriptParser.AssignableContext ctx) {
        if (ctx.identifier() != null) {
            String identifier = ctx.identifier().Identifier().getText();
            short index = resolve(identifier);
            return defaultResult().add(OpCode.STORE, index);
        } else if (ctx.arrayLiteral() != null) {
            throw new CompilationException("Not implemented.");
        } else if (ctx.objectLiteral() != null) {
            throw new CompilationException("Not implemented.");
        } else {
            throw new IllegalStateException("Unknown assignable: " + ctx.getText());
        }
    }

    @Override
    public CompilingResult visitVariableDeclaration(EagleScriptParser.VariableDeclarationContext ctx) {
        CompilingResult result;
        EagleScriptParser.SingleExpressionContext expr = ctx.singleExpression();
        if (expr == null) {
            result = defaultResult().add(OpCode.LOAD_CONST, UNDEFINED);
        } else {
            result = expr.accept(this.visitor);
        }
        result.append(this.visitAssignable(ctx.assignable()));
        return result;
    }
}
