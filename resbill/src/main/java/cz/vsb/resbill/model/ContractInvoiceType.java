package cz.vsb.resbill.model;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "CONTRACT_INVOICE_TYPE")
public class ContractInvoiceType extends BaseVersionedEntity {

	private static final long serialVersionUID = 2028293909241144177L;

	@Embedded
	private Period period;

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "contract_id", nullable = false)
	private Contract contract;

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "invoice_type_id", nullable = false)
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
		StringBuilder builder = new StringBuilder();
		builder.append("ContractInvoiceType [");
		builder.append(super.toString());
		builder.append(", period=");
		builder.append(period);
		builder.append(", contract.id=");
		builder.append(contract != null ? contract.getId() : null);
		builder.append(", invoiceType.id=");
		builder.append(invoiceType != null ? invoiceType.getId() : null);
		builder.append("]");
		return builder.toString();
	}

}
