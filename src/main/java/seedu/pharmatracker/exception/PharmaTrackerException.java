package seedu.pharmatracker.exception;

/**
 * Custom exception class for PharmaTracker application.
 * Represents errors and exceptional conditions specific to the PharmaTracker system.
 */
public class PharmaTrackerException extends Exception {

    /**
     * Constructs a PharmaTrackerException with the specified detail message.
     *
     * @param message The detail message explaining what went wrong.
     */
    public PharmaTrackerException(String message) {
        super(message);
    }

    /**
     * Constructs a PharmaTrackerException with the specified detail message and cause.
     *
     * @param message The detail message explaining what went wrong.
     * @param cause The cause of this exception (another Throwable).
     */
    public PharmaTrackerException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a PharmaTrackerException with the specified cause.
     *
     * @param cause The cause of this exception (another Throwable).
     */
    public PharmaTrackerException(Throwable cause) {
        super(cause);
    }
}
