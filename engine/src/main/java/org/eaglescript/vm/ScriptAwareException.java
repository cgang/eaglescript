package org.eaglescript.vm;

public class ScriptAwareException extends Exception {
    private static final long serialVersionUID = 2807318124635047963L;

    public ScriptAwareException() {
    }

    public ScriptAwareException(String message) {
        super(message);
    }

    public ScriptAwareException(String message, Throwable cause) {
        super(message, cause);
    }

    public ScriptAwareException(Throwable cause) {
        super(cause);
    }

    public ScriptAwareException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
