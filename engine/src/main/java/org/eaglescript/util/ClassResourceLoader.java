package org.eaglescript.util;

import org.eaglescript.vm.ResourceLoader;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class ClassResourceLoader implements ResourceLoader {
    private Class<?> refClass;

    public ClassResourceLoader(Class<?> refClass) {
        this.refClass = refClass;
    }

    @Override
    public Reader open(String name) throws IOException {
        InputStream input = refClass.getResourceAsStream(name);
        if (input == null) {
            throw new FileNotFoundException(name);
        }

        return new InputStreamReader(input, StandardCharsets.UTF_8);
    }
}
