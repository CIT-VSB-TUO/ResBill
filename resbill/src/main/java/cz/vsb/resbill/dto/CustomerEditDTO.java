package cz.vsb.resbill.dto;

import java.io.Serializable;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import cz.vsb.resbill.model.Customer;

public class CustomerEditDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@Valid
	@NotNull
	private Customer customer;

	@NotNull
	private Integer contactPersonId;

	public CustomerEditDTO(Customer customer) {
		this.customer = customer;
		if (customer != null) {
			this.contactPersonId = customer.getContactPerson() != null ? customer.getContactPerson().getId() : null;
		}
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
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
		builder.append("CustomerEditDTO [");
		builder.append(super.toString());
		builder.append(", customer=");
		builder.append(customer);
		builder.append(", contactPersonId=");
		builder.append(contactPersonId);
		builder.append("]");
		return builder.toString();
	}

}
