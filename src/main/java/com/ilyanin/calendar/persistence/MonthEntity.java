package com.ilyanin.calendar.persistence;

import com.ilyanin.calendar.model.MonthName;
import jakarta.persistence.CascadeType;
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "months")
public class MonthEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "calendar_id", nullable = false)
    private CalendarEntity calendar;

    @Column(name = "month_number", nullable = false)
    private int monthNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "name", nullable = false, length = 20)
    private MonthName name;

    @OneToMany(mappedBy = "month", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderBy("dayOfMonth ASC")
    private Set<DayEntity> days = new LinkedHashSet<>();

    protected MonthEntity() {}

    public MonthEntity(int monthNumber, MonthName name) {
        this.monthNumber = monthNumber;
        this.name = name;
    }

    public void addDay(DayEntity day) {
        days.add(day);
        day.setMonth(this);
    }

    void setCalendar(CalendarEntity calendar) {
        this.calendar = calendar;
    }

    public Long getId() {
        return id;
    }

    public CalendarEntity getCalendar() {
        return calendar;
    }

    public int getMonthNumber() {
        return monthNumber;
    }

    public MonthName getName() {
        return name;
    }

    public Set<DayEntity> getDays() {
        return days;
    }
}