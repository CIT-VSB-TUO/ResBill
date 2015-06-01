/*
 * Copyright (c) 2015 CIT, VŠB-TU Ostrava
 * 
 */
package cz.vsb.resbill.criteria;

import java.io.Serializable;
import java.util.Date;

import cz.vsb.resbill.util.ToStringBuilder;

/**
 * Kriteria, ktera definuji parametry pro vytvoreni faktur.
 * 
 * @author Ing. Radek Liebzeit <radek.liebzeit@vsb.cz>
 *
 */
public class InvoiceCreateCriteria implements Serializable {

	/**
   * 
   */
	private static final long serialVersionUID = 1L;

	/**
	 * Fakturace ma byt spustena za tento mesic.
	 * 
	 * Objekt je tridy Date, ale zajima nas pouze mesic. Tj. nezalezi na konkretnim dni v tomto mesici.
	 */
	private Date month;

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
		builder.append("month", month);
		return builder.toString();
	}

}
