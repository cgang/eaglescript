package org.eaglescript.vm;

class CodeVisitor {
    transient CodeSegment text;

    /**
     * The program counter
     */
    private int pc = 0;

    CodeVisitor(CodeSegment text) {
        this.text = text;
    }

    int programCounter() {
        return pc;
    }

    /**
     * Read next opcode from the text and advance the program counter.
     *
     * @return an opcode.
     */
    int opcode() {
        return text.opcode(pc++);
    }

    /**
     * Read next operand from the text and advance the program counter.
     *
     * @return an operand.
     */
    short operand() {
        short operand = (short) text.operand(pc);
        pc += 2;
        return operand;
    }

    Object loadConst(short index) {
        return text.getConst(index);
    }
}
