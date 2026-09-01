package com.ilyanin.calendar.service;

import com.ilyanin.calendar.model.Calendar;
import com.ilyanin.calendar.model.CalendarKey;
import com.ilyanin.calendar.model.WeekDay;

import java.util.HashMap;
import java.util.Map;

public class CalendarRegistry {

    private final CalendarFactory calendarFactory;
    private final Map<CalendarKey, Calendar> calendarsByKey;

    public CalendarRegistry(CalendarFactory calendarFactory) {
        this.calendarFactory = calendarFactory;
        this.calendarsByKey = buildAllUniqueCalendars();
    }

    private Map<CalendarKey, Calendar> buildAllUniqueCalendars() {

        Map<CalendarKey, Calendar> result = new HashMap<>();

        for (WeekDay firstDay : WeekDay.values()) {
            for (boolean leap : new boolean[]{true, false}) {
                CalendarKey key = new CalendarKey(firstDay, leap);
                result.put(key, calendarFactory.create(key));
            }
        }
        return Map.copyOf(result);
    }

    public int uniqueCalendarsCount() {
        return calendarsByKey.size();
    }

    public Calendar getCalendarFor(int year) {
        CalendarKey key = calendarFactory.resolveKey(year);
        Calendar calendar = calendarsByKey.get(key);
        if (calendar == null) {
            throw new IllegalStateException("Календарь для ключа " + key + " не найден в реестре");
        }
        return calendar;
    }
}