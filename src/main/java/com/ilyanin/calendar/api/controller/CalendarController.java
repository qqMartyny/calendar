package com.ilyanin.calendar.api.controller;

import com.ilyanin.calendar.model.Calendar;
import com.ilyanin.calendar.model.WeekDay;
import com.ilyanin.calendar.service.CalendarExporter;
import com.ilyanin.calendar.service.CalendarExporterResolver;
import com.ilyanin.calendar.service.CalendarService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@Tag(name = "Calendar", description = "Построение григорианского календаря и определение дня недели")
public class CalendarController {

    private final CalendarService calendarService;
    private final CalendarExporterResolver exporterResolver;

    public CalendarController(CalendarService calendarService, CalendarExporterResolver exporterResolver) {
        this.calendarService = calendarService;
        this.exporterResolver = exporterResolver;
    }

    @Operation(
            summary = "Получить календарь на указанный год",
            description = "Всего существует 14 уникальных календарей (7 дней недели, на которые "
                    + "может прийтись 1 января, × 2 варианта високосности года) — для года с уже "
                    + "запрошенным ранее ключом возвращается сохранённая запись без пересчёта."
    )
    @ApiResponse(responseCode = "200", description = "Календарь успешно построен/найден")
    @ApiResponse(responseCode = "400", description = "Год меньше 1600 или запрошенный формат не поддерживается")
    @GetMapping("/api/calendars/{year}")
    public ResponseEntity<byte[]> getCalendar(
            @Parameter(description = "Год григорианского календаря", example = "2026")
            @PathVariable int year,
            @Parameter(description = "Формат ответа", example = "json",
                    schema = @Schema(allowableValues = {"json"}))
            @RequestParam(defaultValue = "json") String format
    ) {
        Calendar calendar = calendarService.getCalendarFor(year);

        CalendarExporter exporter = exporterResolver.resolve(format);
        byte[] body = exporter.export(year, calendar);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, exporter.contentType())
                .body(body);
    }

    @Operation(summary = "Определить день недели по дате")
    @ApiResponse(responseCode = "200", description = "День недели определён")
    @ApiResponse(responseCode = "400", description = "Год меньше 1600 или дата некорректна")
    @GetMapping("/api/weekday")
    public WeekDay getWeekDay(
            @Parameter(description = "Дата в формате ISO-8601", example = "2000-01-01")
            @RequestParam LocalDate date
    ) {
        return calendarService.getWeekDay(date);
    }
}