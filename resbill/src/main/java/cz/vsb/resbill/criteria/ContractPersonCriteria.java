package cz.vsb.resbill.criteria;

import java.io.Serializable;

import cz.vsb.resbill.model.ContractPerson;

/**
 * A criteria class for searching {@link ContractPerson} entities.
 * 
 * @author HAL191
 *
 */
public class ContractPersonCriteria implements Serializable {

	private static final long serialVersionUID = 1639391400931690883L;

	private Integer contractId;

	private Integer personId;

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

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ContractPersonCriteria [");
		builder.append(super.toString());
		builder.append(", contractId=");
		builder.append(contractId);
		builder.append(", personId=");
		builder.append(personId);
		builder.append("]");
		return builder.toString();
	}

}
