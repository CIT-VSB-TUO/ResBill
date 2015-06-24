package cz.vsb.resbill.dto.customer;

import cz.vsb.resbill.dto.person.PersonDTO;
import cz.vsb.resbill.model.Customer;

public class CustomerListDTO extends CustomerDTO {

	private static final long serialVersionUID = 5114413748791276885L;

	private PersonDTO contactPerson;

	public CustomerListDTO(Customer customer) {
		super(customer);
	}

	@Override
	protected void fill(Customer customer) {
		super.fill(customer);
		if (customer != null) {
			if (customer.getContactPerson() != null) {
				contactPerson = new PersonDTO(customer.getContactPerson());
			}
		}
	}

	public PersonDTO getContactPerson() {
		return contactPerson;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CustomerListDTO [");
		builder.append(super.toString());
		builder.append(", contactPerson=");
		builder.append(contactPerson);
		builder.append("]");
		return builder.toString();
	}

}
