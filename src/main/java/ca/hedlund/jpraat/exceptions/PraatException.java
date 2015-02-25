package ca.hedlund.jpraat.exceptions;

public class PraatException extends Exception {

	private static final long serialVersionUID = 376837834212187611L;

	public PraatException() {
		super();
	}

	public PraatException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public PraatException(String message, Throwable cause) {
		super(message, cause);
	}

	public PraatException(String message) {
		super(message);
	}

	public PraatException(Throwable cause) {
		super(cause);
	}

	
}
