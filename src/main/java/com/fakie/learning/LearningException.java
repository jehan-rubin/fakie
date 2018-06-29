package com.fakie.learning;

import com.fakie.utils.exceptions.FakieException;

public class LearningException extends FakieException {
    public LearningException() {
    }

    public LearningException(String message) {
        super(message);
    }

    public LearningException(String message, Throwable cause) {
        super(message, cause);
    }

    public LearningException(Throwable cause) {
        super(cause);
    }

    public LearningException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
