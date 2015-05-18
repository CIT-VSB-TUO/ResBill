package cz.vsb.resbill.criteria;

import java.io.Serializable;

public class CustomerCriteria implements Serializable {

	private static final long serialVersionUID = 6066969039894242297L;

	private String namePrefix;

	public String getNamePrefix() {
		return namePrefix;
	}

	public void setNamePrefix(String namePrefix) {
		this.namePrefix = namePrefix;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CustomerCriteria [");
		builder.append(super.toString());
		builder.append(", namePrefix=");
		builder.append(namePrefix);
		builder.append("]");
		return builder.toString();
	}

}
