package org.openended.photosteward;

public class PhotoStewardException extends RuntimeException {
    public PhotoStewardException() {
    }

    public PhotoStewardException(String message) {
        super(message);
    }

    public PhotoStewardException(String message, Throwable cause) {
        super(message, cause);
    }

    public PhotoStewardException(Throwable cause) {
        super(cause);
    }

    public PhotoStewardException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
