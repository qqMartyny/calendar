package com.ilyanin.calendar.api.controller;

import com.ilyanin.calendar.model.Calendar;
import com.ilyanin.calendar.model.WeekDay;
import com.ilyanin.calendar.service.CalendarExporter;
import com.ilyanin.calendar.service.CalendarExporterResolver;
import com.ilyanin.calendar.service.CalendarService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
public class CalendarController {

    private final CalendarService calendarService;
    private final CalendarExporterResolver exporterResolver;

    public CalendarController(CalendarService calendarService, CalendarExporterResolver exporterResolver) {
        this.calendarService = calendarService;
        this.exporterResolver = exporterResolver;
    }

    @GetMapping("/api/calendars/{year}")
    public ResponseEntity<byte[]> getCalendar(
            @PathVariable int year,
            @RequestParam(defaultValue = "json") String format
    ) {
        Calendar calendar = calendarService.getCalendarFor(year);

        CalendarExporter exporter = exporterResolver.resolve(format);
        byte[] body = exporter.export(year, calendar);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, exporter.contentType())
                .body(body);
    }

    @GetMapping("/api/weekday")
    public WeekDay getWeekDay(@RequestParam LocalDate date) {
        return calendarService.getWeekDay(date);
    }
}