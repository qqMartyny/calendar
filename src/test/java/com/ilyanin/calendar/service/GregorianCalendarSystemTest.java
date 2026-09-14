package com.ilyanin.calendar.service;

import com.ilyanin.calendar.model.WeekDay;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class GregorianCalendarSystemTest {

    private final GregorianCalendarSystem system = new GregorianCalendarSystem();

    @ParameterizedTest
    @CsvSource({
            "2024, true",   // делится на 4, не на 100 -> високосный
            "2025, false",  // не делится на 4
            "1900, false",  // делится на 100, но не на 400 -> НЕ високосный
            "2000, true",   // делится на 400 -> високосный
            "1600, true"
    })
    void isLeapYear_shouldDetermineLeapYearCorrectly(int year, boolean expectedLeap) {
        assertThat(system.isLeapYear(year)).isEqualTo(expectedLeap);
    }

    @Test
    void dayOfWeek_shouldReturnCorrectWeekDayForKnownDate() {
        // 1 января 2025 года - среда (проверено по реальному календарю)
        WeekDay result = system.dayOfWeek(LocalDate.of(2025, 1, 1));

        assertThat(result).isEqualTo(WeekDay.WEDNESDAY);
    }

    @Test
    void dayOfWeek_shouldReturnMondayForFirstOfJanuary2024() {
        WeekDay result = system.dayOfWeek(LocalDate.of(2024, 1, 1));

        assertThat(result).isEqualTo(WeekDay.MONDAY);
    }

    @ParameterizedTest
    @CsvSource({
            "1600, true",
            "1599, false",
            "1601, true",
            "2026, true",
            "1000, false"
    })
    void isSupportedYear_shouldSupportOnlyYearsFrom1600(int year, boolean expectedSupported) {
        assertThat(system.isSupportedYear(year)).isEqualTo(expectedSupported);
    }
}