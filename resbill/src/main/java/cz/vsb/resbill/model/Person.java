package cz.vsb.resbill.model;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "PERSON", uniqueConstraints = @UniqueConstraint(name = "UK_person__email", columnNames = "email"))
public class Person extends BaseVersionedEntity {

	private static final long serialVersionUID = -7882942149701501035L;

	@Column(name = "email", length = 250, nullable = false)
	private String email;

	@Column(name = "first_name", length = 250)
	private String firstName;

	@Column(name = "second_name", length = 500)
	private String secondName;

	@Column(name = "title_before", length = 30)
	private String titleBefore;

	@Column(name = "title_after", length = 30)
	private String titleAfter;

	@Column(name = "phone", length = 40)
	private String phone;

	@Column(name = "note", length = 1000)
	private String note;

	@Embedded
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
		StringBuilder builder = new StringBuilder();
		builder.append("Person [");
		builder.append(super.toString());
		builder.append(", email=");
		builder.append(email);
		builder.append(", firstName=");
		builder.append(firstName);
		builder.append(", secondName=");
		builder.append(secondName);
		builder.append(", titleBefore=");
		builder.append(titleBefore);
		builder.append(", titleAfter=");
		builder.append(titleAfter);
		builder.append(", phone=");
		builder.append(phone);
		builder.append(", note=");
		builder.append(note);
		builder.append(", address=");
		builder.append(address);
		builder.append("]");
		return builder.toString();
	}

}
