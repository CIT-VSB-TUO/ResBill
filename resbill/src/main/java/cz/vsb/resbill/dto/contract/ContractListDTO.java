package cz.vsb.resbill.dto.contract;

import cz.vsb.resbill.dto.customer.CustomerDTO;
import cz.vsb.resbill.model.Contract;

public class ContractListDTO extends ContractDTO {

	private static final long serialVersionUID = 1L;

	private CustomerDTO customer;

	public ContractListDTO(Contract contract) {
		super(contract);
	}

	@Override
	protected void fill(Contract contract) {
		super.fill(contract);
		if (contract != null) {
			customer = new CustomerDTO(contract.getCustomer());
		}
	}

	public CustomerDTO getCustomer() {
		return customer;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ContractListDTO [");
		builder.append(super.toString());
		builder.append(", customer=");
		builder.append(customer);
		builder.append("]");
		return builder.toString();
	}

}
