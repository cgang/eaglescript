package org.eaglescript.vm;

public class CompiledScript {
    private String[] identifiers;

    /**
     * Resolve an index to identifier.
     * @param index the index of identifier.
     * @return an identifier
     * @throws IllegalStateException if the index is not valid.
     */
    public String resolve(short index) {
        return identifiers[index & 0xFFFF];
    }
}
