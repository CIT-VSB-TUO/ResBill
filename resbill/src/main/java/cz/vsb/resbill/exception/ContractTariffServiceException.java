package cz.vsb.resbill.exception;


public class ContractTariffServiceException extends ResBillException {

	private static final long serialVersionUID = -8537393080717676496L;

	public static enum Reason {
	}

	private Reason reason;

	public ContractTariffServiceException(Reason reason) {
		super();
		setReason(reason);
	}

	public ContractTariffServiceException(Reason reason, String message, Throwable cause) {
		super(message, cause);
		setReason(reason);
	}

	public ContractTariffServiceException(Reason reason, String message) {
		super(message);
		setReason(reason);
	}

	public ContractTariffServiceException(Reason reason, Throwable cause) {
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
		builder.append("ContractTariffServiceException [");
		builder.append(super.toString());
		builder.append(", reason=");
		builder.append(reason);
		builder.append("]");
		return builder.toString();
	}

}
