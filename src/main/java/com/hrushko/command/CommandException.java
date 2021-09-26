package com.hrushko.command;

public class CommandException extends Exception {
    /**
     * Instantiates a new Command exception
     */
    public CommandException() {
    }

    public CommandException(String message) {
        super(message);
    }

    public CommandException(String message, Throwable cause) {
        super(message, cause);
    }

    public CommandException(Throwable cause) {
        super(cause);
    }
}
