package cz.vsb.resbill.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class ContractServer {

	private Period period;
	private Contract contract;
	private Server server;

	public Period getPeriod() {
		return period;
	}

	public void setPeriod(Period period) {
		this.period = period;
	}

	public Contract getContract() {
		return contract;
	}

	public void setContract(Contract contract) {
		this.contract = contract;
	}

	public Server getServer() {
		return server;
	}

	public void setServer(Server server) {
		this.server = server;
	}

	@Override
	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this);
		builder.append("period", period);
		builder.append("contract", contract);
		builder.append("server", server);
		return builder.toString();
	}
}
