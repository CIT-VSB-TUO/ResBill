package cz.vsb.resbill.dto;

import java.io.Serializable;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import cz.vsb.resbill.model.ContractServer;
import cz.vsb.resbill.model.Server;

public class ServerEditDTO implements Serializable {

	private static final long serialVersionUID = 1386564327039919841L;

	@Valid
	@NotNull
	private Server server;

	private ContractServer currentContractServer;

	public ServerEditDTO(Server server, ContractServer contractServer) {
		this.server = server;
		this.currentContractServer = contractServer;
	}

	public Server getServer() {
		return server;
	}

	public void setServer(Server server) {
		this.server = server;
	}

	public ContractServer getCurrentContractServer() {
		return currentContractServer;
	}

	public void setCurrentContractServer(ContractServer currentContractServer) {
		this.currentContractServer = currentContractServer;
	}

	public boolean isServerIdEditable() {
		return getServer() != null && getServer().getId() == null;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ServerEditDTO [");
		builder.append(super.toString());
		builder.append(", server=");
		builder.append(server);
		builder.append("]");
		return builder.toString();
	}

}
