package com.ilyanin.calendar.service;

import com.ilyanin.calendar.model.Calendar;
import com.ilyanin.calendar.model.CalendarKey;
import com.ilyanin.calendar.model.Day;
import com.ilyanin.calendar.model.Month;
import com.ilyanin.calendar.model.MonthName;
import com.ilyanin.calendar.model.WeekDay;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.json.JsonMapper;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class JsonCalendarExporterTest {

    private final JsonCalendarExporter exporter = new JsonCalendarExporter(JsonMapper.builder().build());

    @Test
    void supports_shouldSupportOnlyJsonFormatCaseInsensitively() {
        assertThat(exporter.supports("json")).isTrue();
        assertThat(exporter.supports("JSON")).isTrue();
        assertThat(exporter.supports("xml")).isFalse();
        assertThat(exporter.supports("xlsx")).isFalse();
    }

    @Test
    void contentType_shouldReturnJsonContentType() {
        assertThat(exporter.contentType()).isEqualTo("application/json");
    }

    @Test
    void export_shouldSerializeCalendarToValidJson() {
        CalendarKey key = new CalendarKey(WeekDay.MONDAY, false);
        Month january = new Month(MonthName.JANUARY, List.of(new Day(1, WeekDay.MONDAY)));
        Calendar calendar = new Calendar(key, List.of(january));

        byte[] result = exporter.export(2026, calendar);
        String json = new String(result, StandardCharsets.UTF_8);

        assertThat(json).contains("\"firstDayOfYear\":\"MONDAY\"");
        assertThat(json).contains("\"leapYear\":false");
        assertThat(json).contains("\"name\":\"JANUARY\"");
        assertThat(json).contains("\"dayOfMonth\":1");
    }
}