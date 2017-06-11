package ru.ifmo.server;

/**
 * Created by l1s on 08.06.17.
 */
public class SessionException extends RuntimeException {
    /**
     * Constructs session exception with message.
     *
     * @param message Exception message.
     */
    public SessionException(String message) {
        super(message);
    }

    /**
     * Constructs session exception with message and cause.
     *
     * @param message Exception message.
     * @param cause   Cause exception.
     */
    public SessionException(String message, Throwable cause) {
        super(message, cause);
    }
}