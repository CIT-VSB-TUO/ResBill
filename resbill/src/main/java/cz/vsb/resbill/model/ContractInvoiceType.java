package cz.vsb.resbill.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class ContractInvoiceType {

	private Period period;
	private Contract contract;
	private InvoiceType invoiceType;

	public Period getPeriod() {
		return period;
	}

	public void setPeriod(Period period) {
		this.period = period;
	}

	public Contract getContract() {
		return contract;
	}

	public void setContract(Contract contract) {
		this.contract = contract;
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
		builder.append("contract", contract);
		builder.append("invoiceType", invoiceType);
		return builder.toString();
	}

}
