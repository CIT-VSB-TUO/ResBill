package cz.vsb.resbill.criteria;

import java.io.Serializable;

public class CustomerCriteria implements Serializable {

	private static final long serialVersionUID = 6066969039894242297L;

	private String namePrefix;

	private Integer contactPersonId;

	public String getNamePrefix() {
		return namePrefix;
	}

	public void setNamePrefix(String namePrefix) {
		this.namePrefix = namePrefix;
	}

	public Integer getContactPersonId() {
		return contactPersonId;
	}

	public void setContactPersonId(Integer contactPersonId) {
		this.contactPersonId = contactPersonId;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CustomerCriteria [");
		builder.append(super.toString());
		builder.append(", namePrefix=");
		builder.append(namePrefix);
		builder.append(", contactPersonId=");
		builder.append(contactPersonId);
		builder.append("]");
		return builder.toString();
	}

}
