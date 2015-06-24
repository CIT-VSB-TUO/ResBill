package cz.vsb.resbill.dto.customer;

import java.io.Serializable;

import cz.vsb.resbill.model.Customer;

public class CustomerDTO implements Serializable {

	private static final long serialVersionUID = -4649296501189503996L;

	private Integer customerId;

	private String name;

	public CustomerDTO(Customer customer) {
		fill(customer);
	}

	protected void fill(Customer customer) {
		if (customer != null) {
			this.customerId = customer.getId();
			this.name = customer.getName();
		}
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CustomerDTO [");
		builder.append(super.toString());
		builder.append(", customerId=");
		builder.append(customerId);
		builder.append(", name=");
		builder.append(name);
		builder.append("]");
		return builder.toString();
	}

}
