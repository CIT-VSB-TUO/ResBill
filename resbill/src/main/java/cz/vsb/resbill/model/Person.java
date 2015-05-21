package cz.vsb.resbill.model;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.Valid;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name = "PERSON", uniqueConstraints = @UniqueConstraint(name = "UK_person__email", columnNames = "email"))
public class Person extends BaseVersionedEntity {

	private static final long serialVersionUID = -7882942149701501035L;

	@Column(name = "email")
	@NotEmpty
	@Size(max = 250)
	@Email
	private String email;

	@Column(name = "first_name")
	@Size(max = 250)
	private String firstName;

	@Column(name = "second_name")
	@Size(max = 500)
	private String secondName;

	@Column(name = "title_before")
	@Size(max = 30)
	private String titleBefore;

	@Column(name = "title_after")
	@Size(max = 30)
	private String titleAfter;

	@Column(name = "phone")
	@Size(max = 40)
	private String phone;

	@Column(name = "note")
	@Size(max = 1000)
	private String note;

	@Embedded
	@Valid
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

	/**
	 * Returns the full name in the form: 'first_name second_name'
	 * 
	 * @return
	 */
	public String getFullName() {
		StringBuilder sb = new StringBuilder();

		if (StringUtils.isNotEmpty(getFirstName())) {
			sb.append(getFirstName());
		}
		if (StringUtils.isNotEmpty(getSecondName())) {
			if (sb.length() > 0) sb.append(" ");
			sb.append(getSecondName());
		}

		return sb.toString();
	}

	/**
	 * Returns the full name with titles in the form: 'title_before first_name
	 * second_name, title_after'
	 * 
	 * @return
	 */
	public String getFullNameWithTitles() {
		StringBuilder sb = new StringBuilder();

		if (StringUtils.isNotEmpty(getTitleBefore())) {
			sb.append(getTitleBefore());
		}
		if (StringUtils.isNotEmpty(getFirstName())) {
			if (sb.length() > 0) sb.append(" ");
			sb.append(getFirstName());
		}
		if (StringUtils.isNotEmpty(getSecondName())) {
			if (sb.length() > 0) sb.append(" ");
			sb.append(getSecondName());
		}
		if (StringUtils.isNotEmpty(getTitleAfter())) {
			if (sb.length() > 0) sb.append(", ");
			sb.append(getTitleAfter());
		}

		return sb.toString();
	}

	/**
	 * Returns titles in the form: 'title_before, title_after'
	 * 
	 * @return
	 */
	public String getTitles() {
		StringBuilder sb = new StringBuilder();

		if (StringUtils.isNotEmpty(getTitleBefore())) {
			sb.append(getTitleBefore());
		}
		if (StringUtils.isNotEmpty(getTitleAfter())) {
			if (sb.length() > 0) sb.append(", ");
			sb.append(getTitleAfter());
		}

		return sb.toString();
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
