package com.ilyanin.calendar.service;

import com.ilyanin.calendar.exception.UnsupportedExportFormatException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CalendarExporterResolverTest {

    @Mock
    private CalendarExporter jsonExporter;

    @Mock
    private CalendarExporter xmlExporter;

    private CalendarExporterResolver resolver;

    @BeforeEach
    void setUp() {
        resolver = new CalendarExporterResolver(List.of(jsonExporter, xmlExporter));
    }

    @Test
    void resolve_shouldReturnExporterThatSupportsRequestedFormat() {
        when(jsonExporter.supports("json")).thenReturn(true);

        CalendarExporter result = resolver.resolve("json");

        assertThat(result).isSameAs(jsonExporter);
    }

    @Test
    void resolve_shouldSkipExporterThatDoesNotSupportFormat() {
        when(jsonExporter.supports("xml")).thenReturn(false);
        when(xmlExporter.supports("xml")).thenReturn(true);

        CalendarExporter result = resolver.resolve("xml");

        assertThat(result).isSameAs(xmlExporter);
    }

    @Test
    void resolve_shouldThrowWhenNoExporterSupportsFormat() {
        when(jsonExporter.supports("qqq")).thenReturn(false);
        when(xmlExporter.supports("qqq")).thenReturn(false);

        assertThatThrownBy(() -> resolver.resolve("qqq"))
                .isInstanceOf(UnsupportedExportFormatException.class);
    }

    @Test
    void resolve_shouldThrowWhenNoExportersRegisteredAtAll() {
        CalendarExporterResolver emptyResolver = new CalendarExporterResolver(List.of());

        assertThatThrownBy(() -> emptyResolver.resolve("json"))
                .isInstanceOf(UnsupportedExportFormatException.class);
    }
}