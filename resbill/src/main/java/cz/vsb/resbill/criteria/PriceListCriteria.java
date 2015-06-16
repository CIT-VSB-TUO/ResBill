package cz.vsb.resbill.criteria;

import java.io.Serializable;

public class PriceListCriteria implements Serializable {

	private static final long serialVersionUID = 6310350455943495145L;

	public static enum OrderBy {
		PERIOD
	}

	private Integer tariffId;

	private Boolean first;

	private Boolean last;

	private OrderBy orderBy;

	public Integer getTariffId() {
		return tariffId;
	}

	public void setTariffId(Integer tariffId) {
		this.tariffId = tariffId;
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

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PriceListCriteria [");
		builder.append(super.toString());
		builder.append(", tariffId=");
		builder.append(tariffId);
		builder.append(", first=");
		builder.append(first);
		builder.append(", last=");
		builder.append(last);
		builder.append(", orderBy=");
		builder.append("]");
		return builder.toString();
	}

}
