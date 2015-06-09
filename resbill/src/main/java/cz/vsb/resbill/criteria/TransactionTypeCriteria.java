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
public class TransactionTypeCriteria implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  private List<OrderBy>     orderBy          = Arrays.asList(new OrderBy[] { OrderBy.ID_ASC });

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
    builder.append("toString()", super.toString());
    return builder.toString();
  }

  /**
   * 
   * @author Ing. Radek Liebzeit <radek.liebzeit@vsb.cz>
   *
   */
  public static enum OrderBy {
    ID_ASC,

    ID_DESC,

    TITLE_ASC,

    TITLE_DESC,

  }
}
