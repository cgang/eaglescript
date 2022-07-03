package org.eaglescript.compiler;

/**
 * A {@link CompilationException} denote exception during compilation.
 */
public class CompilationException extends RuntimeException {
    private static final long serialVersionUID = 990705424926639741L;

    public CompilationException() {
    }

    public CompilationException(String message) {
        super(message);
    }

    public CompilationException(String message, Throwable cause) {
        super(message, cause);
    }

    public CompilationException(Throwable cause) {
        super(cause);
    }

    public CompilationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
