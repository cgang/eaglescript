package org.eaglescript.compiler;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

class CompilingResult {
    private List<Token> tokens = new ArrayList<>();
    private Map<PlaceHolder, Integer> placeHolders;

    public CompilingResult append(CompilingResult next) {
        this.tokens.addAll(next.tokens);
        return this;
    }

    public CompilingResult add(int opcode) {
        return this.add(OpToken.of(opcode));
    }

    public CompilingResult add(int opcode, short operand) {
        return this.add(OpToken.of(opcode, operand));
    }

    public CompilingResult add(Token token) {
        this.tokens.add(token);
        return this;
    }

    public CompilingResult addRef(int opcode, PlaceHolder holder) {
        return this.add(new ReferenceToken(opcode, holder));
    }

    private int size() {
        int size = 0;
        for (Token token : this.tokens) {
            size += token.size();
        }
        return size;
    }

    byte[] toCode() {
        resolve();
        ByteBuffer buffer = ByteBuffer.allocate(size());
        for (Token token : this.tokens) {
            token.appendTo(buffer);
        }
        return buffer.array();
    }

    void resolve() {
        int offset = 0;
        for (int i = 0; i < tokens.size(); i++) {
            Token token = tokens.get(i);
            offset += token.size();
            if (token instanceof ReferenceToken) {
                token = ((ReferenceToken) token).resolve(offset, this);
                tokens.set(i, token);
            }
        }
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
