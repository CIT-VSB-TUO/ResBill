/*
 * Copyright (c) 2015 CIT, VŠB-TU Ostrava
 * 
 */
package cz.vsb.resbill.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import cz.vsb.resbill.util.ToStringBuilder;

/**
 * @author Ing. Radek Liebzeit <radek.liebzeit@vsb.cz>
 *
 */
@Entity
@Table(name = "INVOICE_DAILY_USAGE", uniqueConstraints = @UniqueConstraint(name = "UK_invoice_daily_usage__invoice_id__daily_usage_id", columnNames = { "invoice_id", "daily_usage_id" }))
public class InvoiceDailyUsage extends BaseVersionedEntity {

	private static final long serialVersionUID = 1L;

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "invoice_id", nullable = false, foreignKey = @ForeignKey(name = "FK_invoice_daily_usage__invoice"))
	private Invoice invoice;

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "daily_usage_id", nullable = false, foreignKey = @ForeignKey(name = "FK_invoice_daily_usage__daily_usage"))
	private DailyUsage dailyUsage;

	/**
	 * @return the invoice
	 */
	public Invoice getInvoice() {
		return invoice;
	}

	/**
	 * @param invoice
	 *          the invoice to set
	 */
	public void setInvoice(Invoice invoice) {
		this.invoice = invoice;
	}

	/**
	 * @return the dailyUsage
	 */
	public DailyUsage getDailyUsage() {
		return dailyUsage;
	}

	/**
	 * @param dailyUsage
	 *          the dailyUsage to set
	 */
	public void setDailyUsage(DailyUsage dailyUsage) {
		this.dailyUsage = dailyUsage;
	}

	/**
   * 
   */
	@Override
	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this);
		builder.append("invoice.id", invoice != null ? invoice.getId() : null);
		builder.append("dailyUsage.id", dailyUsage != null ? dailyUsage.getId() : null);
		return builder.toString();
	}

}
