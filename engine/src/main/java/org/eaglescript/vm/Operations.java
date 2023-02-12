package org.eaglescript.vm;

import org.eaglescript.ScriptNull;
import org.eaglescript.ScriptObject;

import java.util.Objects;

import static org.eaglescript.ScriptNumber.asNumber;

class Operations {
    static Object plus(Object right, Object left) {
        if (left instanceof Double && right instanceof Double) {
            return (Double) left + (Double) right;
        } else {
            // TODO be comply with ES6 spec.
            return String.valueOf(left) + right;
        }
    }

    static Object add(Object right, Object left) {
        return (Double) left + (Double) right;
    }

    static Object sub(Object right, Object left) {
        return (Double) left - (Double) right;
    }

    static Object mul(Object right, Object left) {
        return (Double) left * (Double) right;
    }

    static Object div(Object right, Object left) {
        return (Double) left / (Double) right;
    }

    static Object negate(Object value) {
        return -(Double) value;
    }

    static boolean equal(Object value2, Object value1) {
        if (Objects.equals(value1, value2)) {
            return true;
        }

        if (value1 == null) {
            return value2 instanceof ScriptNull;
        } else if (value2 == null) {
            return value1 instanceof ScriptNull;
        }

        // TODO add proper equal support
        return false;
    }

    static boolean strictEqual(Object value2, Object value1) {
        // TODO add proper strict equal support
        return value1 == value2;
    }

    static boolean lessThan(Object value2, Object value1) {
        return asNumber(value1) < asNumber(value2);
    }

    static boolean lessThanOrEqual(Object value2, Object value1) {
        return asNumber(value1) <= asNumber(value2);
    }

    static boolean greaterThan(Object value2, Object value1) {
        return asNumber(value1) > asNumber(value2);
    }

    static boolean greaterThanOrEqual(Object value2, Object value1) {
        return asNumber(value1) >= asNumber(value2);
    }

    static Object doGet(Object key, Object object) {
        if (object instanceof ScriptObject) {
            return ((ScriptObject) object).get(key);
        } else {
            throw new RuntimeException("Unsupported object: " + object);
        }
    }

    static void doSet(Object value, Object key, Object object) {
        if (object instanceof ScriptObject) {
            ((ScriptObject) object).set(key, value);
        } else {
            throw new RuntimeException("Unsupported object: " + object);
        }
    }
}
