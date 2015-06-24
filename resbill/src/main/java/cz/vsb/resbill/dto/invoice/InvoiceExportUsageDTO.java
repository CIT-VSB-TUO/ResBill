/*
 * Copyright (c) 2015 CIT, VŠB-TU Ostrava
 * 
 */
package cz.vsb.resbill.dto.invoice;

import java.io.Serializable;
import java.util.Date;

import cz.vsb.resbill.util.ToStringBuilder;

/**
 * @author Ing. Radek Liebzeit <radek.liebzeit@vsb.cz>
 *
 */
public class InvoiceExportUsageDTO implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  private Date              beginDate        = null;

  private Date              endDate          = null;

  private Long              pieces           = null;

  private Float             unitPrice        = null;

  private Float             amount           = null;

  private Float             price            = null;

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
   * @return the amount
   */
  public Float getAmount() {
    return amount;
  }

  /**
   * @param amount
   *          the amount to set
   */
  public void setAmount(Float amount) {
    this.amount = amount;
  }

  /**
   * @return the price
   */
  public Float getPrice() {
    return price;
  }

  /**
   * @param price
   *          the price to set
   */
  public void setPrice(Float price) {
    this.price = price;
  }

  /**
   * @return the pieces
   */
  public Long getPieces() {
    return pieces;
  }

  /**
   * @param pieces
   *          the pieces to set
   */
  public void setPieces(Long pieces) {
    this.pieces = pieces;
  }

  /**
   * @return the unitPrice
   */
  public Float getUnitPrice() {
    return unitPrice;
  }

  /**
   * @param unitPrice
   *          the unitPrice to set
   */
  public void setUnitPrice(Float unitPrice) {
    this.unitPrice = unitPrice;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    ToStringBuilder builder = new ToStringBuilder(this);
    builder.append("beginDate", beginDate);
    builder.append("endDate", endDate);
    builder.append("pieces", pieces);
    builder.append("unitPrice", unitPrice);
    builder.append("amount", amount);
    builder.append("price", price);
    builder.append("toString()", super.toString());
    return builder.toString();
  }

}
