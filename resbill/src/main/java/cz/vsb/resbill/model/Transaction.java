package cz.vsb.resbill.model;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class Transaction {

	private Integer order;
	private Integer evidenceNumber;
	private Date decisiveDate;
	private BigDecimal amount;
	private String title;
	private String note;
	private TransactionType transactionType;
	private Contract contract;

	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}

	public Integer getEvidenceNumber() {
		return evidenceNumber;
	}

	public void setEvidenceNumber(Integer evidenceNumber) {
		this.evidenceNumber = evidenceNumber;
	}

	public Date getDecisiveDate() {
		return decisiveDate;
	}

	public void setDecisiveDate(Date decisiveDate) {
		this.decisiveDate = decisiveDate;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public TransactionType getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(TransactionType transactionType) {
		this.transactionType = transactionType;
	}

	public Contract getContract() {
		return contract;
	}

	public void setContract(Contract contract) {
		this.contract = contract;
	}

	@Override
	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this);
		builder.append("order", order);
		builder.append("evidenceNumber", evidenceNumber);
		builder.append("decisiveDate", decisiveDate);
		builder.append("amount", amount);
		builder.append("title", title);
		builder.append("note", note);
		builder.append("transactionType", transactionType);
		builder.append("contract", contract);
		return builder.toString();
	}

}
