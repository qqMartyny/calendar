package com.ilyanin.calendar.persistence;

import com.ilyanin.calendar.model.Calendar;
import com.ilyanin.calendar.model.CalendarKey;
import com.ilyanin.calendar.service.CalendarStorage;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public class JpaCalendarStorage implements CalendarStorage {

    private final CalendarRepository repository;
    private final CalendarEntityMapper mapper;

    public JpaCalendarStorage(CalendarRepository repository, CalendarEntityMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Calendar> findByKey(CalendarKey key) {
        return repository
                .findByKeyWithMonthsAndDays(key.firstDayOfYear(), key.leapYear())
                .map(mapper::toDomain);
    }

    @Override
    @Transactional
    public void save(Calendar calendar) {
        CalendarEntity entity = mapper.toEntity(calendar);
        repository.save(entity);
    }
}