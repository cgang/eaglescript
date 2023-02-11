package org.eaglescript.vm;

import org.junit.jupiter.api.Test;

import static org.eaglescript.vm.OpCode.*;

class CodeSegmentTest {
    @Test
    public void testDump() {
        byte[] code = {(byte) LOAD, (byte) STORE, (byte) RETURN, (byte) GOTO};
        CodeSegment segment = new CodeSegment(code, new Object[0]);
        segment.dump(System.out);
    }
}