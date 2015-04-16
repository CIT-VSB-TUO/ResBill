package cz.vsb.resbill.model;

import java.util.Set;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class Customer {

	private String name;
	private String note;
	private String billingNote;
	private Person contactPerson;
	private Set<Contract> contracts;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getBillingNote() {
		return billingNote;
	}

	public void setBillingNote(String billingNote) {
		this.billingNote = billingNote;
	}

	public Person getContactPerson() {
		return contactPerson;
	}

	public void setContactPerson(Person contactPerson) {
		this.contactPerson = contactPerson;
	}

	public Set<Contract> getContracts() {
		return contracts;
	}

	public void setContracts(Set<Contract> contracts) {
		this.contracts = contracts;
	}

	@Override
	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this);
		builder.append("name", name);
		builder.append("note", note);
		builder.append("billingNote", billingNote);
		builder.append("contactPerson", contactPerson);
		builder.append("contracts", contracts);
		return builder.toString();
	}

}
