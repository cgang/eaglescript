package org.eaglescript.vm;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OpCodeTest {
    @Test
    void testNames() {
        String[] names = OpCode.computeNames();
        for (int i = 0; i < names.length; i++) {
            if (names[i] != null) {
                System.out.println(Integer.toHexString(i) + ": " + names[i]);
            }
        }

        assertEquals("RETURN", OpCode.nameOf(0xEF));
    }
}