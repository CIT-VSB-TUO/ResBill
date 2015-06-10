/*
 * Copyright (c) 2015 CIT, VŠB-TU Ostrava
 * 
 */
package cz.vsb.resbill.dto;

import java.io.Serializable;

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

  /**
   * 
   * @param contract
   */
  public void fill(Contract contract) {
    contractId = contract.getId();
    evidenceNumber = contract.getEvidenceNumber();
    name = contract.getName();
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
    builder.append("toString()", super.toString());
    return builder.toString();
  }

}
