package org.eaglescript.vm;

import java.io.PrintStream;
import java.util.Arrays;

import static org.eaglescript.vm.OpCode.*;

/**
 * A {@link CodeSegment} represents a collection of executable instructions.
 */
public class CodeSegment {
    private final byte[] code;

    public CodeSegment(byte[] code) {
        this.code = code;
    }

    /**
     * Returns opcode at specified offset.
     * @param offset the offset.
     */
    int opcode(int offset) {
        return code[offset] & 0xFF;
    }

    /**
     * Returns operand at specified offset.
     * @param offset the offset.
     */
    int operand(int offset) {
        int b1 = code[offset] & 0xFF;
        int b2 = code[offset + 1] & 0xFF;
        return (b1 << 8) | b2;
    }

    /**
     * Dump this segment to specified output stream.
     * @param out a print stream.
     */
    public void dump(PrintStream out) {
        int offset = 0;
        while (offset < code.length) {
            int pc = offset;
            int opcode = opcode(offset++);
            switch (opcode) {
                case PLUS:
                case RETURN:
                    out.println(pc + "\t" + nameOf(opcode));
                    break;
                case LOAD:
                case STORE:
                case LOAD_CONST:
                    out.println(pc + "\t" + nameOf(opcode) + "\t" + operand(offset));
                    offset += 2;
                    break;
                default:
                    throw new RuntimeException("Unsupported opcode: " + nameOf(opcode));
            }
        }
    }
}
