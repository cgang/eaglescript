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
     * Syntax:
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
     * Syntax:
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
     * Syntax:
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
     * Syntax:
     * <pre>GOTO offset</pre>
     * Operand stack unaffected.
     */
    public static final int GOTO = 0x20;

    /**
     * Goto code position with specified offset when operand is true.
     * Syntax:
     * <pre>IF_TRUE offset</pre>
     * Operand stack:
     * <pre>
     * ..., bool ->
     * ...,
     * </pre>
     */
    public static final int IF_TRUE = 0x21;

    /**
     * Goto code position with specified offset when operand is false.
     * Syntax:
     * <pre>IF_FALSE offset</pre>
     * Operand stack:
     * <pre>
     * ..., bool ->
     * ...,
     * </pre>
     */
    public static final int IF_FALSE = 0x22;

    /**
     * Check if value1 is equal to value2.
     * Syntax:
     * <pre>EQUAL</pre>
     * Operand stack:
     * <pre>
     * ..., value1, value2
     * ..., bool
     * </pre>
     */
    public static final int EQUAL = 0x23;

    /**
     * Check if value1 is not equal to value2.
     * Syntax:
     * <pre>NOT_EQUAL</pre>
     * Operand stack:
     * <pre>
     * ..., value1, value2
     * ..., bool
     * </pre>
     */
    public static final int NOT_EQUAL = 0x24;

    /**
     * Check if value1 is strictly equal to value2.
     * Syntax:
     * <pre>S_EQUAL</pre>
     * Operand stack:
     * <pre>
     * ..., value1, value2
     * ..., bool
     * </pre>
     */
    public static final int S_EQUAL = 0x25;

    /**
     * Check if value1 is strictly not equal to value2.
     * Syntax:
     * <pre>S_NOT_EQUAL</pre>
     * Operand stack:
     * <pre>
     * ..., value1, value2
     * ..., bool
     * </pre>
     */
    public static final int S_NOT_EQUAL = 0x26;

    /**
     * Check if value1 is less than value2.
     * Syntax:
     * <pre>CMP_LT</pre>
     * Operand stack:
     * <pre>
     * ..., value1, value2
     * ..., bool
     * </pre>
     */
    public static final int CMP_LT = 0x27;

    /**
     * Check if value1 is less than or equal to value2.
     * Syntax:
     * <pre>CMP_LE</pre>
     * Operand stack:
     * <pre>
     * ..., value1, value2
     * ..., bool
     * </pre>
     */
    public static final int CMP_LE = 0x28;

    /**
     * Check if value1 is greater than value2.
     * Syntax:
     * <pre>CMP_GT</pre>
     * Operand stack:
     * <pre>
     * ..., value1, value2
     * ..., bool
     * </pre>
     */
    public static final int CMP_GT = 0x29;

    /**
     * Check if value1 is greater than or equal to value2.
     * Syntax:
     * <pre>CMP_GE</pre>
     * Operand stack:
     * <pre>
     * ..., value1, value2
     * ..., bool
     * </pre>
     */
    public static final int CMP_GE = 0x2A;

    /**
     * Un-determined plus operation, concatenation or arithmetic addition.
     * Syntax:
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
     * Syntax:
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
     * Syntax:
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
     * Syntax:
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
     * Syntax:
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
     * Syntax:
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
     * Syntax:
     * <pre>NEG</pre>
     * Operand stack:
     * <pre>
     * ..., value ->
     * ..., result
     * </pre>
     */
    public static final int NEG = 0x35;

    /**
     * Bitwise AND operation.
     * Syntax:
     * <pre>BIT_AND</pre>
     * Operand stack:
     * <pre>
     * ..., value1, value2 ->
     * ..., result
     * </pre>
     */
    public static final int BIT_AND = 0x36;

    /**
     * Bitwise OR operation.
     * Syntax:
     * <pre>BIT_OR</pre>
     * Operand stack:
     * <pre>
     * ..., value1, value2 ->
     * ..., result
     * </pre>
     */
    public static final int BIT_OR = 0x37;

    /**
     * Bitwise XOR operation.
     * Syntax:
     * <pre>BIT_XOR</pre>
     * Operand stack:
     * <pre>
     * ..., value1, value2 ->
     * ..., result
     * </pre>
     */
    public static final int BIT_XOR = 0x38;

    /**
     * Bitwise NOT operation.
     * Syntax:
     * <pre>BIT_NOT</pre>
     * Operand stack:
     * <pre>
     * ..., value ->
     * ..., result
     * </pre>
     */
    public static final int BIT_NOT = 0x39;

    /**
     * Duplicate the top operand stack value.
     * Syntax:
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
     * Syntax:
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
     * Syntax:
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
     * Syntax:
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
     * Syntax:
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
     * Syntax:
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
     * Syntax:
     * <pre>SET</pre>
     * Operand stack:
     * <pre>
     * ..., value, object, key ->
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
     * Syntax:
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
     * Syntax:
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
     * Syntax:
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
