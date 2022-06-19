package org.eaglescript.util;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * The {@link JSONSupport} provides basic JSON related support for script.
 * It's intended to be exposed as 'JSON' to script.
 */
public class JSONSupport {
    private final ObjectMapper mapper;

    public JSONSupport() {
        mapper = new ObjectMapper();
    }

    public String stringify(Object object) {
        return null;
    }

    public Object parse(String json) {
        return null;
    }
}
