package cz.vsb.resbill.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class Person {

	private String email;
	private String firstName;
	private String secondName;
	private String titleBefore;
	private String titleAfter;
	private String phone;
	private String note;
	private Address address;

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

	public String getTitleBefore() {
		return titleBefore;
	}

	public void setTitleBefore(String titleBefore) {
		this.titleBefore = titleBefore;
	}

	public String getTitleAfter() {
		return titleAfter;
	}

	public void setTitleAfter(String titleAfter) {
		this.titleAfter = titleAfter;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	@Override
	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this);
		builder.append("email", email);
		builder.append("firstName", firstName);
		builder.append("secondName", secondName);
		builder.append("titleBefore", titleBefore);
		builder.append("titleAfter", titleAfter);
		builder.append("phone", phone);
		builder.append("note", note);
		builder.append("address", address);
		return builder.toString();
	}

}
