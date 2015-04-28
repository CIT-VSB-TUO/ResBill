package cz.vsb.resbill.model;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "CONTRACT")
public class Contract extends BaseVersionedEntity {

	private static final long serialVersionUID = 346744894255948553L;

	@Column(name = "evidence_number", length = 30, nullable = false)
	private Integer evidenceNumber;

	@Column(name = "name", length = 250, nullable = false)
	private String name;

	@Column(name = "note", length = 1000)
	private String note;

	@Column(name = "balance", nullable = false, precision = 16, scale = 2)
	private BigDecimal balance;

	@Embedded
	private Period period;

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "customer_id", nullable = false)
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

	public Period getPeriod() {
		return period;
	}

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

	public void setContractInvoiceTypes(
			Set<ContractInvoiceType> contractInvoiceTypes) {
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
