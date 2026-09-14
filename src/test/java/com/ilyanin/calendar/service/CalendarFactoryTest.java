package com.ilyanin.calendar.service;

import com.ilyanin.calendar.model.Calendar;
import com.ilyanin.calendar.model.CalendarKey;
import com.ilyanin.calendar.model.Day;
import com.ilyanin.calendar.model.Month;
import com.ilyanin.calendar.model.MonthName;
import com.ilyanin.calendar.model.WeekDay;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CalendarFactoryTest {

    @Mock
    private CalendarSystem calendarSystem;

    private CalendarFactory factory;

    @BeforeEach
    void setUp() {
        factory = new CalendarFactory(calendarSystem);
    }

    @Test
    void create_shouldCreateCalendarWithTwelveMonths() {
        CalendarKey key = new CalendarKey(WeekDay.MONDAY, false);

        Calendar calendar = factory.create(key);

        assertThat(calendar.months()).hasSize(12);
    }

    @Test
    void create_shouldSetFirstDayOfJanuaryAccordingToKey() {
        CalendarKey key = new CalendarKey(WeekDay.THURSDAY, false);

        Calendar calendar = factory.create(key);

        Month january = calendar.months().get(0);
        Day firstDay = january.days().get(0);

        assertThat(firstDay.weekDay()).isEqualTo(WeekDay.THURSDAY);
    }

    @Test
    void create_shouldHave29DaysInFebruaryWhenLeapYear() {
        CalendarKey key = new CalendarKey(WeekDay.MONDAY, true);

        Calendar calendar = factory.create(key);

        Month february = calendar.months().get(1);
        assertThat(february.days()).hasSize(29);
    }

    @Test
    void create_shouldHave28DaysInFebruaryWhenNotLeapYear() {
        CalendarKey key = new CalendarKey(WeekDay.MONDAY, false);

        Calendar calendar = factory.create(key);

        Month february = calendar.months().get(1);
        assertThat(february.days()).hasSize(28);
    }

    @Test
    void create_shouldHaveConsecutiveDayNumbersInEveryMonth() {
        CalendarKey key = new CalendarKey(WeekDay.SUNDAY, false);

        Calendar calendar = factory.create(key);

        for (MonthName monthName : MonthName.values()) {
            Month month = calendar.months().get(monthName.ordinal());
            for (int i = 0; i < month.days().size(); i++) {
                assertThat(month.days().get(i).dayOfMonth()).isEqualTo(i + 1);
            }
        }
    }

    @Test
    void resolveKey_shouldResolveKeyByDelegatingToCalendarSystem() {
        when(calendarSystem.dayOfWeek(any(LocalDate.class))).thenReturn(WeekDay.MONDAY);
        when(calendarSystem.isLeapYear(2024)).thenReturn(true);

        CalendarKey key = factory.resolveKey(2024);

        assertThat(key.firstDayOfYear()).isEqualTo(WeekDay.MONDAY);
        assertThat(key.leapYear()).isTrue();
    }

    @Test
    void resolvKey_shouldQueryCalendarSystemWithFirstOfJanuary() {
        when(calendarSystem.dayOfWeek(any(LocalDate.class))).thenReturn(WeekDay.FRIDAY);
        when(calendarSystem.isLeapYear(2025)).thenReturn(false);

        factory.resolveKey(2025);

        org.mockito.Mockito.verify(calendarSystem).dayOfWeek(LocalDate.of(2025, 1, 1));
    }
}