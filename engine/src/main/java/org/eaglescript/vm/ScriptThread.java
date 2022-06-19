package org.eaglescript.vm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A {@link ScriptThread} represents a script thread for execution.
 */
public class ScriptThread implements Serializable {
    private static final long serialVersionUID = 5778214472663465154L;

    private final long id;

    private List<Frame> callStack;

    /**
     * Construct from a long thread ID.
     * @param id a thread ID.
     */
    ScriptThread(long id) {
        this.id = id;
        this.callStack = new ArrayList<>();
    }

    /**
     * Get the ID of this thread.
     * @return a long thread ID.
     */
    public long id() {
        return id;
    }
}
