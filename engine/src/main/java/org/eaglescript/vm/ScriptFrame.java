package org.eaglescript.vm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

class ScriptFrame extends Frame implements Serializable {
    private static final long serialVersionUID = -1627459082291792661L;

    private transient CompiledScript script;
    private transient CodeSegment text;

    private ExecutionContext context;

    /**
     * The program counter
     */
    private int pc = 0;

    private List<Object> operandStack = new ArrayList<>();

    /**
     * Construct a script frame from a code segment.
     * @param text the code segment for this frame.
     */
    public ScriptFrame(CodeSegment text) {
        this.text = text;
    }

    /**
     * Read next opcode from the text and advance the program counter.
     * @return an opcode.
     */
    int opcode() {
        return text.opcode(pc++);
    }

    /**
     * Read next operand from the text and advance the program counter.
     * @return an operand.
     */
    short operand() {
        short operand = (short) text.operand(pc);
        pc += 2;
        return operand;
    }

    /**
     * Get a binding from execution context.
     * @param index the variable index.
     * @return an object reference.
     */
    Object get(short index) {
        return context.get(script.resolve(index));
    }

    /**
     * Put a binding to execution context.
     * @param index the variable index.
     * @param object an object reference.
     */
    void put(short index, Object object) {
        context.put(script.resolve(index), object);
    }

    void push(Object object) {
        operandStack.add(object);
    }

    Object pop() {
        return operandStack.remove(operandStack.size() - 1);
    }
}
