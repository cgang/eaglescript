package org.eaglescript.compiler;


import java.nio.ByteBuffer;

class PlaceHolder extends Token {
    private String message;

    public PlaceHolder(String message) {
        this.message = message;
    }

    @Override
    void appendTo(ByteBuffer buf) {
    }

    @Override
    int size() {
        return 0;
    }

    @Override
    public String toString() {
        return message;
    }
}
