package org.eaglescript;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ScriptNumberTest {

    @Test
    public void testToString() {
        assertEquals("1", ScriptNumber.toString(1.0));
        assertEquals("1.1", ScriptNumber.toString(1.1));
    }
}