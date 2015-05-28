package cz.vsb.resbill.exception;

public class PersonServiceException extends ResBillException {

	private static final long serialVersionUID = 1L;

	public static enum Reason {
		/** Email neni unikatni */
		NONUNIQUE_EMAIL,

		/** Osoba je kontaktem zakaznika */
		CUSTOMERS_CONTACT_PERSON,

		/** Osoba zodpovedna za kontrakt */
		CONTRACT_RESPONSIBILITY
	}

	private Reason reason;

	public PersonServiceException(Reason reason) {
		super();
		setReason(reason);
	}

	public PersonServiceException(Reason reason, String message, Throwable cause) {
		super(message, cause);
		setReason(reason);
	}

	public PersonServiceException(Reason reason, String message) {
		super(message);
		setReason(reason);
	}

	public PersonServiceException(Reason reason, Throwable cause) {
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
		builder.append("PersonServiceException [");
		builder.append(super.toString());
		builder.append(", reason=");
		builder.append(reason);
		builder.append("]");
		return builder.toString();
	}

}
