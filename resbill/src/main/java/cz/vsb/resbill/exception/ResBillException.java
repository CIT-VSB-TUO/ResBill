package cz.vsb.resbill.exception;

/**
 * General superclass for checked exceptions thrown by ResBill.
 * 
 * @author HAL191
 *
 */
public class ResBillException extends Exception {

	private static final long serialVersionUID = 4733942737139555809L;

	public ResBillException() {
		super();
	}

	public ResBillException(String message, Throwable cause) {
		super(message, cause);
	}

	public ResBillException(String message) {
		super(message);
	}

	public ResBillException(Throwable cause) {
		super(cause);
	}

}
