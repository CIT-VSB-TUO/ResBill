package cz.vsb.resbill.dto;

import java.io.Serializable;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import cz.vsb.resbill.model.Contract;

public class ContractEditDTO implements Serializable {

	private static final long serialVersionUID = 2585516902814048464L;

	@Valid
	@NotNull
	private Contract contract;

	@NotNull
	private Integer customerId;

	private boolean customerEditable;

	public ContractEditDTO(Contract contract) {
		this.contract = contract;
		if (contract != null) {
			this.customerId = contract.getCustomer() != null ? contract.getCustomer().getId() : null;
			this.customerEditable = contract.getId() == null;
		}
	}

	public Contract getContract() {
		return contract;
	}

	public void setContract(Contract contract) {
		this.contract = contract;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public boolean isCustomerEditable() {
		return customerEditable;
	}

	public void setCustomerEditable(boolean customerEditable) {
		this.customerEditable = customerEditable;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ContractEditDTO [");
		builder.append(super.toString());
		builder.append(", contract=");
		builder.append(contract);
		builder.append(", customerId=");
		builder.append(customerId);
		builder.append(", customerEditable=");
		builder.append(customerEditable);
		builder.append("]");
		return builder.toString();
	}

}
