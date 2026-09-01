package com.ilyanin.calendar.model;

public enum WeekDay {
    MONDAY("Пн"),
    TUESDAY("Вт"),
    WEDNESDAY("Ср"),
    THURSDAY("Чт"),
    FRIDAY("Пт"),
    SATURDAY("Сб"),
    SUNDAY("Вс");

    private final String shortName;

    WeekDay(String shortName) {
        this.shortName = shortName;
    }

    public String getShortName() {
        return shortName;
    }

    public WeekDay next() {
        WeekDay[] values = values();
        int nextIndex = (this.ordinal() + 1) % values.length;
        return values[nextIndex];
    }
}