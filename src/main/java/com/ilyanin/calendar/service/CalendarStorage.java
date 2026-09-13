package com.ilyanin.calendar.service;

import com.ilyanin.calendar.model.Calendar;
import com.ilyanin.calendar.model.CalendarKey;

import java.util.Optional;

public interface CalendarStorage {

    Optional<Calendar> findByKey(CalendarKey key);

    void save(Calendar calendar);
}