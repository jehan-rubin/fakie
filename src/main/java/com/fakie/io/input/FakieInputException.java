package com.fakie.io.input;

import com.fakie.io.FakieIOException;

public class FakieInputException extends FakieIOException {
    public FakieInputException() {
    }

    public FakieInputException(String message) {
        super(message);
    }

    public FakieInputException(String message, Throwable cause) {
        super(message, cause);
    }

    public FakieInputException(Throwable cause) {
        super(cause);
    }

    public FakieInputException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
