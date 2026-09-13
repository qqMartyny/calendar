package com.ilyanin.calendar.persistence;

import com.ilyanin.calendar.model.WeekDay;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "days")
public class DayEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "month_id", nullable = false)
    private MonthEntity month;

    @Column(name = "day_of_month", nullable = false)
    private int dayOfMonth;

    @Enumerated(EnumType.STRING)
    @Column(name = "week_day", nullable = false, length = 20)
    private WeekDay weekDay;

    protected DayEntity() {}

    public DayEntity(int dayOfMonth, WeekDay weekDay) {
        this.dayOfMonth = dayOfMonth;
        this.weekDay = weekDay;
    }

    void setMonth(MonthEntity month) {
        this.month = month;
    }

    public Long getId() {
        return id;
    }

    public MonthEntity getMonth() {
        return month;
    }

    public int getDayOfMonth() {
        return dayOfMonth;
    }

    public WeekDay getWeekDay() {
        return weekDay;
    }
}