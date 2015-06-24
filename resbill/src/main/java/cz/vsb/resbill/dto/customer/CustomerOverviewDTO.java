package cz.vsb.resbill.dto.customer;

import cz.vsb.resbill.model.Customer;

public class CustomerOverviewDTO extends CustomerListDTO {

	private static final long serialVersionUID = 729046709667145940L;

	private String note;

	private String billingNote;

	public CustomerOverviewDTO(Customer customer) {
		super(customer);
	}

	@Override
	protected void fill(Customer customer) {
		super.fill(customer);
		if (customer != null) {
			this.note = customer.getNote();
			this.billingNote = customer.getBillingNote();
		}
	}

	public String getNote() {
		return note;
	}

	public String getBillingNote() {
		return billingNote;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CustomerOverviewDTO [");
		builder.append(super.toString());
		builder.append(", note=");
		builder.append(note);
		builder.append(", billingNote=");
		builder.append(billingNote);
		builder.append("]");
		return builder.toString();
	}

}
