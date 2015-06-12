/*
 * Copyright (c) 2015 CIT, VŠB-TU Ostrava
 * 
 */
package cz.vsb.resbill.criteria;

import java.io.Serializable;
import java.util.Date;

import cz.vsb.resbill.util.ToStringBuilder;

/**
 * @author Ing. Radek Liebzeit <radek.liebzeit@vsb.cz>
 *
 */
public class InvoiceExportCriteria implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  private Integer           contractId       = null;

  /**
   * Export faktur ma byt proveden za tento mesic.
   * 
   * Objekt je tridy Date, ale zajima nas pouze mesic. Tj. nezalezi na konkretnim dni v tomto mesici.
   */
  private Date              month;

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
   * @return the month
   */
  public Date getMonth() {
    return month;
  }

  /**
   * @param month
   *          the month to set
   */
  public void setMonth(Date month) {
    this.month = month;
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
    builder.append("month", month);
    builder.append("toString()", super.toString());
    return builder.toString();
  }

}
