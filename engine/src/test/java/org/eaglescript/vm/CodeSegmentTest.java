package org.eaglescript.vm;

import org.junit.jupiter.api.Test;

import static org.eaglescript.vm.Instructions.*;
import static org.junit.jupiter.api.Assertions.*;

class CodeSegmentTest {
    @Test
    public void testDump() {
        byte[] code = {(byte) LOAD, (byte) STORE, (byte) RETURN, (byte) GOTO};
        CodeSegment segment = new CodeSegment(code);
        segment.dump(System.out);
    }
}