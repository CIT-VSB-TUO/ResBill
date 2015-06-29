package cz.vsb.resbill.dto.customer;

import java.util.ArrayList;
import java.util.List;

import cz.vsb.resbill.dto.contract.ContractDTO;
import cz.vsb.resbill.model.Contract;
import cz.vsb.resbill.model.Customer;

public class CustomerOverviewDTO extends CustomerListDTO {

	private static final long serialVersionUID = 729046709667145940L;

	private String note;

	private String billingNote;

	private List<ContractDTO> contracts;

	public CustomerOverviewDTO(Customer customer, List<Contract> contracts) {
		super(customer);
		fill(contracts);
	}

	@Override
	protected void fill(Customer customer) {
		super.fill(customer);
		if (customer != null) {
			this.note = customer.getNote();
			this.billingNote = customer.getBillingNote();
		}
	}

	protected void fill(List<Contract> contracts) {
		if (contracts != null) {
			this.contracts = new ArrayList<ContractDTO>(contracts.size());
			for (Contract contract : contracts) {
				this.contracts.add(new ContractDTO(contract));
			}
		}
	}

	public String getNote() {
		return note;
	}

	public String getBillingNote() {
		return billingNote;
	}

	public List<ContractDTO> getContracts() {
		return contracts;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CustomerOverviewDTO [");
		builder.append(super.toString());
		builder.append(", note=");
		builder.append(note);
		builder.append(", billingNote=");
		builder.append(billingNote);
		builder.append(", contracts=");
		builder.append(contracts);
		builder.append("]");
		return builder.toString();
	}

}
