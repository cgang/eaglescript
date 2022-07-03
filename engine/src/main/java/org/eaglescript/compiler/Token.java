package org.eaglescript.compiler;

import java.nio.ByteBuffer;

/**
 * A {@link Token} is an intermediate compiling unit.
 */
abstract class Token {
    /**
     * Append this token to specified buffer.
     * @param buf a result byte buffer.
     */
    abstract void appendTo(ByteBuffer buf);

    abstract int size();
}
