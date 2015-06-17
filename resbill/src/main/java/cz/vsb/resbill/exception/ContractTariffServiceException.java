package cz.vsb.resbill.exception;

public class ContractTariffServiceException extends ResBillException {

	private static final long serialVersionUID = -8537393080717676496L;

	public static enum Reason {
		/** nejedna se o posledni prirazeni tarifu */
		NOT_LAST_CONTRACT_TARIFF,
		/** prirazeni mimo platnost kontraktu */
		OUT_OF_CONTRACT_DURATION,
		/** Obdobi platnosti prirazeni je nespravne (konec predchazi pocatku) */
		INVALID_PERIOD,
		/** nelze menit prirazeni kontrakt-tarif */
		CONTRACT_TARIFF_MODIFICATION,
		/** nelze menit pocatek prvniho prirazeni */
		FIRST_CONTRACT_TARIFF_BEGIN_DATE_MODIFICATION,
		/** ceniky nepokryvaji obdobi prirazeni */
		NOT_COVERED_BY_PRICE_LISTS,
		/** nepokryva fakturovana denni vyuziti zdroju */
		INVOICE_DAILY_USAGE_UNCOVERED,
		/** ke kontraktu prirazen server */
		CONTRACT_SERVER_ASSOCIATED,
		/** existuji fakturovana denni vyuziti zdroju */
		DAILY_USAGE_INVOICED
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
