package org.eaglescript.compiler;


import java.nio.ByteBuffer;

class OpToken extends Token {

    static class Op1Token extends OpToken {
        final short operand;

        Op1Token(int opcode, short operand) {
            super(opcode);
            this.operand = operand;
        }

        @Override
        void appendTo(ByteBuffer buf) {
            buf.put((byte) opcode).putShort(operand);
        }

        int size() {
            return Byte.BYTES + Short.BYTES;
        }
    }

    static class Op2Token extends OpToken {
        final short operand1;
        final short operand2;

        Op2Token(int opcode, short operand1, short operand2) {
            super(opcode);
            this.operand1 = operand1;
            this.operand2 = operand2;
        }

        @Override
        void appendTo(ByteBuffer buf) {
            buf.put((byte) opcode).putShort(operand1).putShort(operand2);
        }

        int size() {
            return Byte.BYTES + Short.BYTES * 2;
        }
    }

    static OpToken of(int opcode) {
        return new OpToken(opcode);
    }

    static OpToken of(int opcode, short operand) {
        return new Op1Token(opcode, operand);
    }

    static OpToken of(int opcode, short operand1, short operand2) {
        return new Op2Token(opcode, operand1, operand2);
    }

    final int opcode;

    OpToken(int opcode) {
        this.opcode = opcode;
    }

    @Override
    void appendTo(ByteBuffer buf) {
        buf.put((byte) opcode);
    }

    int size() {
        return Byte.BYTES;
    }
}
