package com.ilyanin.calendar.exception;

public class UnsupportedExportFormatException extends RuntimeException{

    public UnsupportedExportFormatException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnsupportedExportFormatException(String message) {
        super(message);
    }

}
