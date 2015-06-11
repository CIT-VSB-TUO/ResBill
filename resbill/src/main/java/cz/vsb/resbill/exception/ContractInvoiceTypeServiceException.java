package cz.vsb.resbill.exception;

public class ContractInvoiceTypeServiceException extends ResBillException {

	private static final long serialVersionUID = -8537393080717676496L;

	public static enum Reason {
		// TODO doplnit duvody
		/** nejedna se o posledni prirazeni typu uctovani */
		NOT_LAST_CONTRACT_INVOICE_TYPE,
		/** prirazeni mimo platnost kontraktu */
		OUT_OF_CONTRACT_DURATION,
		/** Obdobi platnosti prirazeni je nespravne (konec predchazi pocatku) */
		INVALID_PERIOD,
		/** nelze menit prirazeni kontrakt-typ uctovani */
		CONTRACT_INVOICE_TYPE_MODIFICATION,
		/** nelze menit pocatek prvniho prirazeni */
		FIRST_CONTRACT_INVOICE_TYPE_BEGIN_DATE_MODIFICATION
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
