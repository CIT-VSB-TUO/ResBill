package cz.vsb.resbill.dto.customer;

import cz.vsb.resbill.model.Customer;

public class CustomerHeaderDTO extends CustomerDTO {

	private static final long serialVersionUID = 3901265276904303899L;

	public CustomerHeaderDTO(Customer customer) {
		super(customer);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CustomerHeaderDTO [");
		builder.append(super.toString());
		builder.append(", ]");
		return builder.toString();
	}

}
