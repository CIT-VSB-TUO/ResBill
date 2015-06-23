package cz.vsb.resbill.dto.person;

import java.io.Serializable;

import cz.vsb.resbill.dto.AddressDTO;
import cz.vsb.resbill.model.Person;

public class PersonDTO implements Serializable {

	private static final long serialVersionUID = -5353520547525276593L;

	private Integer personId;

	private String email;

	private String fullName;

	private String fullNameWithTitles;

	private String titles;

	private String phone;

	private AddressDTO address;

	public PersonDTO(Person p) {
		fill(p);
	}

	protected void fill(Person p) {
		if (p != null) {
			this.personId = p.getId();
			this.email = p.getEmail();
			this.fullName = p.getFullName();
			this.fullNameWithTitles = p.getFullNameWithTitles();
			this.titles = p.getTitles();
			this.phone = p.getPhone();
			this.address = new AddressDTO(p.getAddress());
		}
	}

	public Integer getPersonId() {
		return personId;
	}

	public String getEmail() {
		return email;
	}

	public String getFullName() {
		return fullName;
	}

	public String getFullNameWithTitles() {
		return fullNameWithTitles;
	}

	public String getTitles() {
		return titles;
	}

	public String getPhone() {
		return phone;
	}

	public AddressDTO getAddress() {
		return address;
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
		builder.append(", fullNameWithTitles=");
		builder.append(fullNameWithTitles);
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
