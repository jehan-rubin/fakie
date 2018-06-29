package com.fakie.io;

import com.fakie.utils.exceptions.FakieException;

public class FakieIOException extends FakieException {
    public FakieIOException() {
    }

    public FakieIOException(String message) {
        super(message);
    }

    public FakieIOException(String message, Throwable cause) {
        super(message, cause);
    }

    public FakieIOException(Throwable cause) {
        super(cause);
    }

    public FakieIOException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
