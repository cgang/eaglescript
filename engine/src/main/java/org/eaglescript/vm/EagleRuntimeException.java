package org.eaglescript.vm;

public class EagleRuntimeException extends RuntimeException {
    private static final long serialVersionUID = -5648484856559440436L;

    public EagleRuntimeException() {
    }

    public EagleRuntimeException(String message) {
        super(message);
    }

    public EagleRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public EagleRuntimeException(Throwable cause) {
        super(cause);
    }

    public EagleRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
