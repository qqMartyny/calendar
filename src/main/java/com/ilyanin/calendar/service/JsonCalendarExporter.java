package com.ilyanin.calendar.service;

import com.ilyanin.calendar.exception.CalendarExportException;
import com.ilyanin.calendar.model.Calendar;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.json.JsonMapper;

@Component
public class JsonCalendarExporter implements CalendarExporter {

    private final JsonMapper jsonMapper;

    public JsonCalendarExporter(JsonMapper jsonMapper) {
        this.jsonMapper = jsonMapper;
    }

    @Override
    public boolean supports(String format) {
        return "json".equalsIgnoreCase(format);
    }

    @Override
    public String contentType() {
        return MediaType.APPLICATION_JSON_VALUE;
    }

    @Override
    public byte[] export(int year, Calendar calendar) {
        try {
            return jsonMapper.writeValueAsBytes(calendar);
        } catch (JacksonException e) {
            throw new CalendarExportException("Could not serialize calendar for " + year + " year to JSON", e);
        }
    }
}