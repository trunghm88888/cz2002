package hrps.exception;

/**
 * When trying to enter a date time input not in format yyyy-MM-dd HH:mm or the entered date time input does not
 * exist (eg 2022-02-30 12:00).
 */
public class InvalidDateTimeFormatException extends HRPSException {
    private static final String INVALID_DATE_TIME_MESSAGE = "Invalid date time format, please input yyyy-MM-dd HH:mm, eg. 2022-05-06 12:00";

    public InvalidDateTimeFormatException() {
        super(INVALID_DATE_TIME_MESSAGE);
    }
}
