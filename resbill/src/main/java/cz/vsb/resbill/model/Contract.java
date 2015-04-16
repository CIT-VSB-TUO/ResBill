package cz.vsb.resbill.model;

import java.math.BigDecimal;
import java.util.Set;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class Contract {

	private Integer evidenceNumber;
	private String name;
	private String note;
	private BigDecimal balance;
	private Period period;
	private Set<Transaction> transactions;
	private Set<Person> responsiblePersons;
	private Customer customer;
	private Set<ContractInvoiceType> contractInvoiceTypes;
	private InvoiceType currentInvoiceType;
	private Set<ContractServer> contractServers;
	private Set<Server> currentServers;
	private Set<ContractTariff> contractTariffs;
	private Tariff currentTariff;

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

	public Set<Person> getResponsiblePersons() {
		return responsiblePersons;
	}

	public void setResponsiblePersons(Set<Person> responsiblePersons) {
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

	public InvoiceType getCurrentInvoiceType() {
		return currentInvoiceType;
	}

	public void setCurrentInvoiceType(InvoiceType currentInvoiceType) {
		this.currentInvoiceType = currentInvoiceType;
	}

	public Set<ContractServer> getContractServers() {
		return contractServers;
	}

	public void setContractServers(Set<ContractServer> contractServers) {
		this.contractServers = contractServers;
	}

	public Set<Server> getCurrentServers() {
		return currentServers;
	}

	public void setCurrentServers(Set<Server> currentServers) {
		this.currentServers = currentServers;
	}

	public Set<ContractTariff> getContractTariffs() {
		return contractTariffs;
	}

	public void setContractTariffs(Set<ContractTariff> contractTariffs) {
		this.contractTariffs = contractTariffs;
	}

	public Tariff getCurrentTariff() {
		return currentTariff;
	}

	public void setCurrentTariff(Tariff currentTariff) {
		this.currentTariff = currentTariff;
	}

	@Override
	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this);
		builder.append("evidenceNumber", evidenceNumber);
		builder.append("name", name);
		builder.append("note", note);
		builder.append("balance", balance);
		builder.append("period", period);
		builder.append("transactions", transactions);
		builder.append("responsiblePersons", responsiblePersons);
		builder.append("customer", customer);
		builder.append("contractInvoiceTypes", contractInvoiceTypes);
		builder.append("currentInvoiceType", currentInvoiceType);
		builder.append("contractServers", contractServers);
		builder.append("currentServers", currentServers);
		builder.append("contractTariffs", contractTariffs);
		builder.append("currentTariff", currentTariff);
		return builder.toString();
	}
}
