/*
 * Copyright (c) 2015 CIT, VŠB-TU Ostrava
 * 
 */
package cz.vsb.resbill.criteria;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import cz.vsb.resbill.util.ToStringBuilder;

/**
 * @author Ing. Radek Liebzeit <radek.liebzeit@vsb.cz>
 *
 */
public class InvoiceCriteria implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  private List<OrderBy>     orderBy          = Arrays.asList(new OrderBy[] { OrderBy.DECISIVE_DATE_DESC });

  /**
   * 
   * @return
   */
  public boolean needsContract() {
    return needsCustomer() || orderBy.contains(OrderBy.CONTRACT_NAME_ASC) || orderBy.contains(OrderBy.CONTRACT_NAME_DESC);
  }

  /**
   * 
   * @return
   */
  public boolean needsCustomer() {
    return orderBy.contains(OrderBy.CUSTOMER_NAME_ASC) || orderBy.contains(OrderBy.CUSTOMER_NAME_DESC);
  }

  /**
   * @return the orderBy
   */
  public List<OrderBy> getOrderBy() {
    return orderBy;
  }

  /**
   * @param orderBy
   *          the orderBy to set
   */
  public void setOrderBy(List<OrderBy> orderBy) {
    this.orderBy = orderBy;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    ToStringBuilder builder = new ToStringBuilder(this);
    builder.append("orderBy", orderBy);
    return builder.toString();
  }

  /**
   * 
   * @author Ing. Radek Liebzeit <radek.liebzeit@vsb.cz>
   *
   */
  public static enum OrderBy {
    ORDER_ASC,

    ORDER_DESC,

    DECISIVE_DATE_ASC,

    DECISIVE_DATE_DESC,

    CONTRACT_NAME_ASC,

    CONTRACT_NAME_DESC,

    CUSTOMER_NAME_ASC,

    CUSTOMER_NAME_DESC,
  }
}
