package cz.vsb.resbill.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class Invoice extends Transaction {

	private Period period;
	// TODO file attachment
	// private File attachment;
	private InvoiceType invoiceType;

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
		ToStringBuilder builder = new ToStringBuilder(this);
		builder.append("period", period);
		builder.append("invoiceType", invoiceType);
		return builder.toString();
	}
}
