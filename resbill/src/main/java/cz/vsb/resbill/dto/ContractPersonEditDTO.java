package cz.vsb.resbill.dto;

import java.io.Serializable;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import cz.vsb.resbill.model.ContractPerson;

public class ContractPersonEditDTO implements Serializable {

	private static final long serialVersionUID = 8891062544223739667L;

	@Valid
	@NotNull
	private ContractPerson contractPerson;

	@NotNull
	private Integer contractId;

	@NotNull
	private Integer personId;

	private boolean contractEditable = true;

	private boolean personEditable = true;

	public ContractPersonEditDTO(ContractPerson ct) {
		this.contractPerson = ct;
		if (ct != null) {
			this.contractId = ct.getContract() != null ? ct.getContract().getId() : null;
			this.personId = ct.getPerson() != null ? ct.getPerson().getId() : null;
			this.contractEditable = ct.getId() == null;
			this.personEditable = ct.getId() == null;
		}
	}

	public ContractPerson getContractPerson() {
		return contractPerson;
	}

	public void setContractPerson(ContractPerson contractPerson) {
		this.contractPerson = contractPerson;
	}

	public Integer getContractId() {
		return contractId;
	}

	public void setContractId(Integer contractId) {
		this.contractId = contractId;
	}

	public Integer getPersonId() {
		return personId;
	}

	public void setPersonId(Integer personId) {
		this.personId = personId;
	}

	public boolean isContractEditable() {
		return contractEditable;
	}

	public void setContractEditable(boolean contractEditable) {
		this.contractEditable = contractEditable;
	}

	public boolean isPersonEditable() {
		return personEditable;
	}

	public void setPersonEditable(boolean personEditable) {
		this.personEditable = personEditable;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ContractPersonEditDTO [");
		builder.append(super.toString());
		builder.append(", contractPerson=");
		builder.append(contractPerson);
		builder.append(", contractId=");
		builder.append(contractId);
		builder.append(", personId=");
		builder.append(personId);
		builder.append(", contractEditable=");
		builder.append(contractEditable);
		builder.append(", personEditable=");
		builder.append(personEditable);
		builder.append("]");
		return builder.toString();
	}

}
