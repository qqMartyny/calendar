package com.ilyanin.calendar.persistence;

import com.ilyanin.calendar.model.WeekDay;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "calendars")
public class CalendarEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "week_day_of_first_of_year", nullable = false, length = 20)
    private WeekDay weekDayOfFirstOfYear;

    @Column(name = "is_leap", nullable = false)
    private boolean leap;

    @OneToMany(mappedBy = "calendar", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderBy("monthNumber ASC")
    private List<MonthEntity> months = new ArrayList<>();

    protected CalendarEntity() {}

    public CalendarEntity(WeekDay weekDayOfFirstOfYear, boolean leap) {
        this.weekDayOfFirstOfYear = weekDayOfFirstOfYear;
        this.leap = leap;
    }

    public void addMonth(MonthEntity month) {
        months.add(month);
        month.setCalendar(this);
    }

    public Long getId() {
        return id;
    }

    public WeekDay getWeekDayOfFirstOfYear() {
        return weekDayOfFirstOfYear;
    }

    public boolean isLeap() {
        return leap;
    }

    public List<MonthEntity> getMonths() {
        return months;
    }
}