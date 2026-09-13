package com.ilyanin.calendar.exception;

public class UnsupportedYearException extends RuntimeException {

    public UnsupportedYearException(int year) {
        super("Год " + year + " не поддерживается текущей календарной системой");
    }
}