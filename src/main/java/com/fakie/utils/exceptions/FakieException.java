package com.fakie.utils.exceptions;

public class FakieException extends Exception {
    public FakieException() {
    }

    public FakieException(String message) {
        super(message);
    }

    public FakieException(String message, Throwable cause) {
        super(message, cause);
    }

    public FakieException(Throwable cause) {
        super(cause);
    }

    public FakieException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
