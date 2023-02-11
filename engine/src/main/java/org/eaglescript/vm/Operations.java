package org.eaglescript.vm;

import org.eaglescript.ScriptObject;

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
