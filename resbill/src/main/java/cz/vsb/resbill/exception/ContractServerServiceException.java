package cz.vsb.resbill.exception;

public class ContractServerServiceException extends ResBillException {

	private static final long serialVersionUID = 5094488574188862374L;

	public static enum Reason {
		/** server je v obdobi jiz prirazen */
		SERVER_ASSOCIATION_PERIOD_COLLISION,
		/** nelze menit prirazeni kontrakt-server */
		CONTRACT_SERVER_MODIFICATION,
		/** obdobi prirazeni nespada do trvani kontraktu */
		OUT_OF_CONTRACT_DURATION,
		/** kontrakt nema prirazen tarif */
		CONTRACT_WITHOUT_TARIFF,
		/** nepokryva fakturovana denni vyuziti zdroju */
		INVOICE_DAILY_USAGE_UNCOVERED,
		/** existuji fakturovana denni vyuziti zdroju */
		DAILY_USAGE_INVOICED
	}

	private Reason reason;

	public ContractServerServiceException(Reason reason) {
		super();
		setReason(reason);
	}

	public ContractServerServiceException(Reason reason, String message, Throwable cause) {
		super(message, cause);
		setReason(reason);
	}

	public ContractServerServiceException(Reason reason, String message) {
		super(message);
		setReason(reason);
	}

	public ContractServerServiceException(Reason reason, Throwable cause) {
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
		builder.append("ContractServerServiceException [");
		builder.append(super.toString());
		builder.append(", reason=");
		builder.append(reason);
		builder.append("]");
		return builder.toString();
	}

}
