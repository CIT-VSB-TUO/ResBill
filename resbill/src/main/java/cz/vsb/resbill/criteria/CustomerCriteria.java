package cz.vsb.resbill.criteria;

import java.io.Serializable;

public class CustomerCriteria implements Serializable {

	private static final long serialVersionUID = 6066969039894242297L;

	public static enum OrderBy {
		NAME
	}

	private String namePrefix;

	private String name;

	private Integer contactPersonId;

	private boolean fetchContactPerson;

	private OrderBy orderBy;

	public String getNamePrefix() {
		return namePrefix;
	}

	public void setNamePrefix(String namePrefix) {
		this.namePrefix = namePrefix;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getContactPersonId() {
		return contactPersonId;
	}

	public void setContactPersonId(Integer contactPersonId) {
		this.contactPersonId = contactPersonId;
	}

	public boolean isFetchContactPerson() {
		return fetchContactPerson;
	}

	public void setFetchContactPerson(boolean fetchContactPerson) {
		this.fetchContactPerson = fetchContactPerson;
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
		builder.append("CustomerCriteria [");
		builder.append(super.toString());
		builder.append(", namePrefix=");
		builder.append(namePrefix);
		builder.append(", name=");
		builder.append(name);
		builder.append(", contactPersonId=");
		builder.append(contactPersonId);
		builder.append(", fetchContactPerson=");
		builder.append(fetchContactPerson);
		builder.append(", orderBy=");
		builder.append(orderBy);
		builder.append("]");
		return builder.toString();
	}

}
