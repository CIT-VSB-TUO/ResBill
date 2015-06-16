package cz.vsb.resbill.exception;

public class ContractServiceException extends ResBillException {

	private static final long serialVersionUID = 4710854518407862466L;

	public static enum Reason {
		/** existuje transakce */
		TRANSACTION_EXISTENCE,
		/** pokus o zmenu zakaznika */
		CUSTOMER_MODIFICATION,
		/** platnost kontraktu nezahrnuje vsechna existujici prirazeni serveru */
		SERVER_ASSOCIATION_EXCLUSION,
		/** zmena platnosti kontraktu zpusobi nespravnost prirazeni typu uct. */
		CONTRACT_INVOICE_TYPE_INVALID_PERIOD,
		/** zmena platnosti kontraktu zpusobi nespravnost prirazeni tarifu */
		CONTRACT_TARIFF_INVALID_PERIOD,
		/** nepokryva fakturovana denni vyuziti zdroju prirazeni tarifu */
		CONTRACT_TARIFF_INVOICE_DAILY_USAGE_UNCOVERED
	}

	private Reason reason;

	public ContractServiceException(Reason reason) {
		super();
		setReason(reason);
	}

	public ContractServiceException(Reason reason, String message, Throwable cause) {
		super(message, cause);
		setReason(reason);
	}

	public ContractServiceException(Reason reason, String message) {
		super(message);
		setReason(reason);
	}

	public ContractServiceException(Reason reason, Throwable cause) {
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
		builder.append("ContractServiceException [");
		builder.append(super.toString());
		builder.append(", reason=");
		builder.append(reason);
		builder.append("]");
		return builder.toString();
	}
}
