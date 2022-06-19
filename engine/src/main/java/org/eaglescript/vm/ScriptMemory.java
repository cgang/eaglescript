package org.eaglescript.vm;

public abstract class ScriptMemory {
    /**
     * Create a new thread instance.
     * @return a thread instance.
     */
    public abstract ScriptThread create();

    /**
     * Save a thread to this memory.
     * @param thread a thread instance.
     */
    public abstract void save(ScriptThread thread);

    /**
     * Load a thread from this memory.
     * @param threadId the thread ID.
     * @return a thread instance.
     */
    public abstract ScriptThread load(long threadId);
}
