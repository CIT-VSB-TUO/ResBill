package cz.vsb.resbill.criteria;

import java.io.Serializable;

import cz.vsb.resbill.model.Person;

/**
 * A criteria class for searching {@link Person} entities.
 * 
 * @author HAL191
 *
 */
public class PersonCriteria implements Serializable {

	private static final long serialVersionUID = 3962367746981378568L;

	public static enum OrderBy {
		EMAIL, NAME
	}

	private String email;

	private String firstName;

	private String secondName;

	private OrderBy orderBy = OrderBy.EMAIL;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getSecondName() {
		return secondName;
	}

	public void setSecondName(String secondName) {
		this.secondName = secondName;
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
		builder.append("PersonCriteria [");
		builder.append(super.toString());
		builder.append(", email=");
		builder.append(email);
		builder.append(", firstName=");
		builder.append(firstName);
		builder.append(", secondName=");
		builder.append(secondName);
		builder.append(", orderBy=");
		builder.append(orderBy);
		builder.append("]");
		return builder.toString();
	}

}
