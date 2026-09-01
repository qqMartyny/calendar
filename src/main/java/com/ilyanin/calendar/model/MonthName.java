package com.ilyanin.calendar.model;

public enum MonthName {
    JANUARY("Январь", 31),
    FEBRUARY("Февраль", 28),
    MARCH("Март", 31),
    APRIL("Апрель", 30),
    MAY("Май", 31),
    JUNE("Июнь", 30),
    JULY("Июль", 31),
    AUGUST("Август", 31),
    SEPTEMBER("Сентябрь", 30),
    OCTOBER("Октябрь", 31),
    NOVEMBER("Ноябрь", 30),
    DECEMBER("Декабрь", 31);

    private final String displayName;
    private final int baseDays;

    MonthName(String displayName, int baseDays) {
        this.displayName = displayName;
        this.baseDays = baseDays;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int lengthInDays(boolean leapYear) {
        if (this == FEBRUARY && leapYear) {
            return baseDays + 1;
        }
        return baseDays;
    }
}