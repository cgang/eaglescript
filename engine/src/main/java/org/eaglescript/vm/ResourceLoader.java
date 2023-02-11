package org.eaglescript.vm;

import java.io.IOException;
import java.io.Reader;

public interface ResourceLoader {
    /**
     * Open a resource with specified name.
     *
     * @param name a resource name.
     * @return a reader to specified resource.
     * @throws IOException if error occurs while opening the resource.
     */
    Reader open(String name) throws IOException;
}
