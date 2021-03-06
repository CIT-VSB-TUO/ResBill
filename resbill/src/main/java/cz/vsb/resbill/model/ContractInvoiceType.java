package cz.vsb.resbill.model;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "CONTRACT_INVOICE_TYPE", uniqueConstraints = @UniqueConstraint(name = "UK_contract_invoice_type__contract_id__invoice_type_id__begin_date", columnNames = { "contract_id",
		"invoice_type_id", "begin_date" }))
public class ContractInvoiceType extends BaseVersionedEntity implements PeriodLimitedEntity {

	private static final long serialVersionUID = 2028293909241144177L;

	@Embedded
	@NotNull
	@Valid
	private Period period;

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "contract_id", nullable = false, foreignKey = @ForeignKey(name = "FK_contract_invoice_type__contract"))
	private Contract contract;

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "invoice_type_id", nullable = false, foreignKey = @ForeignKey(name = "FK_contract_invoice_type__invoice_type"))
	private InvoiceType invoiceType;

	@OneToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "previous_id", foreignKey = @ForeignKey(name = "FK_contract_invoice_type__previous"))
	private ContractInvoiceType previous;

	@Override
	public Period getPeriod() {
		return period;
	}

	@Override
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

	public ContractInvoiceType getPrevious() {
		return previous;
	}

	public void setPrevious(ContractInvoiceType previous) {
		this.previous = previous;
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
		builder.append(", previous.id=");
		builder.append(previous != null ? previous.getId() : null);
		builder.append("]");
		return builder.toString();
	}

}
