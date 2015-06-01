package cz.vsb.resbill.criteria;

import java.io.Serializable;

public class PriceListCriteria implements Serializable {

	private static final long serialVersionUID = 6310350455943495145L;

	public static enum OrderBy {
		DATE, VALIDITY
	}

	private Integer tariffId;

	private Boolean lastValid;

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
