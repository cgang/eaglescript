package org.eaglescript.util;

import com.fasterxml.jackson.databind.ObjectMapper;

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
