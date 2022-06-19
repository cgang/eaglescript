package org.eaglescript;

import java.util.Objects;

public class ScriptNumber extends ScriptObject {
    public static boolean isNumber(Object object) {
        return object instanceof Double || object instanceof ScriptNumber;
    }

    public static double asNumber(Object object) {
        if (object instanceof Double) {
            return (Double) object;
        } else if (object instanceof ScriptNumber) {
            return ((ScriptNumber) object).getValue();
        } else {
            throw new IllegalArgumentException("Invalid number type: " + object);
        }
    }

    public static String toString(double value) {
        long ints = (long) value;
        return (ints == value) ? Long.toString(ints) : Double.toString(value);
    }

    private final double value;

    public ScriptNumber(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    @Override
    public String toString() {
        return ScriptNumber.toString(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScriptNumber that = (ScriptNumber) o;
        return Double.compare(that.value, value) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
