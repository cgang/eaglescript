package org.eaglescript.vm;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * Instructions inspired by java virtual machine design.
 */
class Instructions {
    /**
     * Do nothing.
     */
    static final int NOP = 0;

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

    /**
     * Un-determined plus operation, concatenation or arithmetic addition.
     * Format:
     * <pre>PLUS</pre>
     * Operand stack:
     * <pre>
     * ..., value1, value2 ->
     * ..., result
     * </pre>
     */
    static final int PLUS = 0x2F;

    /**
     * Arithmetic addition.
     * Format:
     * <pre>ADD</pre>
     * Operand stack:
     * <pre>
     * ..., value1, value2 ->
     * ..., result
     * </pre>
     */
    static final int ADD = 0x30;
    /**
     * Arithmetic subtraction.
     * Format:
     * <pre>SUB</pre>
     * Operand stack:
     * <pre>
     * ..., value1, value2 ->
     * ..., result
     * </pre>
     */
    static final int SUB = 0x31;
    /**
     * Arithmetic multiplication.
     * Format:
     * <pre>MUL</pre>
     * Operand stack:
     * <pre>
     * ..., value1, value2 ->
     * ..., result
     * </pre>
     */
    static final int MUL = 0x32;
    /**
     * Arithmetic division.
     * Format:
     * <pre>DIV</pre>
     * Operand stack:
     * <pre>
     * ..., value1, value2 ->
     * ..., result
     * </pre>
     */
    static final int DIV = 0x33;
    /**
     * Negate a number.
     * Format:
     * <pre>NEG</pre>
     * Operand stack:
     * <pre>
     * ..., value ->
     * ..., result
     * </pre>
     */
    static final int NEG = 0x34;
    /**
     * Duplicate the top operand stack value.
     * Format:
     * <pre>DUP</pre>
     * Operand stack:
     * <pre>
     * ..., value ->
     * ..., value, value
     * </pre>
     */
    static final int DUP = 0x35;
    /**
     * Duplicate the top operand stack value and insert two values down.
     * Format:
     * <pre>DUP_X1</pre>
     * Operand stack:
     * <pre>
     * ..., value2, value1 ->
     * ..., value1, value2, value1
     * </pre>
     */
    static final int DUP_X1 = 0x36;
    /**
     * Duplicate the top operand stack value and insert three values down.
     * Format:
     * <pre>DUP_X2</pre>
     * Operand stack:
     * <pre>
     * ..., value3, value2, value1 ->
     * ..., value1, value3, value2, value1
     * </pre>
     */
    static final int DUP_X2 = 0x37;
    /**
     * Pop the top operand stack value.
     * Format:
     * <pre>POP</pre>
     * Operand stack:
     * <pre>
     * ..., value ->
     * ...,
     * </pre>
     */
    static final int POP = 0x38;
    /**
     * Swap the top two operand stack values.
     * Format:
     * <pre>SWAP</pre>
     * Operand stack:
     * <pre>
     * ..., value2, value1 ->
     * ..., value1, value2
     * </pre>
     */
    static final int SWAP = 0x39;

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
