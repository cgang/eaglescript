package org.eaglescript.vm;

class Operations {
    static Object plus(Object left, Object right) {
        if (left instanceof Double && right instanceof Double) {
            return (Double) left + (Double) right;
        } else {
            // TODO be comply with ES6 spec.
            return String.valueOf(left) + right;
        }
    }

    static Object add(Object left, Object right) {
        return (Double) left + (Double) right;
    }

    static Object sub(Object left, Object right) {
        return (Double) left - (Double) right;
    }

    static Object mul(Object left, Object right) {
        return (Double) left * (Double) right;
    }

    static Object div(Object left, Object right) {
        return (Double) left / (Double) right;
    }

    static Object negate(Object value) {
        return - (Double) value;
    }
}
