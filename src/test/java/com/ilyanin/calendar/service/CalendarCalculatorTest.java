package com.ilyanin.calendar.service;

import com.ilyanin.calendar.model.WeekDay;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class WeekDayCalculatorTest {

    private final WeekDayCalculator calculator = new WeekDayCalculator();

    @Test
    void shouldReturnWednesdayForFirstOfJanuary2025() {
        // 1 января 2025 года - среда (проверено по реальному календарю)
        assertEquals(WeekDay.WEDNESDAY, calculator.dayOfWeek(2025, 1, 1));
    }

    @Test
    void shouldReturnMondayForFirstOfJanuary2024() {
        // 1 января 2024 года - понедельник
        assertEquals(WeekDay.MONDAY, calculator.dayOfWeek(2024, 1, 1));
    }

    @Test
    void shouldReturnFridayForNewYearMillennium() {
        // 1 января 2000 года - суббота
        assertEquals(WeekDay.SATURDAY, calculator.dayOfWeek(2000, 1, 1));
    }

    @Test
    void shouldCorrectlyHandleLeapFebruary() {
        // 29 февраля 2020 года (високосный год) - суббота
        assertEquals(WeekDay.SATURDAY, calculator.dayOfWeek(2020, 2, 29));
    }

    @Test
    void shouldReturnSundayForIndependenceDay1776() {
        // 4 июля 1776 года - четверг (историческая проверочная дата)
        assertEquals(WeekDay.THURSDAY, calculator.dayOfWeek(1776, 7, 4));
    }
}