package cz.vsb.resbill.exception;

public class ContractPersonServiceException extends ResBillException {

	private static final long serialVersionUID = 4924874272709627751L;

	public static enum Reason {
		/** nelze menit prirazeni zodpovednych osob */
		CONTRACT_PERSON_MODIFICATION,
		/** prirazeni neni unikatni */
		NONUNIQUE_CONTRACT_PERSON
	}

	private Reason reason;

	public ContractPersonServiceException(Reason reason) {
		super();
		setReason(reason);
	}

	public ContractPersonServiceException(Reason reason, String message, Throwable cause) {
		super(message, cause);
		setReason(reason);
	}

	public ContractPersonServiceException(Reason reason, String message) {
		super(message);
		setReason(reason);
	}

	public ContractPersonServiceException(Reason reason, Throwable cause) {
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
		builder.append("ContractPersonServiceException [");
		builder.append(super.toString());
		builder.append(", reason=");
		builder.append(reason);
		builder.append("]");
		return builder.toString();
	}

}
