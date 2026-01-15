package br.com.edneysiqueira.radarinvest.infrastructure.util;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.util.HashSet;
import java.util.Set;

public class BusinessDayCalculator {

    /**
     * Calculates the Nth business day of a given month.
     * 
     * @param year           The year
     * @param month          The month (1-12)
     * @param nthBusinessDay The Nth business day to find.
     *                       If > 0, counts from the start of the month.
     *                       If < 0, counts backwards from the end of the month (-1
     *                       is the last business day).
     * @return The LocalDate of the Nth business day, or null if invalid inputs.
     */
    public static LocalDate calculateNthBusinessDay(int year, int month, int nthBusinessDay) {
        if (nthBusinessDay == 0)
            return null; // Invalid

        Set<LocalDate> holidays = getHolidays(year);
        LocalDate baseDate = LocalDate.of(year, month, 1);
        int daysInMonth = baseDate.lengthOfMonth();

        if (nthBusinessDay > 0) {
            // Count forward from the 1st
            int businessDaysFound = 0;
            for (int i = 1; i <= daysInMonth; i++) {
                LocalDate current = baseDate.withDayOfMonth(i);
                if (isBusinessDay(current, holidays)) {
                    businessDaysFound++;
                    if (businessDaysFound == nthBusinessDay) {
                        return current;
                    }
                }
            }
        } else {
            // Count backward from the last day
            int targetCount = Math.abs(nthBusinessDay);
            int businessDaysFound = 0;
            for (int i = daysInMonth; i >= 1; i--) {
                LocalDate current = baseDate.withDayOfMonth(i);
                if (isBusinessDay(current, holidays)) {
                    businessDaysFound++;
                    if (businessDaysFound == targetCount) {
                        return current;
                    }
                }
            }
        }

        return null; // Should not happen unless N is larger than total business days
    }

    private static boolean isBusinessDay(LocalDate date, Set<LocalDate> holidays) {
        DayOfWeek dow = date.getDayOfWeek();
        if (dow == DayOfWeek.SATURDAY || dow == DayOfWeek.SUNDAY) {
            return false;
        }
        return !holidays.contains(date);
    }

    private static Set<LocalDate> getHolidays(int year) {
        Set<LocalDate> holidays = new HashSet<>();
        // Fixed National Holidays in Brazil
        holidays.add(LocalDate.of(year, Month.JANUARY, 1)); // Confraternização Universal
        holidays.add(LocalDate.of(year, Month.APRIL, 21)); // Tiradentes
        holidays.add(LocalDate.of(year, Month.MAY, 1)); // Dia do Trabalho
        holidays.add(LocalDate.of(year, Month.SEPTEMBER, 7)); // Independência do Brasil
        holidays.add(LocalDate.of(year, Month.OCTOBER, 12)); // Nossa Senhora Aparecida
        holidays.add(LocalDate.of(year, Month.NOVEMBER, 2)); // Finados
        holidays.add(LocalDate.of(year, Month.NOVEMBER, 15)); // Proclamação da República
        holidays.add(LocalDate.of(year, Month.NOVEMBER, 20)); // Dia da Consciência Negra
        holidays.add(LocalDate.of(year, Month.DECEMBER, 25)); // Natal
        return holidays;
    }
}
