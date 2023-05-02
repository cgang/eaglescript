package org.eaglescript.vm;

import org.eaglescript.ScriptFunction;

import java.util.Arrays;
import java.util.LinkedList;

/**
 * A {@link EagleThread} represents a script thread for execution.
 */
public class EagleThread {
    private static final long serialVersionUID = 5778214472663465154L;

    private final long id;

    private transient CompiledScript script;

    private GlobalObject globalObject;

    private LinkedList<Frame> callStack;

    /**
     * Construct from a long thread ID.
     *
     * @param id a thread ID.
     */
    EagleThread(long id, CompiledScript script, GlobalObject object) {
        this.id = id;
        this.callStack = new LinkedList<>();
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
        frame.pushArgs(args);
        return frame;
    }

    public ScriptFrame start(CompiledScript script) {
        ScriptFrame frame = new ScriptFrame(script, new GlobalContext(globalObject), script.code());
        callStack.add(frame);
        // TODO add support for arguments?
        return frame;
    }

    public ScriptFrame popStack() {
        this.callStack.removeLast();
        return (ScriptFrame) this.callStack.peekLast();
    }
}
