package org.eaglescript.compiler;

import org.eaglescript.vm.CodeSegment;

import java.nio.ByteBuffer;
import java.util.*;

class CompilingResult {
    private List<Token> tokens = new ArrayList<>();
    private Map<PlaceHolder, Integer> placeHolders;

    public CompilingResult append(CompilingResult next) {
        this.tokens.addAll(next.tokens);
        return this;
    }

    public CompilingResult add(int opcode) {
        this.tokens.add(OpToken.of(opcode));
        return this;
    }

    public CompilingResult add(int opcode, short operand) {
        this.tokens.add(OpToken.of(opcode, operand));
        return this;
    }

    private int size() {
        int size = 0;
        for (Token token : this.tokens) {
            size += token.size();
        }
        return size;
    }

    CodeSegment toCodeSegment() {
        ByteBuffer buffer = ByteBuffer.allocate(size());
        for (Token token : this.tokens) {
            token.appendTo(buffer);
        }
        return new CodeSegment(buffer.array());
    }

    int getOffset(PlaceHolder holder) {
        if (placeHolders == null) {
            placeHolders = computePlaceHolderMap();
        }
        Integer offset = placeHolders.get(holder);
        if (offset != null) {
            return offset;
        } else {
            throw new IllegalStateException("Place holder not found: " + holder);
        }
    }

    private Map<PlaceHolder, Integer> computePlaceHolderMap() {
        Map<PlaceHolder, Integer> result = new IdentityHashMap<>();
        int offset = 0;
        for (Token token : tokens) {
            if (token instanceof PlaceHolder) {
                result.put((PlaceHolder) token, offset);
            }
            offset += token.size();
        }
        return result;
    }
}
