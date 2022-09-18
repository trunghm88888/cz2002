package hrps.exception;

/**
 * When trying to cancel a CheckedInReservation.
 */
public class CancelCheckInReservationException extends HRPSException {
    private static final String CANCEL_CHECK_IN_MESSAGE = "Sorry, this reservation has been checked in , you cannot cancel it!";

    public CancelCheckInReservationException() {
        super(CANCEL_CHECK_IN_MESSAGE);
    }
}
