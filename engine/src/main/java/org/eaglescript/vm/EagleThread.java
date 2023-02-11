package org.eaglescript.vm;

import org.eaglescript.ScriptFunction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A {@link EagleThread} represents a script thread for execution.
 */
public class EagleThread {
    private static final long serialVersionUID = 5778214472663465154L;

    private final long id;

    private transient CompiledScript script;

    private GlobalObject globalObject;

    private List<Frame> callStack;

    /**
     * Construct from a long thread ID.
     *
     * @param id a thread ID.
     */
    EagleThread(long id, CompiledScript script, GlobalObject object) {
        this.id = id;
        this.callStack = new ArrayList<>();
        this.script = script;
        this.globalObject = object;
    }

    /**
     * Get the ID of this thread.
     *
     * @return a long thread ID.
     */
    public long id() {
        return id;
    }

    ScriptFrame newStack(ScriptFunction function, ExecutionContext context, Object[] args) {
        ScriptFrame frame = new ScriptFrame(script, context, function.code());
        callStack.add(frame);

        args = Arrays.copyOf(args, function.argCount());
        frame.push(args);
        return frame;
    }

    public ScriptFrame start(CompiledScript script, Object[] args) {
        ScriptFrame frame = new ScriptFrame(script, new GlobalContext(globalObject), script.code());
        callStack.add(frame);
        // TODO add support for arguments.
        return frame;
    }
}
