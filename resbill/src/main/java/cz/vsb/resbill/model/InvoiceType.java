package cz.vsb.resbill.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name = "INVOICE_TYPE")
public class InvoiceType extends BaseEnumeratedIdEntity {

	private static final long serialVersionUID = -5801051324020018291L;

	/** Monthly produced invoices */
	public static final Integer MONTHLY = 1;

	/** Quarterly produced invoices */
	public static final Integer QUARTERLY = 2;

	/** Half-yearly produced invoices */
	public static final Integer HALF_YEARLY = 3;

	/** Annually produced invoices */
	public static final Integer ANNUALLY = 4;

	/** The title of the type */
	@Column(name = "title")
	@NotEmpty
	@Size(max = 100)
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
		builder.append("InvoiceType [");
		builder.append(super.toString());
		builder.append(", title=");
		builder.append(title);
		builder.append("]");
		return builder.toString();
	}

}
