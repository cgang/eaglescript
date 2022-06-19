package org.eaglescript.vm;

import static org.eaglescript.vm.Instructions.*;
import static org.eaglescript.vm.Operations.*;

/**
 * The {@link ScriptExecutor} is responsible for execution of byte code.
 */
public class ScriptExecutor {
    private static final ThreadLocal<ScriptExecutor> localExecutor = new ThreadLocal<>();

    /**
     * Get thread local executor instance.
     * @return the thread local executor, null if not found.
     */
    public static ScriptExecutor get() {
        return localExecutor.get();
    }

    void execute(ScriptFrame frame) {
        while (true) {
            int opcode = frame.opcode();
            if (opcode < SIMPLE) {
                executeSimple(frame, opcode);
            }
        }
    }

    private void executeSimple(ScriptFrame frame, int opcode) {
        switch (opcode) {
            case LOAD:
                frame.push(frame.get(frame.operand()));
                break;
            case STORE:
                frame.put(frame.operand(), frame.pop());
                break;
            case PLUS:
                frame.push(plus(frame.pop(), frame.pop()));
                break;
            case ADD:
                frame.push(add(frame.pop(), frame.pop()));
                break;
            case SUB:
                frame.push(sub(frame.pop(), frame.pop()));
                break;
            case MUL:
                frame.push(mul(frame.pop(), frame.pop()));
                break;
            case DIV:
                frame.push(div(frame.pop(), frame.pop()));
                break;
            case NEG:
                frame.push(negate(frame.pop()));
                break;
            default:
                throw new IllegalStateException("Unsupported opcode: " + nameOf(opcode));
        }
    }
}
