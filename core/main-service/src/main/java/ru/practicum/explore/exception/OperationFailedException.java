package ru.practicum.explore.exception;

public class OperationFailedException extends RuntimeException {
    public OperationFailedException(String message) {
        super(message);
    }
}