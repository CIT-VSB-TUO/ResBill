package cz.vsb.resbill.exception;

public class ConstraintViolationException extends ResBillException {

	private static final long serialVersionUID = 1L;

	public static enum Reason {
		UNIQUE_KEY
	}

	private Reason reason;

	public ConstraintViolationException(Reason reason) {
		super();
		setReason(reason);
	}

	public ConstraintViolationException(Reason reason, String message, Throwable cause) {
		super(message, cause);
		setReason(reason);
	}

	public ConstraintViolationException(Reason reason, String message) {
		super(message);
		setReason(reason);
	}

	public ConstraintViolationException(Reason reason, Throwable cause) {
		super(cause);
		setReason(reason);
	}

	public Reason getReason() {
		return reason;
	}

	public void setReason(Reason reason) {
		this.reason = reason;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ConstraintViolationException [");
		builder.append(super.toString());
		builder.append(", reason=");
		builder.append(reason);
		builder.append("]");
		return builder.toString();
	}

}
