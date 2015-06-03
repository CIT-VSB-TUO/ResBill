/*
 * Copyright (c) 2015 CIT, VŠB-TU Ostrava
 * 
 */
package cz.vsb.resbill.dto;

import java.io.Serializable;
import java.util.Date;

import cz.vsb.resbill.util.ToStringBuilder;

/**
 * @author Ing. Radek Liebzeit <radek.liebzeit@vsb.cz>
 *
 */
public class InvoiceExportResultDTO implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID    = 1L;

  /**
   * Cas spusteni exportu
   */
  private Date              beginTimestamp      = null;

  /**
   * Cas ukonceni exportu
   */
  private Date              endTimestamp        = null;

  private int               invoicesNumberAll   = 0;

  private int               invoicesNumberOk    = 0;

  private int               invoicesNumberError = 0;

  /**
   * @return the beginTimestamp
   */
  public Date getBeginTimestamp() {
    return beginTimestamp;
  }

  /**
   * @param beginTimestamp
   *          the beginTimestamp to set
   */
  public void setBeginTimestamp(Date beginTimestamp) {
    this.beginTimestamp = beginTimestamp;
  }

  /**
   * @return the endTimestamp
   */
  public Date getEndTimestamp() {
    return endTimestamp;
  }

  /**
   * @param endTimestamp
   *          the endTimestamp to set
   */
  public void setEndTimestamp(Date endTimestamp) {
    this.endTimestamp = endTimestamp;
  }

  /**
   * @return the invoicesNumberAll
   */
  public int getInvoicesNumberAll() {
    return invoicesNumberAll;
  }

  /**
   * @param invoicesNumberAll
   *          the invoicesNumberAll to set
   */
  public void setInvoicesNumberAll(int invoicesNumberAll) {
    this.invoicesNumberAll = invoicesNumberAll;
  }

  /**
   * @return the invoicesNumberOk
   */
  public int getInvoicesNumberOk() {
    return invoicesNumberOk;
  }

  /**
   * @param invoicesNumberOk
   *          the invoicesNumberOk to set
   */
  public void setInvoicesNumberOk(int invoicesNumberOk) {
    this.invoicesNumberOk = invoicesNumberOk;
  }

  /**
   * @return the invoicesNumberError
   */
  public int getInvoicesNumberError() {
    return invoicesNumberError;
  }

  /**
   * @param invoicesNumberError
   *          the invoicesNumberError to set
   */
  public void setInvoicesNumberError(int invoicesNumberError) {
    this.invoicesNumberError = invoicesNumberError;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    ToStringBuilder builder = new ToStringBuilder(this);
    builder.append("beginTimestamp", beginTimestamp);
    builder.append("endTimestamp", endTimestamp);
    builder.append("invoicesNumberAll", invoicesNumberAll);
    builder.append("invoicesNumberOk", invoicesNumberOk);
    builder.append("invoicesNumberError", invoicesNumberError);
    builder.append("toString()", super.toString());
    return builder.toString();
  }

}
