package com.ilyanin.calendar.service;

import com.ilyanin.calendar.model.Calendar;
import com.ilyanin.calendar.model.CalendarKey;
import com.ilyanin.calendar.model.WeekDay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class CalendarService {

    private static final Logger log = LoggerFactory.getLogger(CalendarService.class);

    private final CalendarFactory calendarFactory;
    private final CalendarStorage calendarStorage;
    private final CalendarSystem calendarSystem;

    public CalendarService(
        CalendarFactory calendarFactory,
        CalendarStorage calendarStorage,
        CalendarSystem calendarSystem
    ) {
        this.calendarFactory = calendarFactory;
        this.calendarStorage = calendarStorage;
        this.calendarSystem = calendarSystem;
    }

    public Calendar getCalendarFor(int year) {
        CalendarKey key = calendarFactory.resolveKey(year);

        Calendar calendar = calendarStorage.findByKey(key)
                .orElseGet(() -> {
                    Calendar built = calendarFactory.create(key);
                    calendarStorage.save(built);
                    log.info("Built and stored new calendar for key {}", key);
                    return built;
                });

        log.info("Calendar requested for year {} (key: {})", year, key);
        return calendar;
    }

    public WeekDay getWeekDay(LocalDate date) {
        WeekDay weekDay = calendarSystem.dayOfWeek(date);
        log.info("Week day requested for date {}: {}", date, weekDay);
        return weekDay;
    }
}