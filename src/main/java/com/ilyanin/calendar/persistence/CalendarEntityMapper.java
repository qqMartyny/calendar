package com.ilyanin.calendar.persistence;

import com.ilyanin.calendar.model.Calendar;
import com.ilyanin.calendar.model.CalendarKey;
import com.ilyanin.calendar.model.Day;
import com.ilyanin.calendar.model.Month;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
class CalendarEntityMapper {

    Calendar toDomain(CalendarEntity entity) {
        CalendarKey key = new CalendarKey(entity.getWeekDayOfFirstOfYear(), entity.isLeap());

        List<Month> months = entity.getMonths().stream()
                .map(this::toDomain)
                .toList();

        return new Calendar(key, months);
    }

    private Month toDomain(MonthEntity entity) {
        List<Day> days = entity.getDays().stream()
                .map(this::toDomain)
                .toList();

        return new Month(entity.getName(), days);
    }

    private Day toDomain(DayEntity entity) {
        return new Day(entity.getDayOfMonth(), entity.getWeekDay());
    }

    CalendarEntity toEntity(Calendar calendar) {
        CalendarKey key = calendar.key();
        CalendarEntity calendarEntity = new CalendarEntity(key.firstDayOfYear(), key.leapYear());

        for (Month month : calendar.months()) {
            MonthEntity monthEntity = new MonthEntity(month.name().ordinal() + 1, month.name());
            calendarEntity.addMonth(monthEntity);

            for (Day day : month.days()) {
                monthEntity.addDay(new DayEntity(day.dayOfMonth(), day.weekDay()));
            }
        }

        return calendarEntity;
    }
}