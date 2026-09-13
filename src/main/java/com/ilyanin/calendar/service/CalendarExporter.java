package com.ilyanin.calendar.service;

import com.ilyanin.calendar.model.Calendar;

public interface CalendarExporter {

    boolean supports(String format);

    String contentType();
    
    byte[] export(int year, Calendar calendar);
}