package cz.vsb.resbill.exception;

public class PriceListServiceException extends ResBillException {

	private static final long serialVersionUID = -4874174019205202442L;

	public static enum Reason {
		INVOICE_PRICE_LIST, INVOICE_DATE_CLASH, INVALID_PERIOD
	}

	private Reason reason;

	public PriceListServiceException(Reason reason) {
		super();
		setReason(reason);
	}

	public PriceListServiceException(Reason reason, String message, Throwable cause) {
		super(message, cause);
		setReason(reason);
	}

	public PriceListServiceException(Reason reason, String message) {
		super(message);
		setReason(reason);
	}

	public PriceListServiceException(Reason reason, Throwable cause) {
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
		builder.append("PriceListServiceException [");
		builder.append(super.toString());
		builder.append(", reason=");
		builder.append(reason);
		builder.append("]");
		return builder.toString();
	}

}
