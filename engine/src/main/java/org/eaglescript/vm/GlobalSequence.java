package org.eaglescript.vm;

class GlobalSequence {
    private static long id = 1;

    static long nextId() {
        // TODO replace with snowflake ID
        return id++;
    }
}
