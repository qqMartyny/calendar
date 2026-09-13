package com.ilyanin.calendar.service;

import com.ilyanin.calendar.model.Calendar;
import com.ilyanin.calendar.model.CalendarKey;
import com.ilyanin.calendar.model.Day;
import com.ilyanin.calendar.model.Month;
import com.ilyanin.calendar.model.MonthName;
import com.ilyanin.calendar.model.WeekDay;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class CalendarFactory {

    private final CalendarSystem calendarSystem;

    public CalendarFactory(CalendarSystem calendarSystem) {
        this.calendarSystem = calendarSystem;
    }

    public Calendar create(CalendarKey key) {
        List<Month> months = new ArrayList<>();

        WeekDay currentWeekDay = key.firstDayOfYear();
        for (MonthName monthName : MonthName.values()) {
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
        WeekDay firstDay = calendarSystem.dayOfWeek(LocalDate.of(year, 1, 1));
        boolean leap = calendarSystem.isLeapYear(year);
        return new CalendarKey(firstDay, leap);
    }
}