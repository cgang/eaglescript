package org.eaglescript.vm;

import static org.eaglescript.vm.Instructions.*;

/**
 * The {@link ScriptExecutor} is responsible for execution of byte code.
 */
public class ScriptExecutor {
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
        }
    }
}
