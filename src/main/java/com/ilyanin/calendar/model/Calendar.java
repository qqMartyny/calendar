package com.ilyanin.calendar.model;

import java.util.List;

public record Calendar(CalendarKey key, List<Month> months) {

    public Calendar {
        months = List.copyOf(months);
    }
}