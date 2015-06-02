package cz.vsb.resbill.criteria;

import java.io.Serializable;

public class PriceListCriteria implements Serializable {

	private static final long serialVersionUID = 6310350455943495145L;

	public static enum OrderBy {
		PERIOD
	}

	private Integer tariffId;

	private Boolean lastValid;

	private OrderBy orderBy;

	public Integer getTariffId() {
		return tariffId;
	}

	public void setTariffId(Integer tariffId) {
		this.tariffId = tariffId;
	}

	public Boolean getLastValid() {
		return lastValid;
	}

	public void setLastValid(Boolean lastValid) {
		this.lastValid = lastValid;
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
		builder.append(", lastValid=");
		builder.append(lastValid);
		builder.append(", orderBy=");
		builder.append("]");
		return builder.toString();
	}

}
