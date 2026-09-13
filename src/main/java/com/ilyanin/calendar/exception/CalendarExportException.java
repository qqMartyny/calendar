package com.ilyanin.calendar.exception;

public class CalendarExportException extends RuntimeException{

    public CalendarExportException(String message, Throwable cause) {
        super(message, cause);
    }

    public CalendarExportException(String message) {
        super(message);
    }

}
