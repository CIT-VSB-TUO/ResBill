package cz.vsb.resbill.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "CUSTOMER", uniqueConstraints = @UniqueConstraint(name = "UK_customer__name", columnNames = "name"))
public class Customer extends BaseVersionedEntity {

	private static final long serialVersionUID = 4230333908768136588L;

	@Column(name = "name", length = 250, nullable = false)
	private String name;

	@Column(name = "note", length = 1000)
	private String note;

	@Column(name = "billing_note", length = 1000)
	private String billingNote;

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "contact_person_id", nullable = false, foreignKey = @ForeignKey(name = "FK_customer__contact_person"))
	private Person contactPerson;

	@OneToMany(mappedBy = "customer", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<Contract> contracts = new HashSet<>();

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
		StringBuilder builder = new StringBuilder();
		builder.append("Customer [");
		builder.append(super.toString());
		builder.append(", name=");
		builder.append(name);
		builder.append(", note=");
		builder.append(note);
		builder.append(", billingNote=");
		builder.append(billingNote);
		builder.append(", contactPerson.id=");
		builder.append(contactPerson != null ? contactPerson.getId() : null);
		builder.append("]");
		return builder.toString();
	}

}
