/*
 * Copyright (c) 2015 CIT, VŠB-TU Ostrava
 * 
 */
package cz.vsb.resbill.dto;

import cz.vsb.resbill.model.Invoice;
import cz.vsb.resbill.model.Period;
import cz.vsb.resbill.util.ToStringBuilder;

/**
 * @author Ing. Radek Liebzeit <radek.liebzeit@vsb.cz>
 *
 */
public class InvoiceDTO extends TransactionDTO {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  private boolean           noPriceList;

  private Period            period;

  /**
   * 
   */
  public InvoiceDTO() {
    super();
  }

  /**
   * 
   * @param invoice
   */
  public InvoiceDTO(Invoice invoice) {
    this();

    fill(invoice);
  }

  /**
   * 
   * @param invoice
   */
  public void fill(Invoice invoice) {
    super.fill(invoice);

    noPriceList = invoice.getNoPriceList();
    period = invoice.getPeriod();
  }

  /**
   * @return the noPriceList
   */
  public boolean isNoPriceList() {
    return noPriceList;
  }

  /**
   * @param noPriceList
   *          the noPriceList to set
   */
  public void setNoPriceList(boolean noPriceList) {
    this.noPriceList = noPriceList;
  }

  /**
   * @return the period
   */
  public Period getPeriod() {
    return period;
  }

  /**
   * @param period
   *          the period to set
   */
  public void setPeriod(Period period) {
    this.period = period;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    ToStringBuilder builder = new ToStringBuilder(this);
    builder.append("noPriceList", noPriceList);
    builder.append("period", period);
    builder.append("toString()", super.toString());
    return builder.toString();
  }

}
