/*
 * Copyright (c) 2015 CIT, VŠB-TU Ostrava
 * 
 */
package cz.vsb.resbill.dto;

import cz.vsb.resbill.model.Invoice;
import cz.vsb.resbill.util.ToStringBuilder;

/**
 * @author Ing. Radek Liebzeit <radek.liebzeit@vsb.cz>
 *
 */
public class InvoiceDetailDTO extends InvoiceDTO {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  private String            detail           = null;

  private String            summary          = null;

  /**
   * 
   */
  public InvoiceDetailDTO() {
    super();
  }

  /**
   * 
   * @param invoice
   */
  public InvoiceDetailDTO(Invoice invoice) {
    this();

    fill(invoice);
  }

  /**
   * 
   */
  @Override
  public void fill(Invoice invoice) {
    super.fill(invoice);

    detail = invoice.getDetail();
    summary = invoice.getSummary();
  }

  /**
   * @return the detail
   */
  public String getDetail() {
    return detail;
  }

  /**
   * @param detail
   *          the detail to set
   */
  public void setDetail(String detail) {
    this.detail = detail;
  }

  /**
   * @return the summary
   */
  public String getSummary() {
    return summary;
  }

  /**
   * @param summary
   *          the summary to set
   */
  public void setSummary(String summary) {
    this.summary = summary;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    ToStringBuilder builder = new ToStringBuilder(this);
    builder.append("detail", detail);
    builder.append("summary", summary);
    builder.append("toString()", super.toString());
    return builder.toString();
  }

}
