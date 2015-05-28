package cz.vsb.resbill.criteria;

import java.io.Serializable;

import cz.vsb.resbill.model.ContractTariff;

/**
 * A criteria class for searching {@link ContractTariff} entities.
 * 
 * @author HAL191
 *
 */
public class ContractTariffCriteria implements Serializable {

	private static final long serialVersionUID = 3752663052905075572L;

	private Integer contractId;

	private Integer tariffId;

	public Integer getContractId() {
		return contractId;
	}

	public void setContractId(Integer contractId) {
		this.contractId = contractId;
	}

	public Integer getTariffId() {
		return tariffId;
	}

	public void setTariffId(Integer tariffId) {
		this.tariffId = tariffId;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ContractTariffCriteria [");
		builder.append(super.toString());
		builder.append(", contractId=");
		builder.append(contractId);
		builder.append(", tariffId=");
		builder.append(tariffId);
		builder.append("]");
		return builder.toString();
	}

}
