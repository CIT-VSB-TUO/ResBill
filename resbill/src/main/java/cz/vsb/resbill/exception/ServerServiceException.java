package cz.vsb.resbill.exception;

public class ServerServiceException extends ResBillException {

	private static final long serialVersionUID = -5200003171167256166L;

	public static enum Reason {
		/** ID serveru neni unikatni */
		NONUNIQUE_SERVER_ID,
		/** existuji prirazene kontrakty */
		CONTRACT_ASSOCIATED,
		/** existuji vygenerovane denni zpravy */
		DAILY_USAGE_EXISTENCE
	}

	private Reason reason;

	public ServerServiceException(Reason reason) {
		super();
		setReason(reason);
	}

	public ServerServiceException(Reason reason, String message, Throwable cause) {
		super(message, cause);
		setReason(reason);
	}

	public ServerServiceException(Reason reason, String message) {
		super(message);
		setReason(reason);
	}

	public ServerServiceException(Reason reason, Throwable cause) {
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
		builder.append("ServerServiceException [");
		builder.append(super.toString());
		builder.append(", reason=");
		builder.append(reason);
		builder.append("]");
		return builder.toString();
	}

}
