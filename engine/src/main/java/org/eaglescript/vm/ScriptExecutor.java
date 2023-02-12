package org.eaglescript.vm;

import org.eaglescript.ScriptBoolean;
import org.eaglescript.ScriptFunction;

import static org.eaglescript.vm.OpCode.*;
import static org.eaglescript.vm.Operations.*;

/**
 * The {@link ScriptExecutor} is responsible for execution of byte code.
 */
public class ScriptExecutor {
    private static final ThreadLocal<ScriptExecutor> localExecutor = new ThreadLocal<>();

    /**
     * Get thread local executor instance.
     *
     * @return the thread local executor, null if not found.
     */
    public static ScriptExecutor get() {
        return localExecutor.get();
    }

    private EagleThread thread;

    public ScriptExecutor(EagleThread thread) {
        this.thread = thread;
    }

    Object execute(ScriptFrame frame) {
        while (true) {
            int opcode = frame.opcode();
            if (opcode < FLOW_CONTROL) {
                executeSimple(frame, opcode);
            } else if (opcode == INVOKE) {
                Object[] args = frame.popArgs(frame.operand());
                Object target = frame.pop();
                if (target instanceof ScriptFunction) {
                    frame = thread.newStack((ScriptFunction) target, frame.getContext(), args);
                } else if (target instanceof Callable) {
                    try {
                        ((Callable) target).invoke(args);
                    } catch (ScriptAwareException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    throw new RuntimeException("unsupported target: " + target);
                }
            } else if (opcode == RETURN) {
                Object retValue = frame.getReturn();
                frame = thread.popStack();
                if (frame == null) {
                    return retValue;
                }

                frame.push(retValue);
            }
        }
    }

    private void executeSimple(ScriptFrame frame, int opcode) {
        switch (opcode) {
            case NOP:
                break;
            case LOAD:
                frame.push(frame.get(frame.operand()));
                break;
            case STORE:
                frame.put(frame.operand(), frame.pop());
                break;
            case EQUAL:
                frame.push(equal(frame.pop(), frame.pop()));
                break;
            case NOT_EQUAL:
                frame.push(!equal(frame.pop(), frame.pop()));
                break;
            case S_EQUAL:
                frame.push(strictEqual(frame.pop(), frame.pop()));
                break;
            case S_NOT_EQUAL:
                frame.push(!strictEqual(frame.pop(), frame.pop()));
                break;
            case CMP_LT:
                frame.push(lessThan(frame.pop(), frame.pop()));
                break;
            case CMP_LE:
                frame.push(lessThanOrEqual(frame.pop(), frame.pop()));
                break;
            case CMP_GT:
                frame.push(greaterThan(frame.pop(), frame.pop()));
                break;
            case CMP_GE:
                frame.push(greaterThanOrEqual(frame.pop(), frame.pop()));
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
            case BIT_AND:
                frame.push(bitAnd(frame.pop(), frame.pop()));
                break;
            case BIT_OR:
                frame.push(bitOr(frame.pop(), frame.pop()));
                break;
            case BIT_XOR:
                frame.push(bitXOr(frame.pop(), frame.pop()));
                break;
            case BIT_NOT:
                frame.push(bitNot(frame.pop(), frame.pop()));
                break;
            case GET:
                frame.push(doGet(frame.pop(), frame.pop()));
                break;
            case SET:
                doSet(frame.pop(), frame.pop(), frame.pop());
                break;
            case LOAD_CONST:
                frame.push(frame.loadConst(frame.operand()));
                break;
            case GOTO:
                frame.jump(frame.operand());
                break;
            case IF_FALSE:
                frame.jump(frame.operand(), !ScriptBoolean.valueOf(frame.pop()));
                break;
            case IF_TRUE:
                frame.jump(frame.operand(), ScriptBoolean.valueOf(frame.pop()));
                break;
            default:
                throw new IllegalStateException("Unsupported opcode: " + nameOf(opcode));
        }
    }

}
