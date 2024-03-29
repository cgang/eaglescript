package org.eaglescript.vm;

import java.io.PrintStream;

import static org.eaglescript.vm.OpCode.*;

/**
 * A {@link CodeSegment} represents a collection of executable instructions.
 */
public class CodeSegment {
    private final byte[] code;
    private final Object[] constants;
    private String[] identifiers;

    public CodeSegment(byte[] code, Object[] constants, String[] identifiers) {
        this.code = code;
        this.constants = constants;
        this.identifiers = identifiers;
    }

    /**
     * Returns opcode at specified offset.
     *
     * @param offset the offset.
     */
    int opcode(int offset) {
        return code[offset] & 0xFF;
    }

    /**
     * Returns operand at specified offset.
     *
     * @param offset the offset.
     */
    int operand(int offset) {
        int b1 = code[offset] & 0xFF;
        int b2 = code[offset + 1] & 0xFF;
        return (b1 << 8) | b2;
    }

    Object getConst(short index) {
        return constants[index];
    }

    /**
     * Resolve an index to identifier.
     *
     * @param index the index of identifier.
     * @return an identifier
     * @throws IllegalStateException if the index is not valid.
     */
    public String resolve(short index) {
        return identifiers[index & 0xFFFF];
    }

    /**
     * Dump this segment to specified output stream.
     *
     * @param out a print stream.
     */
    public void dump(PrintStream out) {
        CodeVisitor visitor = new CodeVisitor(this);
        int pc = 0;
        while ((pc = visitor.programCounter()) < code.length) {
            int opcode = visitor.opcode();
            switch (opcode) {
                case EQUAL:
                case NOT_EQUAL:
                case S_EQUAL:
                case CMP_LT:
                case CMP_LE:
                case CMP_GT:
                case CMP_GE:
                case PLUS:
                case ADD:
                case SUB:
                case MUL:
                case DIV:
                case NEG:
                case BIT_AND:
                case BIT_OR:
                case BIT_XOR:
                case BIT_NOT:
                case GET:
                case SET:
                case RETURN:
                    out.println(pc + "\t" + nameOf(opcode));
                    break;
                case LOAD:
                case STORE:
                    out.println(pc + "\t" + nameOf(opcode) + "\t$" + resolve(visitor.operand()));
                    break;
                case LOAD_CONST:
                    out.println(pc + "\t" + nameOf(opcode) + "\t" + getConst(visitor.operand()));
                    break;
                case LOAD_FUNC:
                    out.println(pc + "\t" + nameOf(opcode) + "\t%" + visitor.operand());
                    break;
                case GOTO:
                case IF_TRUE:
                case IF_FALSE:
                case INVOKE:
                    out.println(pc + "\t" + nameOf(opcode) + "\t" + visitor.operand());
                    break;
                default:
                    throw new RuntimeException("Unsupported opcode: " + nameOf(opcode));
            }
        }
    }
}
