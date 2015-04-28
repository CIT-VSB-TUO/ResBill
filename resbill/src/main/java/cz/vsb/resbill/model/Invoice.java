package cz.vsb.resbill.model;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

@Entity
@DiscriminatorValue(value = "inv")
public class Invoice extends Transaction {

	private static final long serialVersionUID = -3016705349881450838L;

	@Embedded
	private Period period;

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@Column(name = "invoice_type_id", nullable = false)
	private InvoiceType invoiceType;

	// TODO file attachment
	// private File attachment;

	public Period getPeriod() {
		return period;
	}

	public void setPeriod(Period period) {
		this.period = period;
	}

	public InvoiceType getInvoiceType() {
		return invoiceType;
	}

	public void setInvoiceType(InvoiceType invoiceType) {
		this.invoiceType = invoiceType;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Invoice [");
		builder.append(super.toString());
		builder.append(", period=");
		builder.append(period);
		builder.append(", invoiceType.id=");
		builder.append(invoiceType != null ? invoiceType.getId() : null);
		builder.append("]");
		return builder.toString();
	}
}
