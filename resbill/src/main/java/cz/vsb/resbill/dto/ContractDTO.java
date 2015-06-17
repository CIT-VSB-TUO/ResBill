/*
 * Copyright (c) 2015 CIT, VŠB-TU Ostrava
 * 
 */
package cz.vsb.resbill.dto;

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

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  private Integer           contractId       = null;

  private Long              evidenceNumber   = null;

  private String            name             = null;

  private BigDecimal        balance          = null;

  private Date              beginDate        = null;

  private Date              endDate          = null;

  private String            customerName     = null;

  /**
   * 
   */
  public ContractDTO() {
    super();
  }

  /**
   * 
   */
  public ContractDTO(Contract contract) {
    this();

    fill(contract);
  }

  /**
   * 
   * @param contract
   */
  public void fill(Contract contract) {
    contractId = contract.getId();
    evidenceNumber = contract.getEvidenceNumber();
    name = contract.getName();
    balance = contract.getBalance();
    beginDate = contract.getPeriod().getBeginDate();
    endDate = contract.getPeriod().getEndDate();

    customerName = contract.getCustomer().getName();
  }

  /**
   * @return the contractId
   */
  public Integer getContractId() {
    return contractId;
  }

  /**
   * @param contractId
   *          the contractId to set
   */
  public void setContractId(Integer contractId) {
    this.contractId = contractId;
  }

  /**
   * @return the evidenceNumber
   */
  public Long getEvidenceNumber() {
    return evidenceNumber;
  }

  /**
   * @param evidenceNumber
   *          the evidenceNumber to set
   */
  public void setEvidenceNumber(Long evidenceNumber) {
    this.evidenceNumber = evidenceNumber;
  }

  /**
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * @param name
   *          the name to set
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * @return the beginDate
   */
  public Date getBeginDate() {
    return beginDate;
  }

  /**
   * @param beginDate
   *          the beginDate to set
   */
  public void setBeginDate(Date beginDate) {
    this.beginDate = beginDate;
  }

  /**
   * @return the endDate
   */
  public Date getEndDate() {
    return endDate;
  }

  /**
   * @param endDate
   *          the endDate to set
   */
  public void setEndDate(Date endDate) {
    this.endDate = endDate;
  }

  /**
   * @return the customerName
   */
  public String getCustomerName() {
    return customerName;
  }

  /**
   * @param customerName
   *          the customerName to set
   */
  public void setCustomerName(String customerName) {
    this.customerName = customerName;
  }

  /**
   * @return the balance
   */
  public BigDecimal getBalance() {
    return balance;
  }

  /**
   * @param balance
   *          the balance to set
   */
  public void setBalance(BigDecimal balance) {
    this.balance = balance;
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
    builder.append("customerName", customerName);
    builder.append("toString()", super.toString());
    return builder.toString();
  }

}
