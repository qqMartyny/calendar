package com.ilyanin.calendar.service;

import com.ilyanin.calendar.model.WeekDay;

import java.time.LocalDate;

public interface CalendarSystem {

    boolean isLeapYear(int year);

    WeekDay dayOfWeek(LocalDate date);
}