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

	public static enum OrderBy {
		PERSON_NAME_ASC, PERSON_NAME_DESC
	}

	private Integer contractId;

	private Integer personId;

	private OrderBy orderBy;

	private boolean fetchContract;

	private boolean fetchPerson;

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

	public OrderBy getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(OrderBy orderBy) {
		this.orderBy = orderBy;
	}

	public boolean isFetchContract() {
		return fetchContract;
	}

	public void setFetchContract(boolean fetchContract) {
		this.fetchContract = fetchContract;
	}

	public boolean isFetchPerson() {
		return fetchPerson;
	}

	public void setFetchPerson(boolean fetchPerson) {
		this.fetchPerson = fetchPerson;
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
		builder.append(", orderBy=");
		builder.append(orderBy);
		builder.append(", fetchContract=");
		builder.append(fetchContract);
		builder.append(", fetchPerson=");
		builder.append(fetchPerson);
		builder.append("]");
		return builder.toString();
	}

}
