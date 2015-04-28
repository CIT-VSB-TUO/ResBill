package cz.vsb.resbill.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "TRANASCTION_TYPE")
public class TransactionType extends BaseEnumeratedIdEntity {

	private static final long serialVersionUID = -502862889044300245L;

	/** Invoice */
	public static final Integer INVOICE = 1;

	/** Incoming payment */
	public static final Integer INCOMING_PAYMENT = 2;

	/** Additional payment */
	public static final Integer ADDITIONAL_PAYMENT = 3;

	@Column(name = "title", length = 100, nullable = false)
	private String title;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("TransactionType [");
		builder.append(super.toString());
		builder.append(", title=");
		builder.append(title);
		builder.append("]");
		return builder.toString();
	}

}
