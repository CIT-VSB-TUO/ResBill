package cz.vsb.resbill.model;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name = "CONTRACT", uniqueConstraints = @UniqueConstraint(name = "UK_contract__evidence_number", columnNames = "evidence_number"))
public class Contract extends BaseVersionedEntity implements PeriodLimitedEntity {

	private static final long serialVersionUID = 346744894255948553L;

	@Column(name = "evidence_number")
	@NotNull
	@Digits(integer = 10, fraction = 0)
	private Integer evidenceNumber;

	@Column(name = "name")
	@Size(max = 250)
	@NotEmpty
	private String name;

	@Column(name = "note")
	@Size(max = 1000)
	private String note;

	@Column(name = "balance")
	@NotNull
	@Digits(integer = 14, fraction = 2)
	private BigDecimal balance;

	@Embedded
	@NotNull
	@Valid
	private Period period;

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "customer_id", foreignKey = @ForeignKey(name = "FK_contract__customer"))
	@NotNull
	private Customer customer;

	@OneToMany(mappedBy = "contract", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<Transaction> transactions = new HashSet<>();

	@OneToMany(mappedBy = "contract", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<ContractPerson> responsiblePersons = new HashSet<>();

	@OneToMany(mappedBy = "contract", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<ContractInvoiceType> contractInvoiceTypes = new HashSet<>();

	@OneToMany(mappedBy = "contract", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<ContractServer> contractServers = new HashSet<>();

	@OneToMany(mappedBy = "contract", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<ContractTariff> contractTariffs = new HashSet<>();

	public Integer getEvidenceNumber() {
		return evidenceNumber;
	}

	public void setEvidenceNumber(Integer evidenceNumber) {
		this.evidenceNumber = evidenceNumber;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	@Override
	public Period getPeriod() {
		return period;
	}

	@Override
	public void setPeriod(Period period) {
		this.period = period;
	}

	public Set<Transaction> getTransactions() {
		return transactions;
	}

	public void setTransactions(Set<Transaction> transactions) {
		this.transactions = transactions;
	}

	public Set<ContractPerson> getResponsiblePersons() {
		return responsiblePersons;
	}

	public void setResponsiblePersons(Set<ContractPerson> responsiblePersons) {
		this.responsiblePersons = responsiblePersons;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Set<ContractInvoiceType> getContractInvoiceTypes() {
		return contractInvoiceTypes;
	}

	public void setContractInvoiceTypes(Set<ContractInvoiceType> contractInvoiceTypes) {
		this.contractInvoiceTypes = contractInvoiceTypes;
	}

	public Set<ContractServer> getContractServers() {
		return contractServers;
	}

	public void setContractServers(Set<ContractServer> contractServers) {
		this.contractServers = contractServers;
	}

	public Set<ContractTariff> getContractTariffs() {
		return contractTariffs;
	}

	public void setContractTariffs(Set<ContractTariff> contractTariffs) {
		this.contractTariffs = contractTariffs;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Contract [");
		builder.append(super.toString());
		builder.append(", evidenceNumber=");
		builder.append(evidenceNumber);
		builder.append(", name=");
		builder.append(name);
		builder.append(", note=");
		builder.append(note);
		builder.append(", balance=");
		builder.append(balance);
		builder.append(", period=");
		builder.append(period);
		builder.append(", customer.id=");
		builder.append(customer != null ? customer.getId() : null);
		builder.append("]");
		return builder.toString();
	}
}
