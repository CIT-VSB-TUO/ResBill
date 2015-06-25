package cz.vsb.resbill.dto.contract;

import cz.vsb.resbill.model.Contract;

public class ContractHeaderDTO extends ContractDTO {

	private static final long serialVersionUID = -6153827345498493534L;

	public ContractHeaderDTO(Contract contract) {
		super(contract);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ContractHeaderDTO [");
		builder.append(super.toString());
		builder.append(", ]");
		return builder.toString();
	}

}
