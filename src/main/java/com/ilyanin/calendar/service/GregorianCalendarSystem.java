package com.ilyanin.calendar.service;

import com.ilyanin.calendar.model.WeekDay;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Year;

@Component
public class GregorianCalendarSystem implements CalendarSystem {

    private static final int MIN_SUPPORTED_YEAR = 1600;

    @Override
    public boolean isLeapYear(int year) {
        return Year.isLeap(year);
    }

    @Override
    public WeekDay dayOfWeek(LocalDate date) {
        int isoOrdinal = date.getDayOfWeek().ordinal();
        return WeekDay.values()[isoOrdinal];
    }

    @Override
    public boolean isSupportedYear(int year) {
        return year >= MIN_SUPPORTED_YEAR;
    }
}