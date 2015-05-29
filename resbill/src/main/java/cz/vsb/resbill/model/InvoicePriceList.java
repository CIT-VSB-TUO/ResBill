package cz.vsb.resbill.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "INVOICE_PRICE_LIST", uniqueConstraints = @UniqueConstraint(name = "UK_invoice_price_list__invoice_id__price_list_id", columnNames = { "invoice_id", "price_list_id" }))
public class InvoicePriceList extends BaseVersionedEntity {

	private static final long serialVersionUID = 755842556304745569L;

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "invoice_id", foreignKey = @ForeignKey(name = "FK_invoice_price_list__invoice"))
	@NotNull
	private Invoice invoice;

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "price_list_id", foreignKey = @ForeignKey(name = "FK_invoice_price_list__price_list"))
	@NotNull
	private PriceList priceList;

	public Invoice getInvoice() {
		return invoice;
	}

	public void setInvoice(Invoice invoice) {
		this.invoice = invoice;
	}

	public PriceList getPriceList() {
		return priceList;
	}

	public void setPriceList(PriceList priceList) {
		this.priceList = priceList;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("InvoicePriceList [");
		builder.append(super.toString());
		builder.append(", invoice.id=");
		builder.append(invoice != null ? invoice.getId() : null);
		builder.append(", priceList.id=");
		builder.append(priceList != null ? priceList.getId() : null);
		builder.append("]");
		return builder.toString();
	}

}
