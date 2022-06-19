package org.eaglescript.vm;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InstructionsTest {
    @Test
    void testNames() {
        String[] names = Instructions.computeNames();
        for (int i = 0; i < names.length; i++) {
            if (names[i] != null) {
                System.out.println(Integer.toHexString(i) + ": " + names[i]);
            }
        }

        assertEquals("RETURN", Instructions.nameOf(0xEF));
    }
}