package com.ilyanin.calendar.service;

import com.ilyanin.calendar.model.WeekDay;

public class WeekDayCalculator {

    public WeekDay dayOfWeek(int year, int month, int day) {
        int y = year;
        int m = month;

        if (m < 3) {
            m += 12;
            y -= 1;
        }

        int k = y % 100;
        int j = y / 100;

        int h = (day + (13 * (m + 1)) / 5 + k + k / 4 + j / 4 + 5 * j) % 7;

        return mapZellerResultToWeekDay(h);
    }

    private WeekDay mapZellerResultToWeekDay(int zellerResult) {
        return switch (zellerResult) {
            case 0 -> WeekDay.SATURDAY;
            case 1 -> WeekDay.SUNDAY;
            case 2 -> WeekDay.MONDAY;
            case 3 -> WeekDay.TUESDAY;
            case 4 -> WeekDay.WEDNESDAY;
            case 5 -> WeekDay.THURSDAY;
            case 6 -> WeekDay.FRIDAY;
            default -> throw new IllegalStateException(
                    "Некорректный результат формулы Зеллера: " + zellerResult);
        };
    }
}