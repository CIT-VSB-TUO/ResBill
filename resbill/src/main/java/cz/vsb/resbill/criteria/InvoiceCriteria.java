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
public class InvoiceCriteria implements Serializable {

	/**
   * 
   */
	private static final long serialVersionUID = 1L;

	private OrderBy orderBy = OrderBy.DECISIVE_DATE;

	private boolean orderDesc = false;

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
		builder.append("orderBy", orderBy);
		builder.append("orderDesc", orderDesc);
		return builder.toString();
	}

	/**
	 * 
	 * @author Ing. Radek Liebzeit <radek.liebzeit@vsb.cz>
	 *
	 */
	public static enum OrderBy {
		DECISIVE_DATE,
	}
}
