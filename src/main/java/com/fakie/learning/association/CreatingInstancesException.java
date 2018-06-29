package com.fakie.learning.association;

import com.fakie.utils.FakieException;

public class CreatingInstancesException extends FakieException {
    public CreatingInstancesException() {
    }

    public CreatingInstancesException(String message) {
        super(message);
    }

    public CreatingInstancesException(String message, Throwable cause) {
        super(message, cause);
    }

    public CreatingInstancesException(Throwable cause) {
        super(cause);
    }

    public CreatingInstancesException(String message, Throwable cause, boolean enableSuppression, boolean
            writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
