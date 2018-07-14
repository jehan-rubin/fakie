package com.fakie.utils.exceptions;

public class FakieRuntimeException extends RuntimeException {
    public FakieRuntimeException() {
    }

    public FakieRuntimeException(String message) {
        super(message);
    }

    public FakieRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public FakieRuntimeException(Throwable cause) {
        super(cause);
    }

    public FakieRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean
            writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
