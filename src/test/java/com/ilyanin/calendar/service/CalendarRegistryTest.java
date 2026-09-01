package com.ilyanin.calendar.service;

import com.ilyanin.calendar.model.Calendar;
import com.ilyanin.calendar.model.CalendarKey;
import com.ilyanin.calendar.model.WeekDay;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class CalendarRegistryTest {

    private CalendarRegistry registry;

    @BeforeEach
    void setUp() {
        CalendarFactory factory = new CalendarFactory(new WeekDayCalculator());
        registry = new CalendarRegistry(factory);
    }

    @Test
    void shouldContainExactlyFourteenUniqueCalendars() {
        assertEquals(14, registry.uniqueCalendarsCount());
    }

    @Test
    void shouldReturnCalendarMatchingRequestedYear() {
        // 2024 год - високосный, начинается с понедельника
        Calendar calendar = registry.getCalendarFor(2024);

        assertEquals(new CalendarKey(WeekDay.MONDAY, true), calendar.key());
    }

    @Test
    void shouldReturnSameCalendarInstanceForYearsWithSameKey() {
        // 2024 и 1996 - оба високосные и оба начинаются с понедельника
        Calendar calendar2024 = registry.getCalendarFor(2024);
        Calendar calendar1996 = registry.getCalendarFor(1996);

        assertSame(calendar2024, calendar1996);
    }

    @Test
    void shouldReturnDifferentCalendarsForDifferentKeys() {
        Calendar calendar2024 = registry.getCalendarFor(2024); // leap, Monday
        Calendar calendar2025 = registry.getCalendarFor(2025); // not leap, Wednesday

        assertEquals(false, calendar2024.key().equals(calendar2025.key()));
    }
}