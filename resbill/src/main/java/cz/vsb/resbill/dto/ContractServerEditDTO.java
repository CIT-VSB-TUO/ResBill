package cz.vsb.resbill.dto;

import java.io.Serializable;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import cz.vsb.resbill.model.ContractServer;

public class ContractServerEditDTO implements Serializable {

	private static final long serialVersionUID = -6476040712642948180L;

	@Valid
	@NotNull
	private ContractServer contractServer;

	@NotNull
	private Integer contractId;

	@NotNull
	private Integer serverId;

	private boolean contractEditable = true;

	private boolean serverEditable = true;

	public ContractServerEditDTO(ContractServer cs) {
		this.contractServer = cs;
		if (cs != null) {
			this.contractId = cs.getContract() != null ? cs.getContract().getId() : null;
			this.serverId = cs.getServer() != null ? cs.getServer().getId() : null;
			this.contractEditable = cs.getId() == null;
			this.serverEditable = cs.getId() == null;
		}
	}

	public ContractServer getContractServer() {
		return contractServer;
	}

	public void setContractServer(ContractServer contractServer) {
		this.contractServer = contractServer;
	}

	public Integer getContractId() {
		return contractId;
	}

	public void setContractId(Integer contractId) {
		this.contractId = contractId;
	}

	public Integer getServerId() {
		return serverId;
	}

	public void setServerId(Integer serverId) {
		this.serverId = serverId;
	}

	public boolean isContractEditable() {
		return contractEditable;
	}

	public void setContractEditable(boolean contractEditable) {
		this.contractEditable = contractEditable;
	}

	public boolean isServerEditable() {
		return serverEditable;
	}

	public void setServerEditable(boolean serverEditable) {
		this.serverEditable = serverEditable;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ContractServerEditDTO [");
		builder.append(super.toString());
		builder.append(", contractServer=");
		builder.append(contractServer);
		builder.append(", contractId=");
		builder.append(contractId);
		builder.append(", serverId=");
		builder.append(serverId);
		builder.append(", contractEditable=");
		builder.append(contractEditable);
		builder.append(", serverEditable=");
		builder.append(serverEditable);
		builder.append("]");
		return builder.toString();
	}

}
