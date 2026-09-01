package com.ilyanin.calendar.service;

import com.ilyanin.calendar.model.Calendar;
import com.ilyanin.calendar.model.Day;
import com.ilyanin.calendar.model.Month;
import com.ilyanin.calendar.model.WeekDay;

import java.util.List;

public class CalendarPrinter {

    private static final int CELL_WIDTH = 4;
    private static final int MONTHS_PER_ROW = 3;

    public void print(int year, Calendar calendar) {
        System.out.println();
        System.out.println(centered("===== " + year + " год =====", CELL_WIDTH * 7 * MONTHS_PER_ROW + (MONTHS_PER_ROW - 1) * 3));
        System.out.println();

        List<Month> months = calendar.months();
        for (int i = 0; i < months.size(); i += MONTHS_PER_ROW) {
            List<Month> row = months.subList(i, Math.min(i + MONTHS_PER_ROW, months.size()));
            printMonthRow(row);
        }
    }

    private void printMonthRow(List<Month> row) {
        printTitles(row);
        printWeekDayHeaders(row);
        printDayGrids(row);
        System.out.println();
    }

    private void printTitles(List<Month> row) {
        StringBuilder sb = new StringBuilder();
        for (Month month : row) {
            String title = month.name().getDisplayName();
            sb.append(centered(title, CELL_WIDTH * 7)).append("   ");
        }
        System.out.println(sb.toString().stripTrailing());
    }

    private void printWeekDayHeaders(List<Month> row) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < row.size(); i++) {
            for (WeekDay day : WeekDay.values()) {
                sb.append(pad(day.getShortName()));
            }
            sb.append("   ");
        }
        System.out.println(sb.toString().stripTrailing());
    }

    private void printDayGrids(List<Month> row) {
        int maxLines = row.stream()
                .mapToInt(this::linesNeeded)
                .max()
                .orElse(0);

        for (int line = 0; line < maxLines; line++) {
            StringBuilder sb = new StringBuilder();
            for (Month month : row) {
                sb.append(buildLine(month, line));
                sb.append("   ");
            }
            System.out.println(sb.toString().stripTrailing());
        }
    }

    private int linesNeeded(Month month) {
        int firstDayOffset = month.days().get(0).weekDay().ordinal();
        int totalCells = firstDayOffset + month.days().size();
        return (int) Math.ceil(totalCells / 7.0);
    }

    private String buildLine(Month month, int lineIndex) {
        List<Day> days = month.days();
        int firstDayOffset = days.get(0).weekDay().ordinal();

        StringBuilder sb = new StringBuilder();
        for (int col = 0; col < 7; col++) {
            int cellIndex = lineIndex * 7 + col;
            int dayNumber = cellIndex - firstDayOffset + 1;

            if (dayNumber < 1 || dayNumber > days.size()) {
                sb.append(pad(""));
            } else {
                sb.append(pad(String.valueOf(dayNumber)));
            }
        }
        return sb.toString();
    }

    private String pad(String text) {
        return String.format("%" + CELL_WIDTH + "s", text);
    }

    private String centered(String text, int width) {
        if (text.length() >= width) {
            return text;
        }
        int totalPadding = width - text.length();
        int left = totalPadding / 2;
        int right = totalPadding - left;
        return " ".repeat(left) + text + " ".repeat(right);
    }
}