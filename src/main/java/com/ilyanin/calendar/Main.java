package com.ilyanin.calendar;

import com.ilyanin.calendar.model.Calendar;
import com.ilyanin.calendar.service.CalendarFactory;
import com.ilyanin.calendar.service.CalendarPrinter;
import com.ilyanin.calendar.service.CalendarRegistry;
import com.ilyanin.calendar.service.WeekDayCalculator;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        CalendarFactory calendarFactory = new CalendarFactory(new WeekDayCalculator());
        CalendarRegistry calendarRegistry = new CalendarRegistry(calendarFactory);
        CalendarPrinter calendarPrinter = new CalendarPrinter();

        System.out.println("Всего уникальных календарей (независимо от года): "
                + calendarRegistry.uniqueCalendarsCount());

        try (Scanner scanner = new Scanner(System.in)) {
            while(true) {
                System.out.print("Введите год: ");
                int year = readYear(scanner);

                Calendar calendar = calendarRegistry.getCalendarFor(year);
                calendarPrinter.print(year, calendar);
            }
        }
    }

    private static int readYear(Scanner scanner) {
        while (!scanner.hasNextInt()) {
            System.out.print("Некорректный ввод. Введите год числом: ");
            scanner.next();
        }
        return scanner.nextInt();
    }
}