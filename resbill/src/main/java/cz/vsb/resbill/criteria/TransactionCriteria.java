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
public class TransactionCriteria implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  private List<OrderBy>     orderBy          = Arrays.asList(new OrderBy[] { OrderBy.DECISIVE_DATE_DESC });

  private Integer           contractId       = null;

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
    builder.append("orderBy", orderBy);
    builder.append("contractId", contractId);
    builder.append("toString()", super.toString());
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

  }
}
