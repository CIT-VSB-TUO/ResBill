package cz.vsb.resbill.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

@Entity
@DiscriminatorValue(value = "inv")
public class Invoice extends Transaction {

	private static final long serialVersionUID = -3016705349881450838L;

	// kvuli dedicnosti povoluje mapovani hodnotu null
	@Embedded
	@AttributeOverride(name = "beginDate", column = @Column(name = "begin_date", nullable = true))
	@NotNull
	private Period period;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "invoice_type_id", foreignKey = @ForeignKey(name = "FK_invoice__invoice_type"))
	@NotNull
	private InvoiceType invoiceType;

	// kvuli dedicnosti povoluje mapovani hodnotu null
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "attachment_id", foreignKey = @ForeignKey(name = "FK_invoice__attachment"))
	@NotNull
	private File attachment;

	public Period getPeriod() {
		return period;
	}

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

	/**
	 * 
	 */
	@OneToMany(mappedBy = "invoice", fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE }, orphanRemoval = false)
	private Set<DailyUsage> dailyUsages = new HashSet<>();

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
		builder.append("]");
		return builder.toString();
	}
}
