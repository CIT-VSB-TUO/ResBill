/*
 * Copyright (c) 2015 CIT, VŠB-TU Ostrava
 * 
 */
package cz.vsb.resbill.criteria;

import java.io.Serializable;

import cz.vsb.resbill.util.ToStringBuilder;

/**
 * @author Ing. Radek Liebzeit <radek.liebzeit@vsb.cz>
 *
 */
public class InvoiceTypeCriteria implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  /**
   * Vyfiltruje vsechny InvoiceType, pro ktere plati, zbytek po deleni zeroModuloDividend/divisor je 0
   */
  private Integer           zeroModuloDividend;

  private OrderBy           orderBy          = OrderBy.ID;

  private boolean           orderDesc        = false;

  /**
   * @return the zeroModuloDividend
   */
  public Integer getZeroModuloDividend() {
    return zeroModuloDividend;
  }

  /**
   * @param zeroModuloDividend
   *          the zeroModuloDividend to set
   */
  public void setZeroModuloDividend(Integer zeroModuloDividend) {
    this.zeroModuloDividend = zeroModuloDividend;
  }

  /**
   * @return the orderBy
   */
  public OrderBy getOrderBy() {
    return orderBy;
  }

  /**
   * @param orderBy
   *          the orderBy to set
   */
  public void setOrderBy(OrderBy orderBy) {
    this.orderBy = orderBy;
  }

  /**
   * @return the orderDesc
   */
  public boolean isOrderDesc() {
    return orderDesc;
  }

  /**
   * @param orderDesc
   *          the orderDesc to set
   */
  public void setOrderDesc(boolean orderDesc) {
    this.orderDesc = orderDesc;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    ToStringBuilder builder = new ToStringBuilder(this);
    builder.append("zeroModuloDividend", zeroModuloDividend);
    builder.append("orderBy", orderBy);
    builder.append("orderDesc", orderDesc);
    builder.append("toString()", super.toString());
    return builder.toString();
  }

  /**
   * 
   * @author Ing. Radek Liebzeit <radek.liebzeit@vsb.cz>
   *
   */
  public static enum OrderBy {
    ID,
  }
}
