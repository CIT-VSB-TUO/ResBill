package cz.vsb.resbill.exception;

public class TariffServiceException extends ResBillException {

	private static final long serialVersionUID = 8226154005905011109L;

	public static enum Reason {

		/** Tariff je prirazen ke kontraktu */
		CONTRACT_TARIFF
	}

	private Reason reason;

	public TariffServiceException(Reason reason) {
		super();
		setReason(reason);
	}

	public TariffServiceException(Reason reason, String message, Throwable cause) {
		super(message, cause);
		setReason(reason);
	}

	public TariffServiceException(Reason reason, String message) {
		super(message);
		setReason(reason);
	}

	public TariffServiceException(Reason reason, Throwable cause) {
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
		builder.append("TariffServiceException [");
		builder.append(super.toString());
		builder.append(", reason=");
		builder.append(reason);
		builder.append("]");
		return builder.toString();
	}

}
