package cz.vsb.resbill.dto.contract;

import java.util.ArrayList;
import java.util.List;

import cz.vsb.resbill.dto.person.PersonDTO;
import cz.vsb.resbill.model.Contract;
import cz.vsb.resbill.model.ContractPerson;

public class ContractOverviewDTO extends ContractListDTO {

	private static final long serialVersionUID = -9117722121771554771L;

	private String note;

	private PersonDTO customerContactPerson;

	private List<PersonDTO> responsiblePersons;

	public ContractOverviewDTO(Contract contract, List<ContractPerson> contractPersons) {
		super(contract);
		fill(contractPersons);
	}

	@Override
	protected void fill(Contract contract) {
		super.fill(contract);
		if (contract != null) {
			this.note = contract.getNote();
			if (contract.getCustomer() != null) {
				this.customerContactPerson = new PersonDTO(contract.getCustomer().getContactPerson());
			}
		}
	}

	protected void fill(List<ContractPerson> contractPersons) {
		if (contractPersons != null) {
			this.responsiblePersons = new ArrayList<PersonDTO>(contractPersons.size());
			for (ContractPerson cp : contractPersons) {
				this.responsiblePersons.add(new PersonDTO(cp.getPerson()));
			}
		}
	}

	public String getNote() {
		return note;
	}

	public PersonDTO getCustomerContactPerson() {
		return customerContactPerson;
	}

	public List<PersonDTO> getResponsiblePersons() {
		return responsiblePersons;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ContractOverviewDTO [");
		builder.append(super.toString());
		builder.append(", note=");
		builder.append(note);
		builder.append(", customerContactPerson=");
		builder.append(customerContactPerson);
		builder.append(", responsiblePersons=");
		builder.append(responsiblePersons);
		builder.append("]");
		return builder.toString();
	}

}
