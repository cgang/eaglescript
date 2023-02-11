package org.eaglescript.vm;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * Instructions inspired by java virtual machine design.
 */
public class OpCode {
    /**
     * Do nothing.
     */
    public static final int NOP = 0;

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
    public static final int LOAD = 0x10;
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
    public static final int STORE = 0x11;

    /**
     * Load a constant value.
     * Format:
     * <pre>LOAD_CONST index</pre>
     * Operand stack:
     * <pre>
     * ..., ->
     * ..., const
     * </pre>
     */
    public static final int LOAD_CONST = 0x12;
    /**
     * Goto code position with specified offset.
     * Format:
     * <pre>GOTO offset</pre>
     * Operand stack unaffected.
     */
    public static final int GOTO = 0x20;

    public static final int IF_TRUE = 0x21;

    public static final int IF_FALSE = 0x22;

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
    public static final int PLUS = 0x2F;

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
    public static final int ADD = 0x30;
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
    public static final int SUB = 0x31;
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
    public static final int MUL = 0x32;
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
    public static final int DIV = 0x33;
    /**
     * Arithmetic modulus.
     * Format:
     * <pre>MOD</pre>
     * Operand stack:
     * <pre>
     * ..., value1, value2 ->
     * ..., result
     * </pre>
     */
    public static final int MOD = 0x34;
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
    public static final int NEG = 0x35;
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
    public static final int DUP = 0x40;
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
    public static final int DUP_X1 = 0x41;
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
    public static final int DUP_X2 = 0x42;
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
    public static final int POP = 0x43;
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
    public static final int SWAP = 0x44;

    /**
     * Get property of an object.
     * Format:
     * <pre>GET</pre>
     * Operand stack:
     * <pre>
     * ..., object, key ->
     * ..., value
     * </pre>
     */
    public static final int GET = 0x50;

    /**
     * Set property of an object.
     * Format:
     * <pre>SET</pre>
     * Operand stack:
     * <pre>
     * ..., object, key, value ->
     * ...,
     * </pre>
     */
    public static final int SET = 0x51;

    /**
     * A placeholder to separate flow control instructions from normal instructions.
     */
    static final int FLOW_CONTROL = 0xA0;

    /**
     * Invoke an invocable object with specified number of arguments.
     * Format:
     * <pre>INVOKE count</pre>
     * Operand stack:
     * <pre>
     * ..., target, arg_0, ..., arg_N
     * ...,
     * </pre>
     */
    public static final int INVOKE = 0xA1;

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
    public static final int THROW = 0xA2;
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
    public static final int RETURN = 0xEF;

    static String[] computeNames() {
        String[] names = new String[0x100];
        for (Field field : OpCode.class.getDeclaredFields()) {
            int mod = field.getModifiers();
            if (Modifier.isStatic(mod) && Modifier.isPublic(mod) && field.getType().equals(int.class)) {
                try {
                    int value = field.getInt(null);
                    if (value >= 0 && value < names.length) {
                        names[value] = field.getName();
                    } else {
                        throw new IllegalStateException("Invalid value: " + field.getName() + " = " + value);
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
