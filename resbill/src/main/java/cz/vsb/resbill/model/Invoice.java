package cz.vsb.resbill.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

@Entity
@DiscriminatorValue(value = "inv")
public class Invoice extends Transaction implements PeriodLimitedEntity {

	private static final long serialVersionUID = -3016705349881450838L;

	// kvuli dedicnosti povoluje mapovani hodnotu null
	@Embedded
	@AttributeOverride(name = "beginDate", column = @Column(name = "begin_date", nullable = true))
	@NotNull
	private Period period;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "invoice_type_id", nullable = false, foreignKey = @ForeignKey(name = "FK_invoice__invoice_type"))
	private InvoiceType invoiceType;

	// kvuli dedicnosti povoluje mapovani hodnotu null
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "attachment_id", nullable = false, foreignKey = @ForeignKey(name = "FK_invoice__attachment"))
	private File attachment;

	@OneToMany(mappedBy = "invoice", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<InvoiceDailyUsage> invoiceDailyUsages = new HashSet<>();

	@OneToMany(mappedBy = "invoice", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<InvoicePriceList> invoicePriceLists = new HashSet<>();

	@Column(name = "no_price_list")
	@NotNull
	private Boolean noPriceList = Boolean.FALSE;

	@Column(name = "summary")
	@Lob
	@Basic(fetch = FetchType.LAZY)
	private String summary;

	@Column(name = "detail")
	@Lob
	@Basic(fetch = FetchType.LAZY)
	private String detail;

	@Override
	public Period getPeriod() {
		return period;
	}

	@Override
	public void setPeriod(Period period) {
		this.period = period;
	}

	public File getAttachment() {
		return attachment;
	}

	public void setAttachment(File attachment) {
		this.attachment = attachment;
	}

	public InvoiceType getInvoiceType() {
		return invoiceType;
	}

	public void setInvoiceType(InvoiceType invoiceType) {
		this.invoiceType = invoiceType;
	}

	public Set<InvoiceDailyUsage> getInvoiceDailyUsages() {
		return invoiceDailyUsages;
	}

	public void setInvoiceDailyUsages(Set<InvoiceDailyUsage> invoiceDailyUsages) {
		this.invoiceDailyUsages = invoiceDailyUsages;
	}

	public Set<InvoicePriceList> getInvoicePriceLists() {
		return invoicePriceLists;
	}

	public void setInvoicePriceLists(Set<InvoicePriceList> invoicePriceLists) {
		this.invoicePriceLists = invoicePriceLists;
	}

	/**
	 * @return the noPriceList
	 */
	public Boolean getNoPriceList() {
		return noPriceList;
	}

	/**
	 * @param noPriceList
	 *          the noPriceList to set
	 */
	public void setNoPriceList(Boolean noPriceList) {
		this.noPriceList = noPriceList;
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

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Invoice [");
		builder.append(super.toString());
		builder.append(", period=");
		builder.append(period);
		builder.append(", attachment.id=");
		builder.append(attachment != null ? attachment.getId() : null);
		builder.append(", invoiceType.id=");
		builder.append(invoiceType != null ? invoiceType.getId() : null);
		builder.append(", noPriceList=");
		builder.append(noPriceList);
		builder.append("]");
		return builder.toString();
	}
}
