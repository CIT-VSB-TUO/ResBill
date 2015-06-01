package cz.vsb.resbill.criteria;

import java.io.Serializable;

import cz.vsb.resbill.model.InvoicePriceList;

/**
 * A criteria class for searching {@link InvoicePriceList} entities.
 * 
 * @author HAL191
 *
 */
public class InvoicePriceListCriteria implements Serializable {

	private static final long serialVersionUID = -930208647475856196L;

	private Integer invoiceId;

	private Integer priceListId;

	public Integer getInvoiceId() {
		return invoiceId;
	}

	public void setInvoiceId(Integer invoiceId) {
		this.invoiceId = invoiceId;
	}

	public Integer getPriceListId() {
		return priceListId;
	}

	public void setPriceListId(Integer priceListId) {
		this.priceListId = priceListId;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("InvoicePriceListCriteria [");
		builder.append(super.toString());
		builder.append(", invoiceId=");
		builder.append(invoiceId);
		builder.append(", priceListId=");
		builder.append(priceListId);
		builder.append("]");
		return builder.toString();
	}

}
