/*
 * Copyright (c) 2015 CIT, VŠB-TU Ostrava
 * 
 */
package cz.vsb.resbill.dto.contract;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import cz.vsb.resbill.model.Contract;
import cz.vsb.resbill.util.ToStringBuilder;

/**
 * @author Ing. Radek Liebzeit <radek.liebzeit@vsb.cz>
 *
 */
public class ContractDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer contractId = null;

	private Long evidenceNumber = null;

	private String name = null;

	private BigDecimal balance = null;

	private Date beginDate = null;

	private Date endDate = null;

	public ContractDTO(Contract contract) {
		fill(contract);
	}

	/**
	 * 
	 * @param contract
	 */
	protected void fill(Contract contract) {
		contractId = contract.getId();
		evidenceNumber = contract.getEvidenceNumber();
		name = contract.getName();
		balance = contract.getBalance();
		beginDate = contract.getPeriod().getBeginDate();
		endDate = contract.getPeriod().getEndDate();
	}

	/**
	 * @return the contractId
	 */
	public Integer getContractId() {
		return contractId;
	}

	/**
	 * @return the evidenceNumber
	 */
	public Long getEvidenceNumber() {
		return evidenceNumber;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the beginDate
	 */
	public Date getBeginDate() {
		return beginDate;
	}

	/**
	 * @return the endDate
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * @return the balance
	 */
	public BigDecimal getBalance() {
		return balance;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this);
		builder.append("contractId", contractId);
		builder.append("evidenceNumber", evidenceNumber);
		builder.append("name", name);
		builder.append("balance", balance);
		builder.append("beginDate", beginDate);
		builder.append("endDate", endDate);
		builder.append("toString()", super.toString());
		return builder.toString();
	}

}
