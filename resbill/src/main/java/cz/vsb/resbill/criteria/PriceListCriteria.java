package cz.vsb.resbill.criteria;

import java.io.Serializable;

public class PriceListCriteria implements Serializable {

	private static final long serialVersionUID = 6310350455943495145L;

	public static enum OrderBy {
		DATE, VALIDITY
	}

	private Integer tariffId;

	private Boolean currentlyValid;

	public Integer getTariffId() {
		return tariffId;
	}

	public void setTariffId(Integer tariffId) {
		this.tariffId = tariffId;
	}

	public Boolean getCurrentlyValid() {
		return currentlyValid;
	}

	public void setCurrentlyValid(Boolean currentlyValid) {
		this.currentlyValid = currentlyValid;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PriceListCriteria [");
		builder.append(super.toString());
		builder.append(", tariffId=");
		builder.append(tariffId);
		builder.append(", currentlyValid=");
		builder.append(currentlyValid);
		builder.append(", orderBy=");
		builder.append("]");
		return builder.toString();
	}

}
