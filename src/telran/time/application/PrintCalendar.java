package telran.time.application;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.time.temporal.ChronoField;
import java.util.Locale;

record MonthYear(int month, int year, int weekDay) {

}

public class PrintCalendar {

	private static final int TITLE_OFFSET = 5;
	private static final int COLUMN_WIDTH = 4;
	private static DayOfWeek[] weekDays = DayOfWeek.values();

	public static void main(String[] args) {
		try {
			MonthYear monthYear = getMonthYear(args);
			printCalendar(monthYear);
		} catch (RuntimeException e) {
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	private static MonthYear getMonthYear(String[] args) throws Exception {
		int monthNumber = getMonth(args);
		int year = getYear(args);
		int weekDay = getWeekDay(args);
		return new MonthYear(monthNumber, year, weekDay);
	}

	private static int getWeekDay(String[] args) throws Exception {
		int weekDay = args.length < 3 ? 1 : getWeekDay(args[2]);
		return weekDay;
	}

	private static int getWeekDay(String weekDayString) throws Exception {

		int res = findNumberOfDayOfWeek(weekDayString);
		if (res == -1) {
			throw new Exception("Day of the week is not valid");
		}

		return res;

	}

	private static int getYear(String[] args) throws Exception {
		int year = args.length < 2 ? getCurrentYear() : getYear(args[1]);
		return year;
	}

	private static int getYear(String yearStr) throws Exception {
		try {
			int res = Integer.parseInt(yearStr);
			return res;
		} catch (NumberFormatException e) {
			throw new Exception("year must be an integer number");
		}

	}

	private static int getCurrentYear() {

		return LocalDate.now().getYear();
	}

	private static int getMonth(String[] args) throws Exception {
		int month = args.length == 0 ? getCurrentMonth() : getMonthNumber(args[0]);
		return month;
	}

	private static int getMonthNumber(String monthStr) throws Exception {
		try {
			int result = Integer.parseInt(monthStr);
			if (result < 1) {
				throw new Exception("Month cannot be less than 1");
			}
			if (result > 12) {
				throw new Exception("Month cannot be greater than 12");
			}
			return result;
		} catch (NumberFormatException e) {
			throw new Exception("Month must be a number");
		}
	}

	private static int getCurrentMonth() {

		return LocalDate.now().get(ChronoField.MONTH_OF_YEAR);
	}

	private static void printCalendar(MonthYear monthYear) {
		printTitle(monthYear);
		printWeekDays(monthYear);
		printDays(monthYear);

	}

	private static void printDays(MonthYear monthYear) {
		int nDays = getDaysInMonth(monthYear);
		int currentWeekDay = getFirstDayOfMonth(monthYear);
		int firstOffset = getFirstOffset(currentWeekDay, monthYear);
		System.out.printf("%s", " ".repeat(firstOffset));
		for (int day = 1; day <= nDays; day++) {
			System.out.printf("%" + COLUMN_WIDTH + "d", day);
			if (currentWeekDay == weekDays.length) {
				currentWeekDay = 0;
				System.out.println();
			}
			currentWeekDay++;
		}

	}

	private static int getFirstOffset(int currentWeekDay, MonthYear monthYear) {
		return COLUMN_WIDTH * (currentWeekDay - 1);
	}

	private static int getFirstDayOfMonth(MonthYear monthYear) {
		LocalDate ld = LocalDate.of(monthYear.year(), monthYear.month(), 1);
		DayOfWeek ldToDW = DayOfWeek.of(ld.get(ChronoField.DAY_OF_WEEK)).minus(monthYear.weekDay() - 1);
		return ldToDW.getValue();
	}

	private static int getDaysInMonth(MonthYear monthYear) {
		YearMonth ym = YearMonth.of(monthYear.year(), monthYear.month());
		return ym.lengthOfMonth();
	}

	private static void printWeekDays(MonthYear monthYear) {
		System.out.printf("%s", " ".repeat(1));
		for (DayOfWeek weekday : weekDays) {
			System.out.printf("%" + COLUMN_WIDTH + "s",
					weekday.plus(monthYear.weekDay() - 1).getDisplayName(TextStyle.SHORT, Locale.forLanguageTag("en")));

		}
		System.out.println();

	}

	private static int findNumberOfDayOfWeek(String weekDay) {
		int res = -1;
		for (DayOfWeek weekday : weekDays) {
			if (weekday.toString().equals(weekDay.toUpperCase())) {
				res = weekday.getValue();
			}
		}
		return res;
	}

	private static void printTitle(MonthYear monthYear) {
		String monthName = Month.of(monthYear.month()).getDisplayName(TextStyle.FULL, Locale.getDefault());
		System.out.printf("%s%s %d\n", " ".repeat(TITLE_OFFSET), monthName, monthYear.year());

	}

}
