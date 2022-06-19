package org.eaglescript.vm;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * Instructions inspired by java virtual machine design.
 */
class Instructions {
    /**
     * Load reference from variable.
     * Format:
     * <pre>LOAD index</pre>
     * Operand stack:
     * <pre>
     * ..., ->
     * ..., objectref
     * </pre>
     */
    static final int LOAD = 0x10;
    /**
     * Store reference to variable.
     * Format:
     * <pre>STORE index</pre>
     * Operand stack:
     * <pre>
     * ..., objectref ->
     * ...,
     * </pre>
     */
    static final int STORE = 0x11;

    /**
     * Goto code position with specified offset.
     * Format:
     * <pre>GOTO offset</pre>
     * Operand stack unaffected.
     */
    static final int GOTO = 0x20;

    static final int IF_TRUE = 0x21;

    static final int IF_FALSE = 0x22;

    static final int SIMPLE = 0xA0;

    /**
     * Invoke an invocable object with specified spec index.
     * Format:
     * <pre>INVOKE index</pre>
     * Operand stack:
     * <pre>
     * ..., arg_N, ..., arg_0
     * ...,
     * </pre>
     */
    static final int INVOKE = 0xA1;

    /**
     * Throw an error.
     * Format:
     * <pre>THROW</pre>
     * Operand stack:
     * <pre>
     * ..., objectref ->
     * ...,
     * </pre>
     * The operand stack should be empty after throw.
     */
    static final int THROW = 0xA2;
    /**
     * Return to caller.
     * Format:
     * <pre>RETURN</pre>
     * <pre>
     * ..., objectref ->
     * ...,
     * </pre>
     * The operand stack should be empty after return.
     */
    static final int RETURN = 0xEF;

    static String[] computeNames() {
        String[] names = new String[0x100];
        for (Field field : Instructions.class.getDeclaredFields()) {
            if ("SIMPLE".equals(field.getName())) {
                continue;
            }
            if (field.getType().equals(int.class) && Modifier.isStatic(field.getModifiers())) {
                try {
                    int value = field.getInt(null);
                    if (value > 0 && value < names.length) {
                        names[value] = field.getName();
                    } else {
                        throw new IllegalStateException("Invalid value: " + value);
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return names;
    }

    private static final String[] NAMES = computeNames();

    /**
     * Get name of specified op code.
     * @param opcode an opcode.
     * @return the name of the opcode.
     * @throws IllegalArgumentException if the op code is unknown.
     */
    public static String nameOf(int opcode) {
        if (opcode > 0 && opcode < NAMES.length) {
            String name = NAMES[opcode];
            if (name != null) {
                return name;
            }
        }
        throw new IllegalArgumentException("Unknown code: " + opcode);
    }

}
