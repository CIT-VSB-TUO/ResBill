/*
 * Copyright (c) 2015 CIT, VŠB-TU Ostrava
 * 
 */
package cz.vsb.resbill.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import cz.vsb.resbill.model.Transaction;
import cz.vsb.resbill.util.ToStringBuilder;

/**
 * @author Ing. Radek Liebzeit <radek.liebzeit@vsb.cz>
 *
 */
public class TransactionDTO implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID     = 1L;

  private Integer           transactionId        = null;

  private Integer           order                = null;

  private Integer           evidenceNumber       = null;

  private Date              decisiveDate         = null;

  private BigDecimal        amount               = null;

  private String            title                = null;

  private String            transactionTypeTitle = null;

  private String            contractName         = null;

  private String            customerName         = null;

  /**
   * 
   * @param transaction
   */
  public void fill(Transaction transaction) {
    transactionId = transaction.getId();
    order = transaction.getOrder();
    evidenceNumber = transaction.getEvidenceNumber();
    decisiveDate = transaction.getDecisiveDate();
    amount = transaction.getAmount();
    title = transaction.getTitle();
    transactionTypeTitle = transaction.getTransactionType().getTitle();
    contractName = transaction.getContract().getName();
    customerName = transaction.getContract().getCustomer().getName();
  }

  /**
   * @return the transactionId
   */
  public Integer getTransactionId() {
    return transactionId;
  }

  /**
   * @param transactionId
   *          the transactionId to set
   */
  public void setTransactionId(Integer transactionId) {
    this.transactionId = transactionId;
  }

  /**
   * @return the order
   */
  public Integer getOrder() {
    return order;
  }

  /**
   * @param order
   *          the order to set
   */
  public void setOrder(Integer order) {
    this.order = order;
  }

  /**
   * @return the evidenceNumber
   */
  public Integer getEvidenceNumber() {
    return evidenceNumber;
  }

  /**
   * @param evidenceNumber
   *          the evidenceNumber to set
   */
  public void setEvidenceNumber(Integer evidenceNumber) {
    this.evidenceNumber = evidenceNumber;
  }

  /**
   * @return the decisiveDate
   */
  public Date getDecisiveDate() {
    return decisiveDate;
  }

  /**
   * @param decisiveDate
   *          the decisiveDate to set
   */
  public void setDecisiveDate(Date decisiveDate) {
    this.decisiveDate = decisiveDate;
  }

  /**
   * @return the amount
   */
  public BigDecimal getAmount() {
    return amount;
  }

  /**
   * @param amount
   *          the amount to set
   */
  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

  /**
   * @return the contractName
   */
  public String getContractName() {
    return contractName;
  }

  /**
   * @param contractName
   *          the contractName to set
   */
  public void setContractName(String contractName) {
    this.contractName = contractName;
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
   * @return the title
   */
  public String getTitle() {
    return title;
  }

  /**
   * @param title
   *          the title to set
   */
  public void setTitle(String title) {
    this.title = title;
  }

  /**
   * @return the transactionTypeTitle
   */
  public String getTransactionTypeTitle() {
    return transactionTypeTitle;
  }

  /**
   * @param transactionTypeTitle
   *          the transactionTypeTitle to set
   */
  public void setTransactionTypeTitle(String transactionTypeTitle) {
    this.transactionTypeTitle = transactionTypeTitle;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    ToStringBuilder builder = new ToStringBuilder(this);
    builder.append("transactionId", transactionId);
    builder.append("order", order);
    builder.append("evidenceNumber", evidenceNumber);
    builder.append("decisiveDate", decisiveDate);
    builder.append("amount", amount);
    builder.append("title", title);
    builder.append("transactionTypeTitle", transactionTypeTitle);
    builder.append("contractName", contractName);
    builder.append("customerName", customerName);
    builder.append("toString()", super.toString());
    return builder.toString();
  }

}
