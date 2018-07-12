package com.fakie.utils.paprika;

import com.fakie.utils.FakieUtilsException;

public class PaprikaException extends FakieUtilsException {
    public PaprikaException() {
    }

    public PaprikaException(String message) {
        super(message);
    }

    public PaprikaException(String message, Throwable cause) {
        super(message, cause);
    }

    public PaprikaException(Throwable cause) {
        super(cause);
    }

    public PaprikaException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
