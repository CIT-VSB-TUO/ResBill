package cz.vsb.resbill.exception;

public class PriceListServiceException extends ResBillException {

	private static final long serialVersionUID = -4874174019205202442L;

	public static enum Reason {
		/** jedna se o prvni cenik */
		FIRST_PRICE_LIST,
		/** nejedna se o posledni cenik */
		NOT_LAST_PRICE_LIST,
		/** podle ceniku je uz vytvorena faktura */
		INVOICE_EXISTENCE,
		/** Platnost ceniku zasahuje do obdobi, ktere uz bylo fakturovano */
		INVOICE_DATE_COLLISION,
		/** Platnost ceniku nepokryva obdobi prirazeni tarifu ke kontraktu */
		CONTRACT_PERIOD_UNCOVERED,
		/** Obdobi platnosti ceniku je nespravne (konec predchazi pocatku) */
		INVALID_PERIOD,
		/** pokus zmenit tarif ceniku */
		TARIFF_MODIFICATION
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
