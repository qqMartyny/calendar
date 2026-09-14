package com.ilyanin.calendar.service;

import com.ilyanin.calendar.exception.UnsupportedYearException;
import com.ilyanin.calendar.model.Calendar;
import com.ilyanin.calendar.model.CalendarKey;
import com.ilyanin.calendar.model.WeekDay;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CalendarServiceTest {

    @Mock
    private CalendarFactory calendarFactory;

    @Mock
    private CalendarStorage calendarStorage;

    @Mock
    private CalendarSystem calendarSystem;

    private CalendarService service;

    private final CalendarKey key = new CalendarKey(WeekDay.MONDAY, false);
    private final Calendar calendar = new Calendar(key, List.of());

    @BeforeEach
    void setUp() {
        service = new CalendarService(calendarFactory, calendarStorage, calendarSystem);
    }

    @Test
    void getCalendarFor_shouldReturnExistingCalendarWithoutBuildingWhenAlreadyStored() {
        when(calendarSystem.isSupportedYear(2026)).thenReturn(true);
        when(calendarFactory.resolveKey(2026)).thenReturn(key);
        when(calendarStorage.findByKey(key)).thenReturn(Optional.of(calendar));

        Calendar result = service.getCalendarFor(2026);

        assertThat(result).isEqualTo(calendar);
        // не должны заново строить и сохранять то, что уже есть в хранилище
        verify(calendarFactory, never()).create(any());
        verify(calendarStorage, never()).save(any());
    }

    @Test
    void getCalendarFor_shouldBuildAndSaveCalendarWhenNotYetStored() {
        when(calendarSystem.isSupportedYear(2026)).thenReturn(true);
        when(calendarFactory.resolveKey(2026)).thenReturn(key);
        when(calendarStorage.findByKey(key)).thenReturn(Optional.empty());
        when(calendarFactory.create(key)).thenReturn(calendar);

        Calendar result = service.getCalendarFor(2026);

        assertThat(result).isEqualTo(calendar);
        verify(calendarFactory, times(1)).create(key);
        verify(calendarStorage, times(1)).save(calendar);
    }

    @Test
    void getCalendarFor_shouldThrowUnsupportedYearExceptionWithoutTouchingStorageOrFactory() {
        when(calendarSystem.isSupportedYear(44)).thenReturn(false);

        assertThatThrownBy(() -> service.getCalendarFor(44))
                .isInstanceOf(UnsupportedYearException.class);

        // год отклонён ДО обращения к factory/storage - не должно быть попытки
        // построить или прочитать что-либо для неподдерживаемого года
        verifyNoInteractions(calendarFactory);
        verifyNoInteractions(calendarStorage);
    }

    @Test
    void getWeekDay_shouldReturnWeekDayForSupportedDate() {
        LocalDate date = LocalDate.of(2000, 1, 1);
        when(calendarSystem.isSupportedYear(2000)).thenReturn(true);
        when(calendarSystem.dayOfWeek(date)).thenReturn(WeekDay.SATURDAY);

        WeekDay result = service.getWeekDay(date);

        assertThat(result).isEqualTo(WeekDay.SATURDAY);
    }

    @Test
    void getWeekDay_shouldThrowUnsupportedYearExceptionForWeekDayOfUnsupportedDate() {
        LocalDate date = LocalDate.of(44, 3, 15);
        when(calendarSystem.isSupportedYear(44)).thenReturn(false);

        assertThatThrownBy(() -> service.getWeekDay(date))
                .isInstanceOf(UnsupportedYearException.class);
    }
}