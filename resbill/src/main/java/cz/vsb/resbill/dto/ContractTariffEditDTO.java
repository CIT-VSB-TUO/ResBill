package cz.vsb.resbill.dto;

import java.io.Serializable;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import cz.vsb.resbill.model.ContractTariff;

public class ContractTariffEditDTO implements Serializable {

	private static final long serialVersionUID = -7260266505503391388L;

	@Valid
	@NotNull
	private ContractTariff contractTariff;

	@NotNull
	private Integer contractId;

	@NotNull
	private Integer tariffId;

	private boolean contractEditable = true;

	private boolean tariffEditable = true;

	public ContractTariffEditDTO(ContractTariff ct) {
		this.contractTariff = ct;
		if (ct != null) {
			this.contractId = ct.getContract() != null ? ct.getContract().getId() : null;
			this.tariffId = ct.getTariff() != null ? ct.getTariff().getId() : null;
			this.contractEditable = ct.getId() == null;
			this.tariffEditable = ct.getId() == null;
		}
	}

	public ContractTariff getContractTariff() {
		return contractTariff;
	}

	public void setContractTariff(ContractTariff contractTariff) {
		this.contractTariff = contractTariff;
	}

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

	public boolean isContractEditable() {
		return contractEditable;
	}

	public void setContractEditable(boolean contractEditable) {
		this.contractEditable = contractEditable;
	}

	public boolean isTariffEditable() {
		return tariffEditable;
	}

	public void setTariffEditable(boolean tariffEditable) {
		this.tariffEditable = tariffEditable;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ContractTariffEditDTO [");
		builder.append(super.toString());
		builder.append(", contractTariff=");
		builder.append(contractTariff);
		builder.append(", contractId=");
		builder.append(contractId);
		builder.append(", tariffId=");
		builder.append(tariffId);
		builder.append(", contractEditable=");
		builder.append(contractEditable);
		builder.append(", tariffEditable=");
		builder.append(tariffEditable);
		builder.append("]");
		return builder.toString();
	}

}
