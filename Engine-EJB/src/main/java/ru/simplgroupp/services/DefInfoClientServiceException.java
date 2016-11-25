package ru.simplgroupp.services;

/**
 * DEF info client exception
 */
public class DefInfoClientServiceException extends RuntimeException {

    public DefInfoClientServiceException(String message) {
        super(message);
    }

    public DefInfoClientServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
