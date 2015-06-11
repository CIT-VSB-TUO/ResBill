package cz.vsb.resbill.criteria;

import java.io.Serializable;

import cz.vsb.resbill.model.ContractInvoiceType;

/**
 * A criteria class for searching {@link ContractInvoiceType} entities.
 * 
 * @author HAL191
 *
 */
public class ContractInvoiceTypeCriteria implements Serializable {

	private static final long serialVersionUID = 3752663052905075572L;

	public static enum OrderBy {
		PERIOD_ASC, PERIOD_DESC
	}

	private Integer contractId;

	private Integer invoiceTypeId;

	private Boolean first;

	private Boolean last;

	private OrderBy orderBy;

	private boolean fetchContract;

	private boolean fetchInvoiceType;

	public Integer getContractId() {
		return contractId;
	}

	public void setContractId(Integer contractId) {
		this.contractId = contractId;
	}

	public Integer getInvoiceTypeId() {
		return invoiceTypeId;
	}

	public void setInvoiceTypeId(Integer invoiceTypeId) {
		this.invoiceTypeId = invoiceTypeId;
	}

	public Boolean getFirst() {
		return first;
	}

	public void setFirst(Boolean first) {
		this.first = first;
	}

	public Boolean getLast() {
		return last;
	}

	public void setLast(Boolean last) {
		this.last = last;
	}

	public OrderBy getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(OrderBy orderBy) {
		this.orderBy = orderBy;
	}

	public boolean isFetchContract() {
		return fetchContract;
	}

	public void setFetchContract(boolean fetchContract) {
		this.fetchContract = fetchContract;
	}

	public boolean isFetchInvoiceType() {
		return fetchInvoiceType;
	}

	public void setFetchInvoiceType(boolean fetchInvoiceType) {
		this.fetchInvoiceType = fetchInvoiceType;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ContractInvoiceTypeCriteria [");
		builder.append(super.toString());
		builder.append(", contractId=");
		builder.append(contractId);
		builder.append(", invoiceTypeId=");
		builder.append(invoiceTypeId);
		builder.append(", first=");
		builder.append(first);
		builder.append(", last=");
		builder.append(last);
		builder.append(", orderBy=");
		builder.append(orderBy);
		builder.append(", fetchContract=");
		builder.append(fetchContract);
		builder.append(", fetchInvoiceType=");
		builder.append(fetchInvoiceType);
		builder.append("]");
		return builder.toString();
	}

}
