package cz.vsb.resbill.criteria;

import java.io.Serializable;

public class TariffCriteria implements Serializable {

	private static final long serialVersionUID = 2384073660740137541L;

	public static enum OrderBy {
		NAME, VALIDITY
	}

	private String namePrefix;

	private Boolean valid;

	private OrderBy orderBy;

	public String getNamePrefix() {
		return namePrefix;
	}

	public void setNamePrefix(String namePrefix) {
		this.namePrefix = namePrefix;
	}

	public Boolean getValid() {
		return valid;
	}

	public void setValid(Boolean valid) {
		this.valid = valid;
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
		builder.append("TariffCriteria [");
		builder.append(super.toString());
		builder.append(", namePrefix=");
		builder.append(namePrefix);
		builder.append(", valid=");
		builder.append(valid);
		builder.append(", orderBy=");
		builder.append(orderBy);
		builder.append("]");
		return builder.toString();
	}

}
