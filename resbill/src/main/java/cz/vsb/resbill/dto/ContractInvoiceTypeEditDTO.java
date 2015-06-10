package cz.vsb.resbill.dto;

import java.io.Serializable;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import cz.vsb.resbill.model.ContractInvoiceType;

public class ContractInvoiceTypeEditDTO implements Serializable {

	private static final long serialVersionUID = -7260266505503391388L;

	@Valid
	@NotNull
	private ContractInvoiceType contractInvoiceType;

	@NotNull
	private Integer contractId;

	@NotNull
	private Integer invoiceTypeId;

	private boolean contractEditable = true;

	private boolean invoiceTypeEditable = true;

	public ContractInvoiceTypeEditDTO(ContractInvoiceType ct) {
		this.contractInvoiceType = ct;
		if (ct != null) {
			this.contractId = ct.getContract() != null ? ct.getContract().getId() : null;
			this.invoiceTypeId = ct.getInvoiceType() != null ? ct.getInvoiceType().getId() : null;
			this.contractEditable = ct.getId() == null;
			this.invoiceTypeEditable = ct.getId() == null;
		}
	}

	public ContractInvoiceType getContractInvoiceType() {
		return contractInvoiceType;
	}

	public void setContractInvoiceType(ContractInvoiceType contractInvoiceType) {
		this.contractInvoiceType = contractInvoiceType;
	}

	public Integer getContractId() {
		return contractId;
	}

	public void setContractId(Integer contractId) {
		this.contractId = contractId;
	}

	public Integer getInvoiceTypeId() {
		return invoiceTypeId;
	}

	public void setInvoiceTypeId(Integer invoiceTypeId) {
		this.invoiceTypeId = invoiceTypeId;
	}

	public boolean isContractEditable() {
		return contractEditable;
	}

	public void setContractEditable(boolean contractEditable) {
		this.contractEditable = contractEditable;
	}

	public boolean isInvoiceTypeEditable() {
		return invoiceTypeEditable;
	}

	public void setInvoiceTypeEditable(boolean invoiceTypeEditable) {
		this.invoiceTypeEditable = invoiceTypeEditable;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ContractInvoiceTypeEditDTO [");
		builder.append(super.toString());
		builder.append(", contractInvoiceType=");
		builder.append(contractInvoiceType);
		builder.append(", contractId=");
		builder.append(contractId);
		builder.append(", invoiceTypeId=");
		builder.append(invoiceTypeId);
		builder.append(", contractEditable=");
		builder.append(contractEditable);
		builder.append(", invoiceTypeEditable=");
		builder.append(invoiceTypeEditable);
		builder.append("]");
		return builder.toString();
	}

}
