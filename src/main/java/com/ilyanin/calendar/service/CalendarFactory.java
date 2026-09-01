package com.ilyanin.calendar.service;

import com.ilyanin.calendar.model.Calendar;
import com.ilyanin.calendar.model.CalendarKey;
import com.ilyanin.calendar.model.Day;
import com.ilyanin.calendar.model.Month;
import com.ilyanin.calendar.model.MonthName;
import com.ilyanin.calendar.model.WeekDay;

import java.util.ArrayList;
import java.util.List;

public class CalendarFactory {

    private final WeekDayCalculator weekDayCalculator;

    public CalendarFactory(WeekDayCalculator weekDayCalculator) {
        this.weekDayCalculator = weekDayCalculator;
    }

    public Calendar create(CalendarKey key) {
        List<Month> months = new ArrayList<>();

        WeekDay currentWeekDay = key.firstDayOfYear();
        for (MonthName monthName : MonthName.values()) {
            int monthNumber = monthName.ordinal() + 1;
            int daysInMonth = monthName.lengthInDays(key.leapYear());

            List<Day> days = new ArrayList<>(daysInMonth);
            for (int dayOfMonth = 1; dayOfMonth <= daysInMonth; dayOfMonth++) {
                days.add(new Day(dayOfMonth, currentWeekDay));
                currentWeekDay = currentWeekDay.next();
            }

            months.add(new Month(monthName, days));
        }

        return new Calendar(key, months);
    }

    public CalendarKey resolveKey(int year) {
        WeekDay firstDay = weekDayCalculator.dayOfWeek(year, 1, 1);
        boolean leap = isLeapYear(year);
        return new CalendarKey(firstDay, leap);
    }

    private boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
    }
}