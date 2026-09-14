package com.ilyanin.calendar.persistence;

import com.ilyanin.calendar.model.Calendar;
import com.ilyanin.calendar.model.CalendarKey;
import com.ilyanin.calendar.model.Day;
import com.ilyanin.calendar.model.Month;
import com.ilyanin.calendar.model.MonthName;
import com.ilyanin.calendar.model.WeekDay;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CalendarEntityMapperTest {

    private final CalendarEntityMapper mapper = new CalendarEntityMapper();

    @Test
    void toDomain_shouldMapEntityToDomainPreservingKey() {
        CalendarEntity entity = new CalendarEntity(WeekDay.THURSDAY, false);

        Calendar result = mapper.toDomain(entity);

        assertThat(result.key()).isEqualTo(new CalendarKey(WeekDay.THURSDAY, false));
    }

    @Test
    void toDomain_shouldMapEntityMonthsAndDaysToDomain() {
        CalendarEntity calendarEntity = new CalendarEntity(WeekDay.MONDAY, false);
        MonthEntity monthEntity = new MonthEntity(1, MonthName.JANUARY);
        calendarEntity.addMonth(monthEntity);
        monthEntity.addDay(new DayEntity(1, WeekDay.MONDAY));
        monthEntity.addDay(new DayEntity(2, WeekDay.TUESDAY));

        Calendar result = mapper.toDomain(calendarEntity);

        assertThat(result.months()).hasSize(1);
        Month january = result.months().get(0);
        assertThat(january.name()).isEqualTo(MonthName.JANUARY);
        assertThat(january.days()).hasSize(2);
        assertThat(january.days().get(0)).isEqualTo(new Day(1, WeekDay.MONDAY));
        assertThat(january.days().get(1)).isEqualTo(new Day(2, WeekDay.TUESDAY));
    }

    @Test
    void toEntity_shouldMapDomainToEntityPreservingKey() {
        CalendarKey key = new CalendarKey(WeekDay.FRIDAY, true);
        Calendar calendar = new Calendar(key, List.of());

        CalendarEntity result = mapper.toEntity(calendar);

        assertThat(result.getWeekDayOfFirstOfYear()).isEqualTo(WeekDay.FRIDAY);
        assertThat(result.isLeap()).isTrue();
    }

    @Test
    void toEntity_shouldMapDomainMonthsAndDaysToEntityWithBackReferences() {
        CalendarKey key = new CalendarKey(WeekDay.MONDAY, false);
        Month january = new Month(MonthName.JANUARY, List.of(new Day(1, WeekDay.MONDAY)));
        Calendar calendar = new Calendar(key, List.of(january));

        CalendarEntity result = mapper.toEntity(calendar);

        assertThat(result.getMonths()).hasSize(1);
        MonthEntity monthEntity = result.getMonths().iterator().next();
        assertThat(monthEntity.getName()).isEqualTo(MonthName.JANUARY);
        // проверяем, что двусторонняя связь выставлена корректно (addMonth/addDay)
        assertThat(monthEntity.getCalendar()).isSameAs(result);

        DayEntity dayEntity = monthEntity.getDays().iterator().next();
        assertThat(dayEntity.getDayOfMonth()).isEqualTo(1);
        assertThat(dayEntity.getMonth()).isSameAs(monthEntity);
    }

    @Test
    void toEntity_toDomain_shouldRoundTripCalendarThroughEntityAndBack() {
        CalendarKey key = new CalendarKey(WeekDay.SATURDAY, true);
        Month february = new Month(MonthName.FEBRUARY, List.of(
                new Day(1, WeekDay.SATURDAY),
                new Day(2, WeekDay.SUNDAY)
        ));
        Calendar original = new Calendar(key, List.of(february));

        CalendarEntity entity = mapper.toEntity(original);
        Calendar roundTripped = mapper.toDomain(entity);

        assertThat(roundTripped).isEqualTo(original);
    }
}