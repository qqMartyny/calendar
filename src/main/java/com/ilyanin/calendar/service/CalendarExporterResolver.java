package com.ilyanin.calendar.service;

import org.springframework.stereotype.Component;

import com.ilyanin.calendar.exception.UnsupportedExportFormatException;

import java.util.List;

@Component
public class CalendarExporterResolver {

    private final List<CalendarExporter> exporters;

    public CalendarExporterResolver(List<CalendarExporter> exporters) {
        this.exporters = exporters;
    }

    public CalendarExporter resolve(String format) {
        return exporters.stream()
                .filter(exporter -> exporter.supports(format))
                .findFirst()
                .orElseThrow(() -> new UnsupportedExportFormatException(format));
    }
}