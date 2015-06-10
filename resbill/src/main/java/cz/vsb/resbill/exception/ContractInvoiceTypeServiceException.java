package cz.vsb.resbill.exception;

public class ContractInvoiceTypeServiceException extends ResBillException {

	private static final long serialVersionUID = -8537393080717676496L;

	public static enum Reason {
		// TODO doplnit duvody
	}

	private Reason reason;

	public ContractInvoiceTypeServiceException(Reason reason) {
		super();
		setReason(reason);
	}

	public ContractInvoiceTypeServiceException(Reason reason, String message, Throwable cause) {
		super(message, cause);
		setReason(reason);
	}

	public ContractInvoiceTypeServiceException(Reason reason, String message) {
		super(message);
		setReason(reason);
	}

	public ContractInvoiceTypeServiceException(Reason reason, Throwable cause) {
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
		builder.append("ContractInvoiceTypeServiceException [");
		builder.append(super.toString());
		builder.append(", reason=");
		builder.append(reason);
		builder.append("]");
		return builder.toString();
	}

}
