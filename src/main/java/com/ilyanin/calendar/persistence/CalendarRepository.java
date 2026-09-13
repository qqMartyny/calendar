package com.ilyanin.calendar.persistence;

import com.ilyanin.calendar.model.WeekDay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CalendarRepository extends JpaRepository<CalendarEntity, Long> {

    @Query("""
            SELECT DISTINCT c FROM CalendarEntity c
            LEFT JOIN FETCH c.months m
            LEFT JOIN FETCH m.days
            WHERE c.weekDayOfFirstOfYear = :weekDay AND c.leap = :leap
            """)
    Optional<CalendarEntity> findByKeyWithMonthsAndDays(
            @Param("weekDay") WeekDay weekDay,
            @Param("leap") boolean leap
    );
}