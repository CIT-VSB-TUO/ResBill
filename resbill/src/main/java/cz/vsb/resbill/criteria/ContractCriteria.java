package cz.vsb.resbill.criteria;

import java.io.Serializable;

import cz.vsb.resbill.model.Contract;

/**
 * A criteria class for searching {@link Contract} entities.
 * 
 * @author HAL191
 *
 */
public class ContractCriteria implements Serializable {

	private static final long serialVersionUID = 3463991795938726531L;

	public static enum OrderBy {
		EVIDENCE_NUMBER, NAME
	}

	private Integer customerId;

	private OrderBy orderBy;

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public OrderBy getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(OrderBy orderBy) {
		this.orderBy = orderBy;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ContractCriteria [");
		builder.append(super.toString());
		builder.append(", customerId=");
		builder.append(customerId);
		builder.append(", orderBy=");
		builder.append(orderBy);
		builder.append("]");
		return builder.toString();
	}

}
