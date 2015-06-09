/*
 * Copyright (c) 2015 CIT, VŠB-TU Ostrava
 * 
 */
package cz.vsb.resbill.dto;

import java.io.Serializable;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import cz.vsb.resbill.model.Transaction;
import cz.vsb.resbill.util.ToStringBuilder;

/**
 * @author Ing. Radek Liebzeit <radek.liebzeit@vsb.cz>
 *
 */
public class TransactionEditDTO implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID  = 1L;

  @Valid
  @NotNull
  private Transaction       transaction       = null;

  @NotNull
  private Integer           transactionTypeId = null;

  @NotNull
  private Integer           contractId        = null;

  /**
   * 
   * @return
   */
  public boolean isEditDisabled() {
    return transaction != null && transaction.getId() != null;
  }

  /**
   * 
   * @param transaction
   */
  public TransactionEditDTO(Transaction transaction) {
    this.transaction = transaction;
    if (transaction != null) {
      this.transactionTypeId = transaction.getTransactionType() != null ? transaction.getTransactionType().getId() : null;
      this.contractId = transaction.getContract() != null ? transaction.getContract().getId() : null;
    }
  }

  /**
   * @return the transaction
   */
  public Transaction getTransaction() {
    return transaction;
  }

  /**
   * @param transaction
   *          the transaction to set
   */
  public void setTransaction(Transaction transaction) {
    this.transaction = transaction;
  }

  /**
   * @return the transactionTypeId
   */
  public Integer getTransactionTypeId() {
    return transactionTypeId;
  }

  /**
   * @param transactionTypeId
   *          the transactionTypeId to set
   */
  public void setTransactionTypeId(Integer transactionTypeId) {
    this.transactionTypeId = transactionTypeId;
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

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    ToStringBuilder builder = new ToStringBuilder(this);
    builder.append("transaction", transaction);
    builder.append("transactionTypeId", transactionTypeId);
    builder.append("contractId", contractId);
    builder.append("toString()", super.toString());
    return builder.toString();
  }

}
