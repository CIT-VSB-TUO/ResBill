package cz.vsb.resbill.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "CONTRACT_PERSON", uniqueConstraints = @UniqueConstraint(name = "UK_contract_person__contract_id__person_id", columnNames = { "contract_id", "person_id" }))
public class ContractPerson extends BaseVersionedEntity {

	private static final long serialVersionUID = 6372466046222089422L;

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "contract_id", nullable = false, foreignKey = @ForeignKey(name = "FK_contract_person__contract"))
	private Contract contract;

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "person_id", nullable = false, foreignKey = @ForeignKey(name = "FK_contract_person__person"))
	private Person person;

	public Contract getContract() {
		return contract;
	}

	public void setContract(Contract contract) {
		this.contract = contract;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ContractPerson [");
		builder.append(super.toString());
		builder.append(", contract.id=");
		builder.append(contract != null ? contract.getId() : null);
		builder.append(", person.id=");
		builder.append(person != null ? person.getId() : null);
		builder.append("]");
		return builder.toString();
	}

}
