package cz.vsb.resbill.dto.contract;

import cz.vsb.resbill.dto.person.PersonDTO;
import cz.vsb.resbill.model.Contract;

public class ContractOverviewDTO extends ContractListDTO {

	private static final long serialVersionUID = -9117722121771554771L;

	private String note;

	private PersonDTO contactPerson;

	public ContractOverviewDTO(Contract contract) {
		super(contract);
	}

	@Override
	protected void fill(Contract contract) {
		super.fill(contract);
		if (contract != null) {
			this.note = contract.getNote();
			if (contract.getCustomer() != null) {
				this.contactPerson = new PersonDTO(contract.getCustomer().getContactPerson());
			}
		}
	}

	public String getNote() {
		return note;
	}

	public PersonDTO getContactPerson() {
		return contactPerson;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ContractOverviewDTO [");
		builder.append(super.toString());
		builder.append(", note=");
		builder.append(note);
		builder.append(", contactPerson=");
		builder.append(contactPerson);
		builder.append("]");
		return builder.toString();
	}

}
