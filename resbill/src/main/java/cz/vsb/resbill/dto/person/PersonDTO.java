package cz.vsb.resbill.dto.person;

import java.io.Serializable;

import cz.vsb.resbill.model.Person;

public class PersonDTO implements Serializable {

	private static final long serialVersionUID = -5353520547525276593L;

	private Integer personId;

	private String email;

	private String fullName;

	private String titles;

	private String phone;

	private String address;

	public PersonDTO(Person p) {
		if (p != null) {
			this.personId = p.getId();
			this.email = p.getEmail();
			this.fullName = p.getFullName();
			this.titles = p.getTitles();
			this.phone = p.getPhone();
			if (p.getAddress() != null) {
				this.address = p.getAddress().getInlineFormat();
			}
		}
	}

	public Integer getPersonId() {
		return personId;
	}

	public void setPersonId(Integer personId) {
		this.personId = personId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getTitles() {
		return titles;
	}

	public void setTitles(String titles) {
		this.titles = titles;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PersonDTO [");
		builder.append(super.toString());
		builder.append(", personId=");
		builder.append(personId);
		builder.append(", email=");
		builder.append(email);
		builder.append(", fullName=");
		builder.append(fullName);
		builder.append(", titles=");
		builder.append(titles);
		builder.append(", phone=");
		builder.append(phone);
		builder.append(", address=");
		builder.append(address);
		builder.append("]");
		return builder.toString();
	}

}
