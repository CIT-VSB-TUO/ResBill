package cz.vsb.resbill.exception;

public class CustomerServiceException extends ResBillException {

	private static final long serialVersionUID = -2679027729416407521L;

	public static enum Reason {
		/** Nazev neni unikatni */
		NONUNIQUE_NAME,

		/** Zakaznik jiz ma vytvorene kontrakty */
		CONTRACTS_EXIST
	}

	private Reason reason;

	public CustomerServiceException(Reason reason) {
		super();
		setReason(reason);
	}

	public CustomerServiceException(Reason reason, String message, Throwable cause) {
		super(message, cause);
		setReason(reason);
	}

	public CustomerServiceException(Reason reason, String message) {
		super(message);
		setReason(reason);
	}

	public CustomerServiceException(Reason reason, Throwable cause) {
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
		builder.append("CustomerServiceException [");
		builder.append(super.toString());
		builder.append(", reason=");
		builder.append(reason);
		builder.append("]");
		return builder.toString();
	}

}
