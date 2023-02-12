package org.eaglescript.compiler;

import java.nio.ByteBuffer;

public class ReferenceToken extends Token {
    private static final int SIZE = Byte.BYTES * 3;
    private int opcode;
    private PlaceHolder target;

    ReferenceToken(int opcode, PlaceHolder target) {
        this.opcode = opcode;
        this.target = target;
    }

    OpToken resolve(int offset, CompilingResult result) {
        int off = result.getOffset(target) - offset;
        return OpToken.of(opcode, (short) off);
    }

    @Override
    void appendTo(ByteBuffer buf) {
        throw new IllegalStateException("unresolved reference");
    }

    @Override
    int size() {
        return SIZE;
    }
}