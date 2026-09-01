package com.ilyanin.calendar.service;

import com.ilyanin.calendar.model.Calendar;
import com.ilyanin.calendar.model.CalendarKey;
import com.ilyanin.calendar.model.Day;
import com.ilyanin.calendar.model.Month;
import com.ilyanin.calendar.model.MonthName;
import com.ilyanin.calendar.model.WeekDay;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CalendarFactoryTest {

    private final CalendarFactory factory = new CalendarFactory(new WeekDayCalculator());

    @Test
    void shouldCreateCalendarWithTwelveMonths() {
        Calendar calendar = factory.create(new CalendarKey(WeekDay.MONDAY, false));

        assertEquals(12, calendar.months().size());
    }

    @Test
    void shouldSetFirstDayOfJanuaryAccordingToKey() {
        Calendar calendar = factory.create(new CalendarKey(WeekDay.THURSDAY, false));

        Month january = calendar.months().get(0);
        Day firstDay = january.days().get(0);

        assertEquals(WeekDay.THURSDAY, firstDay.weekDay());
    }

    @Test
    void shouldHave29DaysInFebruaryWhenLeapYear() {
        Calendar calendar = factory.create(new CalendarKey(WeekDay.MONDAY, true));

        Month february = calendar.months().get(1);

        assertEquals(29, february.days().size());
    }

    @Test
    void shouldHave28DaysInFebruaryWhenNotLeapYear() {
        Calendar calendar = factory.create(new CalendarKey(WeekDay.MONDAY, false));

        Month february = calendar.months().get(1);

        assertEquals(28, february.days().size());
    }

    @Test
    void shouldResolveKeyMatchingKnownYear() {
        // 2024 год - високосный, начинается с понедельника
        CalendarKey key = factory.resolveKey(2024);

        assertEquals(WeekDay.MONDAY, key.firstDayOfYear());
        assertTrue(key.leapYear());
    }

    @Test
    void shouldHaveConsecutiveDayNumbersInEveryMonth() {
        Calendar calendar = factory.create(new CalendarKey(WeekDay.SUNDAY, false));

        for (MonthName monthName : MonthName.values()) {
            Month month = calendar.months().get(monthName.ordinal());
            for (int i = 0; i < month.days().size(); i++) {
                assertEquals(i + 1, month.days().get(i).dayOfMonth());
            }
        }
    }
}