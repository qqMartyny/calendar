package com.ilyanin.calendar.model;

import java.util.List;

public record Month(MonthName name, List<Day> days) {

    public Month {
        days = List.copyOf(days);
    }
}