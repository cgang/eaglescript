package org.eaglescript.vm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

class ScriptFrame extends CodeVisitor implements Frame, Serializable {
    private static final long serialVersionUID = -1627459082291792661L;

    private transient CompiledScript script;

    private ExecutionContext context;

    private List<Object> operandStack = new ArrayList<>();

    /**
     * Construct a script frame from a code segment.
     *
     * @param text the code segment for this frame.
     */
    public ScriptFrame(CompiledScript script, ExecutionContext context, CodeSegment text) {
        super(text);
        this.script = script;
        this.context = new LexicalEnvContext(context);
    }

    /**
     * Get a binding from execution context.
     *
     * @param index the variable index.
     * @return an object reference.
     */
    Object get(short index) {
        return context.get(script.resolve(index));
    }

    ExecutionContext getContext() {
        return context;
    }

    /**
     * Put a binding to execution context.
     *
     * @param index  the variable index.
     * @param object an object reference.
     */
    void put(short index, Object object) {
        context.put(script.resolve(index), object);
    }

    void push(Object object) {
        operandStack.add(object);
    }

    void push(Object[] objects) {
        for (Object obj : objects) {
            operandStack.add(obj);
        }
    }

    Object pop() {
        return operandStack.remove(operandStack.size() - 1);
    }

    Object getReturn() {
        if (operandStack.isEmpty()) {
            return null;
        }

        return operandStack.remove(operandStack.size() - 1);
    }

    Object[] popArgs(int n) {
        ArrayList<Object> objects = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            objects.add(0, pop());
        }
        return objects.toArray();
    }

    @Override
    public StackTraceElement toStackTrace() {
        return null;
    }

}
